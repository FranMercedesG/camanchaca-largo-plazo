package cl.camanchaca.parametrization;

import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.parametrization.adapter.excel.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(value = {
        "cl.camanchaca.business.usecases.largoplazo.parameters",
        "cl.camanchaca.business.usecases.shared"
},        useDefaultFilters = false, includeFilters = @ComponentScan.Filter
        (type = FilterType.REGEX, pattern = ".*UseCase")
)
public class ParametersConfig {
    @Bean
    public MainErrorhandler getMainErrorhandler() {
        return new MainErrorhandler();
    }

    @Bean
    public ReadExcel getReadSKUexcel(ExcelParameterSKUAdapter excelParameterSKUAdapter){
        return new ReadExcel(excelParameterSKUAdapter);
    }
    @Bean
    public ReadExcel getReadSizeexcel(ExcelParameterSizeAdapter excelParameterSizeAdapter){
        return new ReadExcel(excelParameterSizeAdapter);
    }
    @Bean
    public ReadExcel getReadPerformanceExcel(ExcelParameterPerformanceAdapter excelParameterPerformanceAdapter){
        return new ReadExcel(excelParameterPerformanceAdapter);
    }

    @Bean
    public ReadExcel getReadCapacitiesExcel(ExcelParameterCapacityAdapter excelParameterCapacityAdapter){
        return new ReadExcel(excelParameterCapacityAdapter);
    }

    @Bean
    public ReadExcel getReadMinimumExcel(ExcelParameterMinimumAdapter excelParameterMinimumAdapter){
        return new ReadExcel(excelParameterMinimumAdapter);
    }

    @Bean
    public ReadExcel getReadGroupExcel(ExcelParameterGroupAdapter excelParameterGroupAdapter){
        return new ReadExcel(excelParameterGroupAdapter);
    }

}
