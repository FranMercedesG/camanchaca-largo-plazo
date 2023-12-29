package cl.camanchaca.optimization.config;

import cl.camanchaca.generics.MainErrorhandler;
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


}
