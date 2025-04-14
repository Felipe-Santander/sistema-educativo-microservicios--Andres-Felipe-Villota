package com.sistemaseducativo.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;// <-- Importar
// import org.springframework.cloud.netflix.eureka.EnableEurekaClient; // <-- Lo usaremos después

@SpringBootApplication
@EnableConfigServer // <-- AÑADIR ESTA LÍNEA
// @EnableEurekaClient // <-- Lo usaremos después
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

}