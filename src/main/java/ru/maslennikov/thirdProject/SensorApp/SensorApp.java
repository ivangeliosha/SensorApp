package ru.maslennikov.thirdProject.SensorApp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SensorApp {

	public static void main(String[] args) {
		SpringApplication.run(SensorApp.class, args);
	}

	@Bean
    protected ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
