package cl.camanchaca.orders.adapters.postgres.demand;

import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import cl.camanchaca.orders.mappers.UnrestrictedDemandMapper;
import cl.camanchaca.orders.utils.OfficeEnum;
import cl.camanchaca.orders.utils.SalesStatusEnum;
import cl.camanchaca.orders.utils.SalesTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnrestrictedDataAdapter implements UnrestrictedDemandRepository {

    private final UnrestrictedDataRepository unrestrictedDataRepository;

    private final DatabaseClient databaseClient;


    @Override
    public Flux<UnrestrictedDemand> getAll() {
        return unrestrictedDataRepository.findAll().map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }


    @Override
    public Flux<UnrestrictedDemand> getAllByPeriods(LocalDate initialDate, LocalDate finalDate) {
        return unrestrictedDataRepository.findAllByDateRange(initialDate, finalDate).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }


    @Override
    public Flux<UnrestrictedDemand> getAllByPageAndSize(Integer page, Integer size) {
        return unrestrictedDataRepository.findAllByOffsetAndSize(page, size).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }

    @Override
    public Flux<UnrestrictedDemand> getAllByPeriodAndPageAndSize(Integer page, Integer size, LocalDate date) {
        return unrestrictedDataRepository.findAllByPeriodAndOffsetAndSize(date, page, size).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }

    @Override
    public Flux<UnrestrictedDemand> getAllBetweenDatesAndPageAndSize(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate) {
        return unrestrictedDataRepository.findAllByDateRangeAndOffsetAndSize(initialDate, finalDate, page, size).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }

    @Override
    public Flux<UnrestrictedDemand> getAllBetweenDatesAndPageAndSizeAndFilters(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate, String specie, String family) {
        return unrestrictedDataRepository.findAllByDateRangeAndOffsetAndSizeAndFilters(initialDate, finalDate, page, size, specie, family).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }

    @Override
    public Flux<UnrestrictedDemand> getAllOfficeBetweenDatesAndPageAndSizeAndFilters(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate, String specie, String family, String office) {
        return unrestrictedDataRepository.findAllOfficeByDateRangeAndOffsetAndSizeAndFilters(initialDate, finalDate, page, size, specie, family, office).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }

    @Override
    public Flux<UnrestrictedDemand> getAllOfficeBetweenDatesAndFilters(LocalDate initialDate, LocalDate finalDate, String specie, String family, String office) {
        return unrestrictedDataRepository.findAllOfficeByDateRangeAndFilters(initialDate, finalDate, specie, family, office).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }

    @Override
    public Flux<UnrestrictedDemand> getAllBetweenDatesByOfficeAndPageAndSize(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate, String office) {
        return unrestrictedDataRepository.findAllByDateRangeOfficeAndOffsetAndSize(initialDate, finalDate, page, size, office).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }

    @Override
    public Flux<UnrestrictedDemand> getAllBetweenDatesByOffice(LocalDate initialDate, LocalDate finalDate, String office) {
        return unrestrictedDataRepository.findAllByDateRangeOffice(initialDate, finalDate, office).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }


    @Override
    public Flux<UnrestrictedDemand> saveAll(Flux<UnrestrictedDemand> list) {
        return unrestrictedDataRepository.saveAll(list.map(UnrestrictedDemandMapper::toUnrestrictedDemandData)).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }

    @Override
    public Mono<Void> deleteAllByUser(String user) {
        return unrestrictedDataRepository.deleteAllByUsuario(user);
    }

    @Override
    public Mono<Void> deleteAllByUserAndOrigen(String user, String origen) {
        return unrestrictedDataRepository.deleteAllByUsuarioAndOrigen(user, origen);
    }

    @Override
    public Flux<String> getAllSalesStatus() {
        return Flux.fromArray(SalesStatusEnum.values())
                .map(SalesStatusEnum::getValue);
    }

    @Override
    public Flux<String> getOffice() {
        return Flux.fromArray(OfficeEnum.values())
                .map(salesStatusEnum -> salesStatusEnum.name());
    }

    @Override
    public Flux<String> getSalesType() {
        return Flux.fromArray(SalesTypeEnum.values())
                .map(salesStatusEnum -> salesStatusEnum.name());
    }

    @Override
    public Mono<Long> count() {
        return unrestrictedDataRepository.count();
    }

    @Override
    public Mono<Long> countOffice(LocalDate startDate, LocalDate endDate, String office, String especie, String family) {
        StringBuilder baseQuery = new StringBuilder("SELECT COUNT(*) FROM unrestricted_demand WHERE oficina = :office and periodo BETWEEN :startDate AND :endDate");

        Map<String, Object> params = new HashMap<>();
        params.put("office", office);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        if (family != null && !family.isEmpty()) {
            baseQuery.append(" AND family = :family");
            params.put("family", family);
        }

        if (especie != null && !especie.isEmpty()) {
            baseQuery.append(" AND especie = :especie");
            params.put("especie", especie);
        }

        DatabaseClient.GenericExecuteSpec querySpec = databaseClient.sql(baseQuery.toString());

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySpec = querySpec.bind(entry.getKey(), entry.getValue());
        }

        return querySpec
                .map(row -> row.get(0, Long.class))
                .one();
    }

    @Override
    public Mono<Long> countFilters(LocalDate startDate, LocalDate endDate, String especie, String family) {

        StringBuilder baseQuery = new StringBuilder("SELECT COUNT(*) FROM unrestricted_demand WHERE periodo BETWEEN :startDate AND :endDate");

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        if (family != null && !family.isEmpty()) {
            baseQuery.append(" AND family = :family");
            params.put("family", family);
        }

        if (especie != null && !especie.isEmpty()) {
            baseQuery.append(" AND especie = :especie");
            params.put("especie", especie);
        }

        DatabaseClient.GenericExecuteSpec querySpec = databaseClient.sql(baseQuery.toString());

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySpec = querySpec.bind(entry.getKey(), entry.getValue());
        }

        return querySpec
                .map(row -> row.get(0, Long.class))
                .one();
    }


}
