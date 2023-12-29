package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class SaveUnrestrictedDemandAdminUseCase {

    public static final String PLATAFORMA = "plataforma";
    public static final String BIGQUERY = "bigquery";

    private final UnrestrictedDemandRepository unrestrictedDemandRepository;

    public Flux<UnrestrictedDemand> apply(List<UnrestrictedDemand> unrestrictedDemands, Map<String, String> header, String source) {

        validations(unrestrictedDemands, source, header.get("user"));

        return unrestrictedDemandRepository.deleteAllByUserAndOrigen(header.get("user"), source.toLowerCase())
                .thenMany(unrestrictedDemandRepository.saveAll(Flux.fromIterable(unrestrictedDemands)));


    }

    private static void validations(List<UnrestrictedDemand> unrestrictedDemands, String source, String user) {
        if(source.equalsIgnoreCase(PLATAFORMA)){
            unrestrictedDemands.forEach(unrestrictedDemand -> {
                unrestrictedDemand.setOrigen(PLATAFORMA);
                unrestrictedDemand.setUsuario(user);
            } );
        }

        if(source.equalsIgnoreCase(BIGQUERY)){
            unrestrictedDemands.forEach(unrestrictedDemand -> {
                unrestrictedDemand.setOrigen(BIGQUERY);
                unrestrictedDemand.setUsuario(user);
            } );
        }
    }
}
