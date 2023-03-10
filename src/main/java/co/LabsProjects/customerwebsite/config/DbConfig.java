package co.LabsProjects.customerwebsite.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DbConfig {
    // @Value annotation assigns the value to attribute at the time of bean creation
    @Value("${spring.datasource.driver}") private String mainDatasourceDriver;
    @Value("${spring.datasource.url}") private String mainDatasourceUrl;
    @Value("${spring.datasource.username}") private String mainDatasourceUsername;
    @Value("${spring.datasource.password}") private String mainDatasourcePassword;

    @Bean
    // The @Primary annotation is used to assign in higher preference to the annotated bean during bean injection when the class has multiple beans of same time, in this case we have two beans of the DataSource type.
    public DataSource mainDatasource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(mainDatasourceDriver);
        config.setJdbcUrl(mainDatasourceUrl);
        config.setUsername(mainDatasourceUsername);
        config.setPassword(mainDatasourcePassword);
        return new HikariDataSource(config);
    }
}
