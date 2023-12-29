package cl.camanchaca.parametrization.entrypoints.cron;

import cl.camanchaca.business.usecases.largoplazo.parameters.bigquery.SaveCSTDUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.TimeZone;

@Configuration
@EnableScheduling
@AllArgsConstructor
@Slf4j
public class BigqueryCSTD {

    private final SaveCSTDUseCase saveCSTDUseCase;

    @Scheduled(cron = "0 0 2 L * ?")
    public void updateProductsData() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Santiago"));
        log.info(" init save product ");
        saveCSTDUseCase.SaveSKU()
                .doOnComplete(() -> log.info(" finished save "))
                .subscribe();
    }

}
