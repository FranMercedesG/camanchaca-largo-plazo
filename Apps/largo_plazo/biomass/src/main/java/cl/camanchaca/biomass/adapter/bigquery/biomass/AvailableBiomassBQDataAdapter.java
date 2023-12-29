package cl.camanchaca.biomass.adapter.bigquery.biomass;

import cl.camanchaca.biomass.mappers.bigquery.AvailableBiomassBQMapper;
import cl.camanchaca.business.repositories.bigquery.AvailableBiomassBQRepository;
import cl.camanchaca.domain.models.biomass.AvailableBiomass;
import com.google.cloud.bigquery.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class AvailableBiomassBQDataAdapter implements AvailableBiomassBQRepository {

    private final BigQuery bigQuery;

    @Override
    public Flux<AvailableBiomass> getAll() throws InterruptedException {
        String sqlQuery = "SELECT * FROM `datalikecorp.OptimusRMP.BiomasaDisponibleSalmonesTome`";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .build();
        return runFluxQuery(querySqlConfig);
    }

    @Override
    public Flux<AvailableBiomass> getByDateAndPage(LocalDate date, Integer size, Integer offset) throws InterruptedException {
        String sqlQuery = "SELECT * FROM `datalikecorp.OptimusRMP.BiomasaDisponibleSalmonesTome` " +
                "WHERE DATE(PARSE_TIMESTAMP('%Y-%m-%d %H:%M:%E*S', Fecpro)) =  @date " +
                "LIMIT @size OFFSET @offset";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .addNamedParameter("date", QueryParameterValue.date(date.toString()))
                .addNamedParameter("size", QueryParameterValue.int64(size))
                .addNamedParameter("offset", QueryParameterValue.int64(offset))
                .build();
        return runFluxQuery(querySqlConfig);
    }

    @Override
    public Flux<AvailableBiomass> getByDate(LocalDate date) throws InterruptedException {
        String sqlQuery = "SELECT * FROM `datalikecorp.OptimusRMP.BiomasaDisponibleSalmonesTome` " +
                "WHERE DATE(PARSE_TIMESTAMP('%Y-%m-%d %H:%M:%E*S', Fecpro)) =  @date ";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .addNamedParameter("date", QueryParameterValue.date(date.toString()))
                .build();
        return runFluxQuery(querySqlConfig);
    }

    @Override
    public Flux<AvailableBiomass> getBetweenDateAndPage(LocalDate since, LocalDate until, Integer size, Integer offset) throws InterruptedException {
        String sqlQuery = "SELECT * FROM `datalikecorp.OptimusRMP.BiomasaDisponibleSalmonesTome` " +
                "WHERE DATE(PARSE_TIMESTAMP('%Y-%m-%d %H:%M:%E*S', Fecpro)) BETWEEN @since AND @until " +
                "LIMIT @size OFFSET @offset";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .addNamedParameter("since", QueryParameterValue.date(since.toString()))
                .addNamedParameter("until", QueryParameterValue.date(until.toString()))
                .addNamedParameter("size", QueryParameterValue.int64(size))
                .addNamedParameter("offset", QueryParameterValue.int64(offset))
                .build();
        return runFluxQuery(querySqlConfig);
    }


    @Override
    public Mono<Long> count() throws InterruptedException {
        String sqlQuery = "SELECT COUNT(*) FROM `datalikecorp.OptimusRMP.BiomasaDisponibleSalmonesTome`";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .build();
        return runMonoQuery(querySqlConfig);
    }

    @Override
    public Mono<Long> countByDate(LocalDate date) throws InterruptedException {
        String sqlQuery = "SELECT COUNT(*) FROM `datalikecorp.OptimusRMP.BiomasaDisponibleSalmonesTome` " +
                "WHERE DATE(PARSE_TIMESTAMP('%Y-%m-%d %H:%M:%E*S', Fecpro)) =  @date ";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .addNamedParameter("date", QueryParameterValue.date(date.toString()))
                .build();
        return runMonoQuery(querySqlConfig);
    }

    @Override
    public Mono<Long> countBetweemDate(LocalDate since, LocalDate until) throws InterruptedException {
        String sqlQuery = "SELECT COUNT(*) FROM `datalikecorp.OptimusRMP.BiomasaDisponibleSalmonesTome` " +
                "WHERE DATE(PARSE_TIMESTAMP('%Y-%m-%d %H:%M:%E*S', Fecpro)) BETWEEN @since AND @until";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .addNamedParameter("since", QueryParameterValue.date(since.toString()))
                .addNamedParameter("until", QueryParameterValue.date(until.toString()))
                .build();
        return runMonoQuery(querySqlConfig);
    }

    private Flux<AvailableBiomass> runFluxQuery(QueryJobConfiguration querySqlConfig) throws InterruptedException {
        TableResult result = bigQuery.query(querySqlConfig);
        return Flux.create(fluxSink -> {
            for (FieldValueList row : result.getValues()) {
                fluxSink.next(AvailableBiomassBQMapper.toAvailableBiomass(row));
            }
            fluxSink.complete();
        });
    }

    private Mono<Long> runMonoQuery(QueryJobConfiguration querySqlConfig) throws InterruptedException {
        TableResult result = bigQuery.query(querySqlConfig);
        return Mono.create(monoSink -> {
            for (FieldValueList row : result.getValues()) {
                monoSink.success(row.get(0).getLongValue());
            }
        });
    }

}
