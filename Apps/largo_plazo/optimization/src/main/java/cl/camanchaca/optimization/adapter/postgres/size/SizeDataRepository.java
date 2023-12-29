package cl.camanchaca.optimization.adapter.postgres.size;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface SizeDataRepository extends ReactiveCrudRepository<SizeData, UUID> {

    Flux<SizeData> findAllBySpecie(String specie);
    Flux<SizeData> findAllByMinRangeAndMaxRangeAndUnitAndSpecieAndPieceType(
            BigDecimal initialRange,
            BigDecimal finalRange,
            String unit,
            String specie,
            String pieceType);

}
