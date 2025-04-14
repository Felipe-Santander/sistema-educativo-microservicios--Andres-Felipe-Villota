package com.sistemaseducativo.matriculas.feignclients;

import com.sistemaseducativo.matriculas.dto.UsuarioDTO; // Importa el DTO que creamos
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity; // Importa ResponseEntity
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// La anotación @FeignClient define que esta interfaz es un cliente Feign
@FeignClient(name = "usuarios-servicio", url = "http://localhost:8081/api/usuarios") 
// name: Es el nombre lógico del microservicio al que llamamos (importante para Eureka después)
// url: Es la URL base directa del microservicio (cambiará cuando usemos Eureka)
public interface UsuarioFeignClient {

    // Este método representa una llamada al endpoint GET /api/usuarios/{id} del usuarios-servicio
    @GetMapping("/{id}") // La ruta relativa al 'url' base definido arriba
    ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id); 
    // - @PathVariable funciona igual que en @RestController
    // - El tipo de retorno es ResponseEntity<UsuarioDTO>. Feign interpretará la respuesta JSON
    //   del otro servicio y la convertirá en un UsuarioDTO. Usar ResponseEntity nos permite
    //   verificar el código de estado (ej. 200 OK, 404 Not Found).

    // Podríamos añadir aquí otros métodos si necesitáramos llamar a otros endpoints de usuarios-servicio
    // Por ejemplo:
    // @PostMapping
    // UsuarioDTO createUsuario(@RequestBody UsuarioDTO usuario); 
}