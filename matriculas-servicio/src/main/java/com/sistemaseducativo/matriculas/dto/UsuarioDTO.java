package com.sistemaseducativo.matriculas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// No lleva anotaciones JPA (@Entity, @Id, etc.) porque no se persiste en ESTE servicio.
// Es solo un contenedor de datos para la comunicación.
@Data // Getters, setters, toString, etc.
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los argumentos
public class UsuarioDTO {

    // Los campos deben coincidir (en nombre y tipo) con los que devuelve la API de usuarios-servicio
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String tipoUsuario; 

}