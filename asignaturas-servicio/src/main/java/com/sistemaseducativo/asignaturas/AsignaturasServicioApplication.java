package com.sistemaseducativo.asignaturas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // <-- Importar

@SpringBootApplication
@EnableDiscoveryClient // <-- AÑADIR
public class AsignaturasServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsignaturasServicioApplication.class, args);
    }

}