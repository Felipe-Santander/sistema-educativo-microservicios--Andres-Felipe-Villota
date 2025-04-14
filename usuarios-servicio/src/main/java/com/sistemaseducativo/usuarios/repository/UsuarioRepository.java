package com.sistemaseducativo.usuarios.repository;

import com.sistemaseducativo.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Indica que esta interfaz es un componente Repository de Spring (aunque es opcional si extiendes JpaRepository)
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // ¡Eso es todo por ahora!
    // Spring Data JPA automáticamente nos dará métodos como:
    // save(Usuario usuario): Guarda o actualiza un usuario.
    // findById(Long id): Busca un usuario por su ID.
    // findAll(): Devuelve todos los usuarios.
    // deleteById(Long id): Elimina un usuario por su ID.
    // ... y muchos más!

    // Si necesitáramos consultas más específicas en el futuro (ej: buscar por email),
    // podríamos definirlas aquí. Por ejemplo:
    // Optional<Usuario> findByEmail(String email);
    // Spring Data JPA interpretaría el nombre del método y generaría la consulta SQL apropiada.

}