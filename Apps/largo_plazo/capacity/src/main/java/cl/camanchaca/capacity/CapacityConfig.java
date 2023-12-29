package cl.camanchaca.capacity;

import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.capacity.adapters.excel.maximum.ExcelMinimumTotalCapacityAdapter;
import cl.camanchaca.capacity.adapters.excel.maximum.ExcelTotalCapacityAdapter;
import cl.camanchaca.generics.MainErrorhandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@AllArgsConstructor
@ComponentScan(
        value = "cl.camanchaca.business.usecases.largoplazo.capacity",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = ".*UseCase"
        )
)
public class CapacityConfig {

    @Bean
    public MainErrorhandler mainErrorHandler() {
        return new MainErrorhandler();
    }

    @Bean
    public ReadExcel getCapacityTotalBulkExcel(ExcelTotalCapacityAdapter totalCapacityAdapter){
        return new ReadExcel(totalCapacityAdapter);
    }

    @Bean
    public ReadExcel getCapacityMinimumTotalBulkExcel(ExcelMinimumTotalCapacityAdapter totalCapacityAdapter){
        return new ReadExcel(totalCapacityAdapter);

    }
}
