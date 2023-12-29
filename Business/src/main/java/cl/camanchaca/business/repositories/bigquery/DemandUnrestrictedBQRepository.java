package cl.camanchaca.business.repositories.bigquery;

import cl.camanchaca.domain.models.demand.Freight;
import reactor.core.publisher.Flux;

public interface DemandUnrestrictedBQRepository {

    Flux<Freight> getFreightsByPortAndIncoterms(String port, String incoterms) throws InterruptedException;
}
