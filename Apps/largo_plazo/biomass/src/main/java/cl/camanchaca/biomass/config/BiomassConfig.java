package cl.camanchaca.biomass.config;

import cl.camanchaca.biomass.adapter.excel.GroupMaxExcelAdapter;
import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.generics.MainErrorhandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(value={
        "cl.camanchaca.business.usecases.largoplazo.biomass"
},
        useDefaultFilters = false, includeFilters = @ComponentScan.Filter
        (type = FilterType.REGEX, pattern = ".*UseCase")
)
public class BiomassConfig {

    @Bean
    public ReadExcel getGroupMaxBulkExcel(GroupMaxExcelAdapter groupMaxExcelAdapter){
        return new ReadExcel(groupMaxExcelAdapter);
    }
    @Bean
    public MainErrorhandler getMainErrorhandler(){
        return new MainErrorhandler();
    }

}
