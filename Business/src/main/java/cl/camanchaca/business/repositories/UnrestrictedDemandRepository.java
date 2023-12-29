package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface UnrestrictedDemandRepository {

    Flux<UnrestrictedDemand> getAll();

    Flux<UnrestrictedDemand> getAllByPeriods(LocalDate initialDate, LocalDate finalDate);

    Flux<UnrestrictedDemand> getAllByPageAndSize(Integer page, Integer size);

    Flux<UnrestrictedDemand> getAllByPeriodAndPageAndSize(Integer page, Integer size, LocalDate date);

    Flux<UnrestrictedDemand> getAllBetweenDatesAndPageAndSize(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate);

    Flux<UnrestrictedDemand> getAllBetweenDatesAndPageAndSizeAndFilters(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate, String specie, String family);

    Flux<UnrestrictedDemand> getAllOfficeBetweenDatesAndPageAndSizeAndFilters(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate, String specie, String family, String office);

    Flux<UnrestrictedDemand> getAllOfficeBetweenDatesAndFilters(LocalDate initialDate, LocalDate finalDate, String specie, String family, String office);

    Flux<UnrestrictedDemand> getAllBetweenDatesByOfficeAndPageAndSize(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate, String office);

    Flux<UnrestrictedDemand> getAllBetweenDatesByOffice(LocalDate initialDate, LocalDate finalDate, String office);

    Flux<UnrestrictedDemand> saveAll(Flux<UnrestrictedDemand> list);

    Mono<Void> deleteAllByUser(String user);

    Mono<Void> deleteAllByUserAndOrigen(String user, String origen);


    Flux<String> getAllSalesStatus();

    Flux<String> getOffice();

    Flux<String> getSalesType();

    Mono<Long> count();

    Mono<Long> countOffice(LocalDate startDate, LocalDate endDate, String office, String especie, String family);

    Mono<Long> countFilters(LocalDate startDate, LocalDate endDate, String especie, String family);

}
