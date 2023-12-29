package cl.camanchaca.business.repositories;

import cl.camanchaca.business.responses.ProjectedBiomassResponse;
import reactor.core.publisher.Mono;

import java.util.List;


public interface ExcelProjectedBiomassRepository {

    Mono<byte[]> generatedBytesOfExcel(List<ProjectedBiomassResponse> projectedBiomasses);

}
