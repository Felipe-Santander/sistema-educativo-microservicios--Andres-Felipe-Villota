package com.sistemaseducativo.usuarios.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(unique = true, nullable = false) // El email será nuestro "username"
    private String email;

    // --- NUEVO CAMPO ---
    @Column(nullable = false) // La contraseña no puede ser nula
    private String password;
    // --- FIN NUEVO CAMPO ---

    @Column(nullable = false)
    private String tipoUsuario; // Podría ser "ESTUDIANTE" o "DOCENTE" (o podríamos usar roles más adelante)

    // Lombok @Data genera automáticamente:
    // - Getters y Setters para todos los campos (incluido password)
    // - toString()
    // - equals() y hashCode()
    // Lombok @NoArgsConstructor y @AllArgsConstructor generan los constructores.
}