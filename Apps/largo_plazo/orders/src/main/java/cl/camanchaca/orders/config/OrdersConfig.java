package cl.camanchaca.orders.config;

import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.orders.adapters.excel.ExcelDemandOrderAdminAdapter;
import cl.camanchaca.orders.adapters.excel.ExcelDemandOrderOfficeAdapter;
import io.r2dbc.spi.ConnectionFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;

@Configuration
@AllArgsConstructor
@ComponentScan(
  value = "cl.camanchaca.business.usecases.largoplazo.orders",
  useDefaultFilters = false,
  includeFilters = @ComponentScan.Filter(
    type = FilterType.REGEX,
    pattern = ".*UseCase"
  )
)
public class OrdersConfig {

  @Bean
  public MainErrorhandler mainErrorHandler() {
    return new MainErrorhandler();
  }

  @Bean
  public ReadExcel getReadDemandAdminExcel(ExcelDemandOrderAdminAdapter orderAdminAdapter){
    return new ReadExcel(orderAdminAdapter);
  }

  @Bean
  public ReadExcel getReadDemandOfficeExcel(ExcelDemandOrderOfficeAdapter orderOfficeAdapter){
    return new ReadExcel(orderOfficeAdapter);
  }

}
