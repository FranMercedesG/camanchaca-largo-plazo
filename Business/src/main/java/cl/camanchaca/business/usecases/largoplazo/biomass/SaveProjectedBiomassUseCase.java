package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.business.repositories.biomass.ProjectedBiomassRepository;
import cl.camanchaca.business.responses.ProjectedBiomassResponse;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.biomass.PieceByDay;
import cl.camanchaca.domain.models.biomass.ProjectedBiomass;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SaveProjectedBiomassUseCase implements BiFunction<Flux<ProjectedBiomassResponse>, String, Flux<ProjectedBiomassResponse>> {
    private final SizeRepository sizeRepository;
    private final ProjectedBiomassRepository projectedBiomassRepository;

    @Override
    public Flux<ProjectedBiomassResponse> apply(Flux<ProjectedBiomassResponse> projectedBiomassResponses, String specie) {

        return projectedBiomassResponses
                .flatMap(projectedBiomassResponse -> {
                    Size sizeIn = Size.ofString(projectedBiomassResponse.getSize());
                    sizeIn.setPieceType(projectedBiomassResponse.getQuality());
                    sizeIn.setSpecies(specie.toUpperCase());

                    return sizeRepository.getByRangesAndUnitAndQualityAndSpecie(sizeIn)
                            .flatMapMany(size ->
                                    projectedBiomassRepository.deleteAllBySizeId(size.getId())
                                            .thenMany(Flux.fromIterable(projectedBiomassResponse.getDays()))
                                            .flatMap(pieceByDay -> projectedBiomassRepository
                                                    .save(ProjectedBiomass.builder()
                                                            .projectionDate(pieceByDay.getDay())
                                                            .pieces(pieceByDay.getValue())
                                                            .sizeId(size.getId())
                                                            .quality(BigDecimal.valueOf(-1))
                                                            .build()))
                                            .collectList()
                                            .map(projectedBiomass -> ProjectedBiomassResponse.builder()
                                                    .quality(size.getPieceType())
                                                    .size(size.nameColunm())
                                                    .days(projectedBiomass.stream()
                                                            .map(biomass ->
                                                                    PieceByDay.builder()
                                                                            .id(biomass.getId())
                                                                            .period(biomass.getProjectionDate())
                                                                            .piecesBySizeValue(biomass.getPieces())
                                                                            .build()
                                                            )
                                                            .collect(Collectors.toList())
                                                    )
                                                    .build())
                            );
                });

    }
}
