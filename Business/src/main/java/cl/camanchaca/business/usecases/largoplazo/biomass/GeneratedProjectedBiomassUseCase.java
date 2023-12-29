package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.business.repositories.biomass.ProjectedBiomassRepository;
import cl.camanchaca.business.responses.ProjectedBiomassResponse;
import cl.camanchaca.business.utils.ProjectedBiomassAccumulator;
import cl.camanchaca.business.utils.SizeUtils;
import cl.camanchaca.domain.dtos.biomass.ProjectedBiomassDTO;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.biomass.ProjectedBiomass;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.NormalDistribution;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;


@Slf4j
@AllArgsConstructor
public class GeneratedProjectedBiomassUseCase extends Usecase<List<ProjectedBiomassDTO>, Flux<ProjectedBiomassResponse>> {

    private final SizeRepository sizeRepository;
    private final ProjectedBiomassRepository projectedBiomassRepository;
    private final GetProjectBiomassBySpecieUseCase getProjectBiomassBySpecieUseCase;
    private final CalculatedCorrectionFacorUseCase calculatedCorrectionFacorUseCase;

    @Override
    public Flux<ProjectedBiomassResponse> apply(List<ProjectedBiomassDTO> projectedBiomassDTOs) {
        String specie = projectedBiomassDTOs.get(0).getSpecie();
        return sizeRepository.getBySpecie(specie)
                .flatMap(size -> projectedBiomassRepository.deleteAllBySizeId(size.getId())
                        .then(Mono.just(size))
                )
                .collectList()
                .flatMapMany(sizes ->
                        Flux.fromIterable(projectedBiomassDTOs)
                                .map(projectedBiomassDTO -> {

                                    double availablePieces = projectedBiomassDTO.getNumber();
                                    double qualityValPremium = Objects.requireNonNullElse(projectedBiomassDTO.getQuality(), 100d) / 100;
                                    projectedBiomassDTO.setTotalNumber(availablePieces);

                                    return this.calculateDataPerDay(
                                            sizes,
                                            availablePieces,
                                            qualityValPremium,
                                            projectedBiomassDTO
                                    );
                                })

                )
                .flatMap(this::mapResponse)
                .thenMany(calculatedCorrectionFacorUseCase.apply(specie))
                .thenMany(getProjectBiomassBySpecieUseCase.apply(specie))
                .sort(SizeUtils.sortProjected());

    }


    private BigDecimal calculateAverage(List<BigDecimal> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = numbers.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(new BigDecimal(numbers.size()), 10, RoundingMode.HALF_EVEN);
    }

    private Flux<ProjectedBiomass> mapResponse(ProjectedBiomassAccumulator accumulator) {
        return Flux.fromIterable(accumulator.getProjectedBiomasses())
                .flatMap(projectedBiomassRepository::save, 3);
    }

    private ProjectedBiomassAccumulator calculateDataPerDay(List<Size> sizes,
                                                            double availablePieces,
                                                            Double quality,
                                                            ProjectedBiomassDTO projectedBiomassDTO
    ) {
        var sizesPremium = sizes.stream()
                .filter(size -> Objects.equals(size.getPieceType(), "Piezas Premium"))
                .sorted(Comparator.comparing(Size::getInitialRange));

        var noPremiums = new HashMap<String, List<Size>>();

       sizes.stream()
                .filter(size -> !Objects.equals(size.getPieceType(), "Piezas Premium"))
                .sorted(Comparator.comparing(Size::getInitialRange))
                .forEach(size ->{

                    noPremiums.put(size.getPieceType(),
                            noPremiums.getOrDefault(size.getPieceType(), new ArrayList<>()));
                    noPremiums.get(size.getPieceType())
                            .add(size);
                });


        var resultsPieces = calculateAllSizes(
                sizesPremium, projectedBiomassDTO, new ProjectedBiomassAccumulator(availablePieces, new ArrayList<>()), quality
        );

        var qualityNoPremiums = BigDecimal.ONE.subtract(BigDecimal.valueOf(quality))
                .divide(BigDecimal.valueOf(noPremiums.keySet().size()), 10, RoundingMode.HALF_EVEN)
                .doubleValue();
        noPremiums.keySet()
                .forEach(noPremiunName -> {
                    var results = calculateAllSizes(
                            noPremiums.get(noPremiunName)
                                    .stream(),
                            projectedBiomassDTO,
                            new ProjectedBiomassAccumulator(availablePieces, new ArrayList<>()),
                            qualityNoPremiums
                    );
                    resultsPieces.getProjectedBiomasses().addAll(results.getProjectedBiomasses());
                });

        return resultsPieces;

    }


    private ProjectedBiomassAccumulator calculateAllSizes(Stream<Size> sizes,
                                                          ProjectedBiomassDTO projectedBiomassDTO,
                                                          ProjectedBiomassAccumulator accumulator,
                                                          double quality) {
        return sizes
                .reduce(
                        accumulator,
                        calculateDataBySize(projectedBiomassDTO, quality),
                        (acc, cur) -> {
                            acc.setAvailablePieces(
                                    acc.getAvailablePieces() - cur.getAvailablePieces()
                            );
                            return acc;
                        }
                );
    }

    private BiFunction<ProjectedBiomassAccumulator, Size, ProjectedBiomassAccumulator> calculateDataBySize(ProjectedBiomassDTO projectedBiomassDTO,
                                                                                                           double quaity) {
        return (acc, size) -> {
            long pieces = calculetaDistNorm(
                    size.getFinalRange().doubleValue(),
                    projectedBiomassDTO.getAvgWeight().doubleValue(),
                    acc.getAvailablePieces(),
                    projectedBiomassDTO.getStandardDeviation(),
                    quaity,
                    projectedBiomassDTO.getTotalNumber()
            );

            BigDecimal avgSize = calculateAverage(List.of(size.getInitialRange(), size.getFinalRange()));
            BigDecimal kilosWFEScenario = avgSize.multiply(BigDecimal.valueOf(pieces));

            var pb = ProjectedBiomass
                    .builder()
                    .sizeId(size.getId())
                    .projectionDate(projectedBiomassDTO.getDay())
                    .pieces(pieces)
                    .avgWeight(projectedBiomassDTO.getAvgWeight())
                    .kilosWFE(projectedBiomassDTO.getKilosWFE())
                    .kilosWFEScenario(kilosWFEScenario)
                    .quality(BigDecimal.valueOf(quaity))
                    .build();
            acc.getProjectedBiomasses().add(pb);
            acc.setAvailablePieces(acc.getAvailablePieces() - pieces);
            return acc;
        };
    }


    private long calculetaDistNorm(
            Double size,
            Double weight,
            Double number,
            Double factorStandardDeviation,
            Double quality,
            Double totalNumber
    ) {

        if (validateData(
                weight,
                number,
                quality
        )) {
            return 0;
        }

        double stdDev = factorStandardDeviation * weight;
        NormalDistribution normalDistribution = new NormalDistribution(weight, stdDev);
        double probabilidadAcumulada = normalDistribution.cumulativeProbability(size);
        double pieces = quality * probabilidadAcumulada * totalNumber;
        pieces = pieces - (totalNumber-number);
        return Math.round(pieces);
    }


    private boolean validateData(
            Double weight,
            Double number,
            Double quality
    ) {
        return (Objects.equals(weight, 0d)
                || Objects.equals(number, 0d)
                || Objects.equals(quality, 0d)
        );
    }


}
