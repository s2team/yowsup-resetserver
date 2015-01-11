package de.votesapp.yowsuprest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
public class YowsuprestApplication {

	public static void main(final String[] args) {
		SpringApplication.run(YowsuprestApplication.class, args);
	}
}
