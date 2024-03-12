package cl.camanchaca.optimization.config;

import cl.camanchaca.business.usecases.shared.ReadExcelV2;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.optimization.adapter.excel.DemandPeriodExcelAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(value="cl.camanchaca.business.usecases.optimization",
        useDefaultFilters = false, includeFilters = @ComponentScan.Filter
        (type = FilterType.REGEX, pattern = ".*UseCase")
)
public class OptimizationConfig {

    @Bean
    public MainErrorhandler mainErrorHandler() {
        return new MainErrorhandler();
    }

    @Bean
    public ReadExcelV2 getDemandPeriodExcel(DemandPeriodExcelAdapter demandPeriodExcelAdapter){
        return new ReadExcelV2(demandPeriodExcelAdapter);
    }

}
