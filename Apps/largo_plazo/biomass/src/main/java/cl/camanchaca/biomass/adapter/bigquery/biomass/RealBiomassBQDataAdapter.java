package cl.camanchaca.biomass.adapter.bigquery.biomass;

import cl.camanchaca.biomass.mappers.bigquery.RealBiomassBQMapper;
import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.repositories.bigquery.RealBiomassBQRepository;
import cl.camanchaca.domain.models.biomass.RealBiomass;
import com.google.cloud.bigquery.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class RealBiomassBQDataAdapter implements RealBiomassBQRepository {

    private final BigQuery bigQuery;

    @Override
    public Flux<RealBiomass> getAll() throws InterruptedException {
        String sqlQuery = "SELECT * FROM `datalikecorp.OptimusRMP.BiomasaRealCosechada` LIMIT 200";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .build();
        return runFluxQuery(querySqlConfig);
    }

    @Override
    public Flux<RealBiomass> getByDateAndPage(LocalDate date, Integer size, Integer offset) throws InterruptedException {
        String sqlQuery = "SELECT * FROM `datalikecorp.OptimusRMP.BiomasaRealCosechada` " +
                "WHERE DATE(FechaRegistro) =  @date " +
                "LIMIT @size OFFSET @offset";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .addNamedParameter("date", QueryParameterValue.date(date.toString()))
                .addNamedParameter("size", QueryParameterValue.int64(size))
                .addNamedParameter("offset", QueryParameterValue.int64(offset))
                .build();
        return runFluxQuery(querySqlConfig);
    }

    @Override
    public Flux<RealBiomass> getByDate(LocalDate date) {
        String sqlQuery = "SELECT * FROM `datalikecorp.OptimusRMP.BiomasaRealCosechada` " +
                "WHERE DATE(FechaRegistro) =  @date ";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .addNamedParameter("date", QueryParameterValue.date(date.toString()))
                .build();

        try {
            return runFluxQuery(querySqlConfig);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
           return Flux.error(new BusinessError("Error con los datos de bigquery"));
        }
    }

    @Override
    public Mono<Long> count() throws InterruptedException {
        String sqlQuery = "SELECT COUNT(*) FROM `datalikecorp.OptimusRMP.BiomasaRealCosechada`";
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .build();
        return runMonoQuery(querySqlConfig);
    }

    @Override
    public Mono<Long> countByDate(LocalDate date) throws InterruptedException {
        String sqlQuery = "SELECT COUNT(*) FROM `datalikecorp.OptimusRMP.BiomasaRealCosechada` " +
                "WHERE DATE(PARSE_TIMESTAMP('%Y-%m-%d %H:%M:%E*S', FechaRegistro)) =  @date " ;
        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery)
                .addNamedParameter("date", QueryParameterValue.date(date.toString()))
                .build();
        return runMonoQuery(querySqlConfig);
    }

    private Flux<RealBiomass> runFluxQuery(QueryJobConfiguration querySqlConfig) throws InterruptedException {
        TableResult result = bigQuery.query(querySqlConfig);

        return Flux.create(fluxSink -> {
            for (FieldValueList row : result.getValues()) {
                fluxSink.next(RealBiomassBQMapper.toRealBiomass(row));
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
