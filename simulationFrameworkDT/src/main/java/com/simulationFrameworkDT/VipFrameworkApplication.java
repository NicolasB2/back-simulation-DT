package com.simulationFrameworkDT;

import java.text.ParseException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan("com.simulationFrameworkDT")
public class VipFrameworkApplication{
	
	public static void main(String[] args) throws ParseException {		
		SpringApplication.run(VipFrameworkApplication.class, args);
	}
}
