package cl.camanchaca.biomass.adapter.bigquery.biomass;

import cl.camanchaca.biomass.mappers.bigquery.ProjectedBQBiomassBQMapper;
import cl.camanchaca.business.repositories.bigquery.ProjectedBiomasBQRepository;
import cl.camanchaca.domain.models.biomass.ProjectedBiomassScenario;
import cl.camanchaca.domain.models.biomass.ProyectadaMerkatus;
import com.google.cloud.bigquery.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProjectedBiomassBQDataAdapter implements ProjectedBiomasBQRepository {

    private final BigQuery bigQuery;
    @Override
    public Flux<ProjectedBiomassScenario> getAllByScenario(LocalDate initialDate, LocalDate finalDate, String scenario, String especie, Integer size, Integer offset) throws InterruptedException {
        return getProyectadaMerkatusFlux(initialDate, finalDate,  size,  offset)
                .filter(o -> o.getScenario().equalsIgnoreCase(scenario) && o.getEspecie().equalsIgnoreCase(especie))
                .map(ProjectedBQBiomassBQMapper::toProjectedBiomassScenario);
    }
    @Override
    public Flux<ProjectedBiomassScenario> getAll(LocalDate initialDate, LocalDate finalDate, Integer size, Integer offset) throws InterruptedException {
        return getProyectadaMerkatusFlux(initialDate, finalDate,  size,  offset).map(ProjectedBQBiomassBQMapper::toProjectedBiomassScenario);
    }

    @Override
    public Flux<String> getAllScenarios(LocalDate initialDate, LocalDate finalDate, Integer size, Integer offset) throws InterruptedException {
        return getProyectadaMerkatusFlux(initialDate, finalDate,  size,  offset)
                .distinct(ProyectadaMerkatus::getScenario)
                .map(ProyectadaMerkatus::getScenario);
    }

    private Flux<ProyectadaMerkatus> getProyectadaMerkatusFlux(LocalDate initialDate, LocalDate finalDate, Integer size, Integer offset) throws InterruptedException {
        Map<Integer, List<String>> yearToMonthsMap = new HashMap<>();
        LocalDate currentDate = initialDate;

        while (!currentDate.isAfter(finalDate)) {
            yearToMonthsMap.computeIfAbsent(currentDate.getYear(), k -> new ArrayList<>())
                    .add(currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            currentDate = currentDate.plusMonths(1);
        }

        StringBuilder whereClause = new StringBuilder("WHERE ");
        for (Map.Entry<Integer, List<String>> entry : yearToMonthsMap.entrySet()) {
            whereClause.append(String.format("  (Year = '%d' AND Month IN (%s))   OR ",
                    entry.getKey(),
                    entry.getValue().stream().map(month -> "'" + month + "'").collect(Collectors.joining(", "))));
        }

        whereClause.setLength(whereClause.length() - 6);

        String sqlQuery = String.format("SELECT * " +
                "FROM `datalikecorp.OptimusRMP.BiomasaProyectadaMerkatus` " +
                "%s", whereClause.toString());

        log.info(sqlQuery);

        QueryJobConfiguration querySqlConfig = QueryJobConfiguration.newBuilder(sqlQuery).build();

        return runFluxQuery(querySqlConfig);
    }



    private Flux<ProyectadaMerkatus> runFluxQuery(QueryJobConfiguration querySqlConfig) throws InterruptedException {
        TableResult result = bigQuery.query(querySqlConfig);
        return Flux.create(fluxSink -> {
            for (FieldValueList row : result.getValues()) {
                fluxSink.next(ProjectedBQBiomassBQMapper.toProyectadaMerkatus(row));
            }
            fluxSink.complete();
        });
    }
}
