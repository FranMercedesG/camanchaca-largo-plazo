package cl.camanchaca.optimization.adapter.postgres.demand;

import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import cl.camanchaca.optimization.mapper.UnrestrictedDemandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UnrestrictedDataAdapter implements UnrestrictedDemandRepository {

    private final UnrestrictedDataRepository unrestrictedDataRepository;

    @Override
    public Flux<UnrestrictedDemand> getAll() {
        return null;
    }

    @Override
    public Flux<UnrestrictedDemand> getAllByPeriods(LocalDate initialDate, LocalDate finalDate) {
        return null;
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
        return null;
    }

    @Override
    public Flux<UnrestrictedDemand> getAllOfficeBetweenDatesAndPageAndSizeAndFilters(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate, String specie, String family, String office) {
        return null;
    }

    @Override
    public Flux<UnrestrictedDemand> getAllOfficeBetweenDatesAndFilters(LocalDate initialDate, LocalDate finalDate, String specie, String family, String office) {
        return null;
    }

    @Override
    public Flux<UnrestrictedDemand> getAllBetweenDatesByOfficeAndPageAndSize(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate, String office) {
        return unrestrictedDataRepository.findAllByDateRangeOfficeAndOffsetAndSize(initialDate, finalDate, page, size, office).map(UnrestrictedDemandMapper::toUnrestrictedDemand);
    }

    @Override
    public Flux<UnrestrictedDemand> getAllBetweenDatesByOffice(LocalDate initialDate, LocalDate finalDate, String office) {
        return null;
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
        return null;
    }

    @Override
    public Flux<String> getAllSalesStatus() {
        return null;
    }

    @Override
    public Flux<String> getOffice() {
        return null;
    }

    @Override
    public Flux<String> getSalesType() {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Long> countOffice(LocalDate startDate, LocalDate endDate, String office, String especie, String family) {
        return null;
    }

    @Override
    public Mono<Long> countFilters(LocalDate startDate, LocalDate endDate, String especie, String family) {
        return null;
    }

}
