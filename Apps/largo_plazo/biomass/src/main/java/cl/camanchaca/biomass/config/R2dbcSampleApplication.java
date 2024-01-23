package cl.camanchaca.biomass.config;


import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.time.Duration;

@Configuration
public class R2dbcSampleApplication extends AbstractR2dbcConfiguration {
    @Value("${db.url}")
    private String url;
    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        String connectionString = url;
        ConnectionFactory connectionFactory = ConnectionFactories.get(connectionString);
        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration
                .builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(1000))
                .maxSize(20)
                .build();

        return new ConnectionPool(configuration);
    }
}
