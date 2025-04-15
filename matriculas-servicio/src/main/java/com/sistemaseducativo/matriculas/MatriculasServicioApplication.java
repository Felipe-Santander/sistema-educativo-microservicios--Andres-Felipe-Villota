package com.sistemaseducativo.matriculas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // <-- AÑADIR IMPORTACIÓN
import org.springframework.cloud.openfeign.EnableFeignClients; 

@SpringBootApplication
@EnableFeignClients(basePackages = "com.sistemaseducativo.matriculas.feignclients") 
@EnableDiscoveryClient // <-- AÑADIR ANOTACIÓN
public class MatriculasServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatriculasServicioApplication.class, args);
    }

}