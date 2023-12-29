package cl.camanchaca.parametrization.adapter.bigquery.product;

import cl.camanchaca.business.repositories.BigqueryCSTDRepository;
import cl.camanchaca.domain.models.Product;
import cl.camanchaca.parametrization.mappers.bigquery.ProductBQMapper;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class ProductBigqueryAdapter implements BigqueryCSTDRepository {

    private final BigQuery bigQuery;

    @Override
    public Mono<Long> count() throws InterruptedException {
        String sqlQuery = "SELECT COUNT(*) as total FROM `datalikecorp.OptimusRMP.CostoEstandarSalmones`";
        AtomicReference<Long> total = new AtomicReference<>(0l);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(sqlQuery).build();
        bigQuery.query(queryConfig)
                .getValues()
                .forEach(fieldValues -> {
                    total.set(fieldValues
                            .get(0)
                            .getLongValue());
                });
        return Mono.just(total.get());
    }

    @Override
    public Flux<Product> getAll() throws InterruptedException {
        String sqlQuery = "SELECT * FROM `datalikecorp.OptimusRMP.CostoEstandarSalmones`";

        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .build();

        TableResult result = bigQuery.query(querySqlConfig);

       return Flux.create(fluxSink -> {
            for (FieldValueList row : result.getValues()) {
                fluxSink.next(ProductBQMapper.toProduct(row));
            }
            fluxSink.complete();
        }) ;

    }
}
