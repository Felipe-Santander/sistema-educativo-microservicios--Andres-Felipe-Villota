package com.sistemaseducativo.usuarios;

import org.springframework.boot.SpringApplication; // <-- Asegúrate que esté importado
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; 

@SpringBootApplication
@EnableDiscoveryClient 
public class UsuariosServicioApplication {

    // Método main COMPLETO
    public static void main(String[] args) {
        SpringApplication.run(UsuariosServicioApplication.class, args); // <-- ESTA LÍNEA FALTABA
    }

}