package com.sm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ademus
 *         Date: 11/30/16
 */
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        if(false) {
            System.out.println("Start Not Web");
            new SpringApplicationBuilder(Application.class).web(false).run(args);
        } else {
            System.out.println("Start Web");
            SpringApplication.run(Application.class, args);
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}
