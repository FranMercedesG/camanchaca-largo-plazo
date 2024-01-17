package cl.camanchaca.orders.adapters.bigquery;

import cl.camanchaca.business.repositories.bigquery.SalmonOrdersBQRepository;
import cl.camanchaca.domain.models.Order;
import cl.camanchaca.orders.mappers.bigquery.OrderBQMapper;
import com.google.cloud.bigquery.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class SalmonOrderBQAdapter implements SalmonOrdersBQRepository {

    private final BigQuery bigQuery;

    @Override
    public Flux<Order> getOrdersBetweenDate(LocalDate initialDate, LocalDate finalDate) {
        String sqlQuery = "SELECT * FROM `datalikecorp.OptimusRMP.PedidosSalmonesRMP` " +
                "WHERE  FechaPreferenteEntrega BETWEEN ? AND ? " +
                "AND NombreSector LIKE '%Congelado%'";
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .addPositionalParameter(QueryParameterValue.date(initialDate.toString()))
                .addPositionalParameter(QueryParameterValue.date(finalDate.toString()))
                .build();

        return runFluxQuery(queryConfig);
    }


    private Flux<Order> runFluxQuery(QueryJobConfiguration querySqlConfig) {

        return Flux.create(fluxSink -> {
            try {
                TableResult result = bigQuery.query(querySqlConfig);
                for (FieldValueList row : result.getValues()) {
                    fluxSink.next(OrderBQMapper.toOrder(row));
                }
                fluxSink.complete();
            } catch (InterruptedException e) {
                fluxSink.error(e);
                Thread.currentThread().interrupt();
            }
        });
    }


}
