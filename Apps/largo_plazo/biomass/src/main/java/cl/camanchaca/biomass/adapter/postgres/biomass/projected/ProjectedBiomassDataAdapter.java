package cl.camanchaca.biomass.adapter.postgres.biomass.projected;

import cl.camanchaca.biomass.mappers.postgres.ProjectedBiomassMapper;
import cl.camanchaca.business.repositories.biomass.ProjectedBiomassRepository;
import cl.camanchaca.domain.models.biomass.ProjectedBiomass;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectedBiomassDataAdapter implements ProjectedBiomassRepository {

    private final ProjectedBiomassDataRepository projectedBiomassDataRepository;

    @Override
    public Mono<ProjectedBiomass> save(ProjectedBiomass projectedBiomass) {
        return projectedBiomassDataRepository.save(
                ProjectedBiomassMapper.toProjectedBiomassData(projectedBiomass)
        )
                .map(ProjectedBiomassMapper::toProjectedBiomass);
    }

    @Override
    public Flux<ProjectedBiomass> getBySizeId(UUID sizeId) {
        return projectedBiomassDataRepository.findAllBySizeId(sizeId)
                .map(ProjectedBiomassMapper::toProjectedBiomass);
    }

    @Override
    public Mono<Void> deleteAllBySizeId(UUID sizeId){
        return projectedBiomassDataRepository.deleteAllBySizeId(sizeId);
    }


}
