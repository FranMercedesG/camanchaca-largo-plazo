package cl.camanchaca.orders.adapters.bigquery;

import cl.camanchaca.business.repositories.bigquery.OrderBQRepository;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Service
@AllArgsConstructor
public class OrdersBQAdapter implements OrderBQRepository {

    private final BigQuery bigQuery;

    @Override
    public Flux<String> getAllIncoTerms() {
        String sqlQuery = "SELECT DISTINCT(Incoterms) FROM `datalikecorp.OptimusRMP.FleteReferenciaSalmonesMD`";

        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .build();
        return runStringFlux(querySqlConfig);

    }

    @Override
    public Flux<String> getAllDestiantionPort() {
        String sqlQuery = "SELECT DISTINCT(PuertoDestino) FROM `datalikecorp.OptimusRMP.FleteReferenciaSalmonesMD`";

        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .build();
        return runStringFlux(querySqlConfig);
    }

    private Flux<String> runStringFlux(QueryJobConfiguration querySqlConfig) {
        return Flux.create(fluxSink -> {
            try {
                TableResult result = bigQuery.query(querySqlConfig);
                for (FieldValueList row : result.getValues()) {
                    if (!row.get(0).isNull()) {
                        fluxSink.next(row.get(0).getStringValue());
                    }
                }
                fluxSink.complete();
            } catch (InterruptedException e) {
                e.printStackTrace();
                fluxSink.error(e);
            }
        });
    }

}
