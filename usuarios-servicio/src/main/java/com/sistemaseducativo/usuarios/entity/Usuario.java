package com.sistemaseducativo.usuarios.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity // Indica que esta clase es una entidad JPA (mapeada a una tabla)
@Table(name = "usuarios") // Especifica el nombre de la tabla en la BD (opcional, por defecto usa el
                          // nombre de la clase)
@Data // Anotación de Lombok: genera getters, setters, toString, equals, hashCode
      // automáticamente
@NoArgsConstructor // Anotación de Lombok: genera un constructor sin argumentos
@AllArgsConstructor // Anotación de Lombok: genera un constructor con todos los argumentos
public class Usuario {

    @Id // Marca este campo como la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el ID es autogenerado por la BD (estrategia común
                                                        // para H2, MySQL, PostgreSQL)
    private Long id;

    @Column(nullable = false) // Indica que este campo mapea a una columna y no puede ser nulo
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(unique = true, nullable = false) // El email debe ser único y no nulo
    private String email;

    @Column(nullable = false)
    private String tipoUsuario; // Podría ser "ESTUDIANTE" o "DOCENTE"

    // No necesitas escribir getters, setters, constructores, etc. ¡Lombok lo hace
    // por ti!
    // Si no usaras Lombok, tendrías que escribirlos manualmente.
}