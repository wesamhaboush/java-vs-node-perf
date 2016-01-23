package net.codebreeze.rest.server.config;

import net.codebreeze.rest.server.services.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@ComponentScan( basePackages = {"net.codebreeze.rest.server"} )
@EnableWebMvc
public class AppConfig
{

    @Bean
    public HelloService helloService()
    {
        return new HelloService();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
