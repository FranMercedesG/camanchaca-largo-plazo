package cl.camanchaca.business.usecases.largoplazo.biomass;


import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.BusinessException;
import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.business.responses.ProjectedBiomassResponse;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiFunction;

@AllArgsConstructor
public class ReadProjectedBiomassExcelUseCase implements BiFunction<String, InputStream, Flux<ProjectedBiomassResponse>> {

    private final ExcelRepository<ProjectedBiomassResponse> excelRepository;


    @Override
    public Flux<ProjectedBiomassResponse> apply(String specie, InputStream file) {
        try {
            return excelRepository.readFile(file)
                    .onErrorResume(throwable -> {
                        throwable.printStackTrace();
                        return Mono.error(new BusinessError("Error con los datos del excel"));
                    });
        } catch (IOException e) {
            throw new BusinessException(e.getLocalizedMessage());
        }
    }
}
