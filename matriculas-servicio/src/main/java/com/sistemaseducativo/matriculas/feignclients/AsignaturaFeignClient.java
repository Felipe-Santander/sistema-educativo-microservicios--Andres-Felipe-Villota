package com.sistemaseducativo.matriculas.feignclients;

import com.sistemaseducativo.matriculas.dto.AsignaturaDTO; // Importa el DTO
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "asignaturas-servicio", url = "http://localhost:8082/api/asignaturas")
// name: nombre lógico del servicio de asignaturas
// url: URL directa por ahora (puerto 8082)
public interface AsignaturaFeignClient {

    @GetMapping("/{id}")
    ResponseEntity<AsignaturaDTO> getAsignaturaById(@PathVariable Long id);

    // Podríamos añadir más métodos si fueran necesarios...
}