package com.sistemaseducativo.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemaseducativo.usuarios.entity.Usuario; // Asegúrate que Optional esté importado

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // --- NUEVO MÉTODO ---
    // Spring Data JPA generará la consulta automáticamente basándose en el nombre del método
    Optional<Usuario> findByEmail(String email);
    // --- FIN NUEVO MÉTODO ---

    // Métodos existentes...
}