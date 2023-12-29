package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.business.repositories.biomass.ProjectedBiomassRepository;
import cl.camanchaca.business.utils.SizeUtils;
import cl.camanchaca.domain.dtos.biomass.ProjectedBiomassDTO;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.biomass.ProjectedBiomass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CalculatedCorrectionFacorUseCase {

    private final ProjectedBiomassRepository projectedBiomassRepository;
    private final SizeRepository sizeRepository;

    private static final String PREMIUM_VALUE = "Piezas Premium";

    public Flux<ProjectedBiomass> apply(String specie) {

        return sizeRepository.getBySpecie(specie)
                .sort(SizeUtils.sortSizes())
                .flatMap(size -> projectedBiomassRepository
                        .getBySizeId(size.getId())
                        .map(projectedBiomass -> this.toDto(projectedBiomass, size))
                )
                .groupBy(ProjectedBiomassDTO::getDay)
                .flatMap(GroupedFlux::collectList)
                .map(this::gruopedByPieceType)
                .flatMap(this::apply)
                .flatMap(projectedBiomassRepository::save, 3);


    }

    private Flux<ProjectedBiomass> apply(List<List<ProjectedBiomassDTO>> lists) {
        lists.sort(sortLists());
        lists.forEach(this::sortListPtoByRange);
        return Mono.defer(() -> Mono.just(lists))
                .map(projectedBiomassDTOS ->
                        recurrent(projectedBiomassDTOS, 0)
                )
                .flatMapMany(Flux::fromIterable)
                .flatMap(Flux::fromIterable)
                .map(this::toData);

    }

    private List<List<ProjectedBiomassDTO>> recurrent(List<List<ProjectedBiomassDTO>> lists, int lap) {


        var currentTotalWfe = calculateTotalWfe(lists);
        if (Objects.equals(lap, 5) || currentTotalWfe.equals(BigDecimal.ZERO)) {
            return lists;
        }
        calculatedPremium(lists.get(0), currentTotalWfe);

        lists = updateWfe(lists);

        currentTotalWfe = calculateTotalWfe(lists);
        for (int i = 1; i < lists.size(); i++) {
            var noPremiumList = lists.get(i);
            int lastIndex = noPremiumList.size() - 1;

            findFactorAndwfe(noPremiumList, lastIndex, currentTotalWfe);

            lists = updateWfe(lists);
            currentTotalWfe = calculateTotalWfe(lists);
        }

        return recurrent(lists, lap + 1);
    }


    private List<List<ProjectedBiomassDTO>> updateWfe(List<List<ProjectedBiomassDTO>> lists) {
        return lists.stream()
                .map(projectedBiomassDTOS -> projectedBiomassDTOS
                        .stream().map(this::calculateWfe)
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }

    private void sortListPtoByRange(List<ProjectedBiomassDTO> list) {
        list.sort(sortDtoByInitialRange());
    }

    private void calculatedPremium(List<ProjectedBiomassDTO> listPremium, BigDecimal currentTotalWfe) {
        int midIndex = listPremium.size() / 2;

        findFactorAndwfe(listPremium, midIndex, currentTotalWfe);

    }

    private void findFactorAndwfe(List<ProjectedBiomassDTO> list, int index, BigDecimal currentTotalWfe) {
        ProjectedBiomassDTO element = list
                .get(index);


        var avgwWeightFactor = calculateFactor(element, currentTotalWfe);
        avgwWeightFactor = validateRange(
                avgwWeightFactor,
                element.getSize().getInitialRange(),
                element.getSize().getFinalRange()
        );
        var res = avgwWeightFactor.multiply(BigDecimal.valueOf(element.getPieces()));

        element.setKilosWFEScenario(res);

    }

    private static Comparator<ProjectedBiomassDTO> sortDtoByInitialRange() {
        return Comparator.comparing(o -> o.getSize().getInitialRange());
    }

    private Comparator<List<ProjectedBiomassDTO>> sortLists() {
        return Comparator.comparing(projectedBiomassDTOS -> !PREMIUM_VALUE.equals(projectedBiomassDTOS.get(0).getSize().getPieceType()));
    }

    private ProjectedBiomassDTO calculateWfe(ProjectedBiomassDTO data) {

        BigDecimal inSize = data.getSize().getInitialRange();
        BigDecimal endSize = data.getSize().getFinalRange();

        BigDecimal avgSize = inSize.add(endSize)
                .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_EVEN);

        var wfe = avgSize.multiply(data.getAvgWeight())
                .setScale(2, RoundingMode.HALF_EVEN);

        return data.toBuilder()
                .totalKilosWFEScenario(wfe)
                .build();
    }

    private BigDecimal calculateTotalWfe(List<List<ProjectedBiomassDTO>> lists) {
        return lists
                .stream()
                .map(projectedBiomassDTOS -> projectedBiomassDTOS
                        .stream().map(ProjectedBiomassDTO::getKilosWFEScenario)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);


    }


    private BigDecimal calculateFactor(ProjectedBiomassDTO element, BigDecimal totalWfe) {

        var wfeWithElement = totalWfe.subtract(element.getKilosWFEScenario());

        if (element.getPieces() == 0) {
            return BigDecimal.ZERO;
        }
        var expectedWeight = element.getKilosWFE().subtract(wfeWithElement);
        return expectedWeight.divide(
                BigDecimal.valueOf(element.getPieces()), 10, RoundingMode.HALF_EVEN
        );
    }


    private List<List<ProjectedBiomassDTO>> gruopedByPieceType(List<ProjectedBiomassDTO> data) {
        var sumTotalWfe = data.stream()
                .map(ProjectedBiomassDTO::getKilosWFEScenario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ArrayList<>(data
                .stream()
                .map(projectedBiomassDTO -> projectedBiomassDTO.toBuilder()
                        .totalKilosWFEScenario(sumTotalWfe)
                        .build())
                .collect(Collectors.groupingBy(
                        projectedBiomassDTO -> projectedBiomassDTO.getSize().getPieceType()
                ))
                .values());
    }

    private BigDecimal validateRange(BigDecimal value, BigDecimal minimum, BigDecimal max) {
        if (value.compareTo(minimum) < 0) {
            return minimum;
        } else if (value.compareTo(max) > 0) {
            return max;
        } else {
            return value;
        }
    }

    private ProjectedBiomassDTO toDto(ProjectedBiomass data, Size size) {
        return ProjectedBiomassDTO
                .builder()
                .id(data.getId())
                .size(size)
                .pieces(data.getPieces())
                .day(data.getProjectionDate())
                .avgWeight(data.getAvgWeight())
                .kilosWFE(data.getKilosWFE())
                .kilosWFEScenario(data.getKilosWFEScenario())
                .quality(data.getQuality().doubleValue())
                .build();
    }


    private ProjectedBiomass toData(ProjectedBiomassDTO data) {
        return ProjectedBiomass
                .builder()
                .id(data.getId())
                .sizeId(data.getSize().getId())
                .pieces(data.getPieces())
                .projectionDate(data.getDay())
                .avgWeight(data.getAvgWeight())
                .kilosWFE(data.getKilosWFE())
                .kilosWFEScenario(data.getKilosWFEScenario())
                .quality(BigDecimal.valueOf(data.getQuality()))
                .build();
    }
}


