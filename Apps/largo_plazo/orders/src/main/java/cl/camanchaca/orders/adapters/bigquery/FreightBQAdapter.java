package cl.camanchaca.orders.adapters.bigquery;

import cl.camanchaca.business.repositories.bigquery.DemandUnrestrictedBQRepository;
import cl.camanchaca.domain.models.demand.Freight;
import cl.camanchaca.orders.mappers.BigQueryMapper;
import com.google.cloud.bigquery.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class FreightBQAdapter implements DemandUnrestrictedBQRepository {

    private final BigQuery bigQuery;

    @Override
    public Flux<Freight> getFreightsByPortAndIncoterms(String port, String incoterms) throws InterruptedException {
        return getFreights(incoterms, port);
    }

    private Flux<Freight> getFreights(String incoterms, String puertoDestino) throws InterruptedException {

        String sqlQuery = "SELECT * FROM `datalikecorp.OptimusRMP.FleteReferenciaSalmonesMD` " +
                "WHERE Incoterms = @incoterms AND PuertoDestino = @puertoDestino " +
                "LIMIT 1";


        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .addNamedParameter("incoterms", QueryParameterValue.string(incoterms))
                .addNamedParameter("puertoDestino", QueryParameterValue.string(puertoDestino))
                .build();

        return runFluxQuery(querySqlConfig);
    }


    private Flux<Freight> runFluxQuery(QueryJobConfiguration querySqlConfig) throws InterruptedException {
        TableResult result = bigQuery.query(querySqlConfig);
        return Flux.create(fluxSink -> {
            for (FieldValueList row : result.getValues()) {
                fluxSink.next(BigQueryMapper.toFreight(row));
            }
            fluxSink.complete();
        });
    }
}
