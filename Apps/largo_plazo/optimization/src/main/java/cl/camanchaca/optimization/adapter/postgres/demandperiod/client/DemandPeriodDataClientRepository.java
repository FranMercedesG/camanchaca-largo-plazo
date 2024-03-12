package cl.camanchaca.optimization.adapter.postgres.demandperiod.client;

import cl.camanchaca.domain.models.optimization.DemandPeriod;
import cl.camanchaca.domain.models.optimization.Period;
import cl.camanchaca.optimization.adapter.postgres.demandperiod.DemandPeriodDto;
import lombok.AllArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class DemandPeriodDataClientRepository implements DemandPeriodDataDBClientRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<DemandPeriod> getAllByPageAndSize(LocalDate startDate, LocalDate endDate, Integer page, Integer size, String dv) {
        return fetchDemandPeriodData(startDate, endDate, page, size, dv)
                .transform(this::transformToDemandPeriod);
    }

    @Override
    public Mono<Long> countDemandPeriodData(LocalDate startDate, LocalDate endDate, String dv) {
        String sql = "SELECT COUNT(*) FROM unrestricted_demand ud WHERE ud.periodo BETWEEN :startDate AND :endDate AND (:dv = '' OR ud.dv = :dv)";

        return databaseClient.sql(sql)
                .bind("startDate", startDate)
                .bind("endDate", endDate)
                .bind("dv", dv)
                .map((row, metadata) -> row.get(0, Long.class))
                .first();

    }

    @Override
    public Flux<DemandPeriod> getAllBetweenDate(LocalDate startDate, LocalDate endDate) {
        return fetchDemandPeriodData(startDate, endDate)
                .transform(this::transformToDemandPeriod);
    }


    @Override
    public Flux<DemandPeriod> getAll() {
        return getAllByDemandPeriod().transform(this::transformToDemandPeriod);
    }

    public Flux<DemandPeriodDto> fetchDemandPeriodData(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT"
                + "    dp.demand_period_id,"
                + "    dp.period,"
                + "    ud.unrestricted_demand_id AS demand_id,"
                + "    dp.status,"
                + "    ud.products_id as codigo,"
                + "    ud.oficina,"
                + "    ud.family AS familia,"
                + "    ud.pais_destino AS pais,"
                + "    ud.dv"
                + " FROM unrestricted_demand ud"
                + " LEFT JOIN demand_period dp ON ud.unrestricted_demand_id = dp.demand_id"
                + " WHERE dp.period BETWEEN :startDate AND :endDate"
                + " OR ud.periodo BETWEEN :startDate AND :endDate"
                + " ORDER BY ud.unrestricted_demand_id, dp.period";

        return databaseClient.sql(sql)
                .bind("startDate", startDate)
                .bind("endDate", endDate)
                .map((row, metadata) -> {
                    UUID demandPeriodId = row.get("demand_period_id", UUID.class);
                    LocalDate period = row.get("period", LocalDate.class);
                    UUID demandId = row.get("demand_id", UUID.class);
                    Boolean status = row.get("status", Boolean.class);
                    String codigo = row.get("codigo", String.class);
                    String oficina = row.get("oficina", String.class);
                    String familia = row.get("familia", String.class);
                    String pais = row.get("pais", String.class);
                    String dv = row.get("dv", String.class);
                    return new DemandPeriodDto(demandPeriodId, period, demandId, status, codigo, oficina, familia, pais, dv);
                })
                .all();
    }



    public Flux<DemandPeriodDto> fetchDemandPeriodData(LocalDate startDate, LocalDate endDate, Integer page, Integer size, String dv) {
        String sql = "WITH DemandWithPeriodStatus AS ("
                + "    SELECT"
                + "        ud.unrestricted_demand_id,"
                + "        ud.products_id as codigo,"
                + "        ud.oficina,"
                + "        ud.family AS familia,"
                + "        ud.pais_destino AS pais,"
                + "        ud.dv,"
                + "        MAX(CASE WHEN dp.period BETWEEN :startDate AND :endDate THEN 1 ELSE 0 END) AS HasPeriod"
                + "    FROM unrestricted_demand ud"
                + "    LEFT JOIN demand_period dp ON ud.unrestricted_demand_id = dp.demand_id"
                + "    WHERE ud.periodo BETWEEN :startDate AND :endDate AND (:dv = '' OR ud.dv = :dv)"
                + "    GROUP BY ud.unrestricted_demand_id, ud.codigo, ud.oficina, ud.family, ud.pais_destino, ud.dv),"
                + "PaginatedDemand AS ("
                + "    SELECT *,"
                + "    ROW_NUMBER() OVER (ORDER BY HasPeriod DESC, unrestricted_demand_id) AS RowNum"
                + "    FROM DemandWithPeriodStatus)"
                + "SELECT"
                + "    dp.demand_period_id,"
                + "    dp.period,"
                + "    pd.unrestricted_demand_id AS demand_id,"
                + "    dp.status,"
                + "    pd.codigo,"
                + "    pd.oficina,"
                + "    pd.familia,"
                + "    pd.pais,"
                + "    pd.dv"
                + " FROM PaginatedDemand pd"
                + " LEFT JOIN demand_period dp ON pd.unrestricted_demand_id = dp.demand_id AND dp.period BETWEEN :startDate AND :endDate"
                + " WHERE pd.RowNum BETWEEN :offset AND :limit"
                + " ORDER BY pd.HasPeriod DESC, pd.unrestricted_demand_id, dp.period";

        int offset = (page - 1) * size + 1;
        int limit = page * size;

        return databaseClient.sql(sql)
                .bind("startDate", startDate)
                .bind("endDate", endDate)
                .bind("dv", dv)
                .bind("offset", offset)
                .bind("limit", limit)
                .map((row, metadata) -> {
                    UUID demandPeriodId = row.get("demand_period_id", UUID.class);
                    LocalDate period = row.get("period", LocalDate.class);
                    UUID demandId = row.get("demand_id", UUID.class);
                    Boolean status = row.get("status", Boolean.class);
                    String codigo = row.get("codigo", String.class);
                    String oficina = row.get("oficina", String.class);
                    String familia = row.get("familia", String.class);
                    String pais = row.get("pais", String.class);
                    String DocV = row.get("dv", String.class);
                    return new DemandPeriodDto(demandPeriodId, period, demandId, status, codigo, oficina, familia, pais, DocV);
                })
                .all();
    }


    public Flux<DemandPeriodDto> getAllByDemandPeriod() {
        String sql = "WITH DemandWithPeriodStatus AS ("
                + "    SELECT"
                + "        ud.unrestricted_demand_id,"
                + "        ud.products_id as codigo,"
                + "        ud.oficina,"
                + "        ud.family AS familia,"
                + "        ud.pais_destino AS pais,"
                + "        ud.dv,"
                + "        CASE WHEN COUNT(dp.demand_id) > 0 THEN 1 ELSE 0 END AS HasPeriod"
                + "    FROM unrestricted_demand ud"
                + "    LEFT JOIN demand_period dp ON ud.unrestricted_demand_id = dp.demand_id"
                + "    GROUP BY ud.unrestricted_demand_id, ud.codigo, ud.oficina, ud.family, ud.pais_destino, ud.dv)"
                + "SELECT"
                + "    dp.demand_period_id,"
                + "    dp.period,"
                + "    pd.unrestricted_demand_id AS demand_id,"
                + "    dp.status,"
                + "    pd.codigo,"
                + "    pd.oficina,"
                + "    pd.familia,"
                + "    pd.pais,"
                + "    pd.dv"
                + " FROM DemandWithPeriodStatus pd"
                + " LEFT JOIN demand_period dp ON pd.unrestricted_demand_id = dp.demand_id"
                + " ORDER BY pd.HasPeriod DESC, pd.unrestricted_demand_id, dp.period";

        return databaseClient.sql(sql)
                .map((row, metadata) -> {
                    UUID demandPeriodId = row.get("demand_period_id", UUID.class);
                    LocalDate period = row.get("period", LocalDate.class);
                    UUID demandId = row.get("demand_id", UUID.class);
                    Boolean status = row.get("status", Boolean.class);
                    String codigo = row.get("codigo", String.class);
                    String oficina = row.get("oficina", String.class);
                    String familia = row.get("familia", String.class);
                    String pais = row.get("pais", String.class);
                    String dv = row.get("dv", String.class);
                    return new DemandPeriodDto(demandPeriodId, period, demandId, status, codigo, oficina, familia, pais, dv);
                })
                .all();
    }


    private Flux<DemandPeriod> transformToDemandPeriod(Flux<DemandPeriodDto> demandPeriodDtoFlux) {
        return demandPeriodDtoFlux
                .collectMultimap(DemandPeriodDto::getDemandId)
                .flatMapMany(map -> Flux.fromIterable(map.entrySet()))
                .map(entry -> {
                    DemandPeriodDto first = entry.getValue().iterator().next();

                    List<Period> periods = entry.getValue().stream()
                            .filter(dpd -> dpd.getDemandPeriodId() != null)
                            .map(dpd -> Period.builder()
                                    .demandPeriodId(dpd.getDemandPeriodId())
                                    .period(dpd.getPeriod())
                                    .status(dpd.getStatus())
                                    .build())
                            .collect(Collectors.toList());

                    return DemandPeriod.builder()
                            .unrestrictedDemandId(first.getDemandId())
                            .codigo(first.getCodigo())
                            .oficina(first.getOficina())
                            .familia(first.getFamilia())
                            .pais(first.getPais())
                            .dv(first.getDv())
                            .periods(periods)
                            .build();
                });
    }

}
