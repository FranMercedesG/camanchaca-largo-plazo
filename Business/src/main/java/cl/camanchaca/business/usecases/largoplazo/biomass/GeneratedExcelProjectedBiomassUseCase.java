package cl.camanchaca.business.usecases.largoplazo.biomass;


import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ExcelProjectedBiomassRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GeneratedExcelProjectedBiomassUseCase  extends Usecase<String, Mono<byte[]>> {

    private final ExcelProjectedBiomassRepository excelProjectedBiomassRepository;
    private final GetProjectBiomassBySpecieUseCase projectBiomassBySpecieUseCase;

    @Override
    public Mono<byte[]> apply(String specie) {
        return projectBiomassBySpecieUseCase.apply(specie.toUpperCase())
                .collectList()
                .flatMap(excelProjectedBiomassRepository::generatedBytesOfExcel);
    }
}
