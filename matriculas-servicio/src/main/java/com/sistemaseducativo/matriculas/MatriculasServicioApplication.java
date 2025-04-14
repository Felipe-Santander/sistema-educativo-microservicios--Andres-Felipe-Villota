package com.sistemaseducativo.matriculas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; 

@SpringBootApplication
// Modifica esta línea: Especifica el paquete donde están tus interfaces Feign Client
@EnableFeignClients(basePackages = "com.sistemaseducativo.matriculas.feignclients") 
public class MatriculasServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatriculasServicioApplication.class, args);
    }

}