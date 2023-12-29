package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.business.repositories.biomass.ProjectedBiomassRepository;
import cl.camanchaca.business.responses.ProjectedBiomassResponse;
import cl.camanchaca.business.utils.SizeUtils;
import cl.camanchaca.domain.models.biomass.PieceByDay;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;


@AllArgsConstructor
public class GetProjectBiomassBySpecieUseCase extends Usecase<String, Flux<ProjectedBiomassResponse>> {

    private final ProjectedBiomassRepository projectedBiomassRepository;
    private final SizeRepository sizeRepository;

    @Override
    public Flux<ProjectedBiomassResponse> apply(String specie) {
       return sizeRepository.getBySpecie(specie.toUpperCase())
                .flatMap(size -> projectedBiomassRepository
                        .getBySizeId(size.getId())
                        .collectList()
                        .map(projectedBiomasses -> ProjectedBiomassResponse
                                .builder()
                                .quality(size.getPieceType())
                                .size(size.nameColunm())
                                .days(projectedBiomasses.stream()
                                        .map(biomass ->
                                                PieceByDay.builder()
                                                        .id(biomass.getId())
                                                        .period(biomass.getProjectionDate())
                                                        .piecesBySizeValue(biomass.getPieces())
                                                        .kilosWFEValue(biomass.getKilosWFEScenario())
                                                        .build()
                                        )
                                        .collect(Collectors.toList()))
                                .build())

                )
               .sort(SizeUtils.sortProjected());

    }
}
