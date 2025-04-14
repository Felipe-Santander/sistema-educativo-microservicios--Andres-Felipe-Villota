package com.sistemaseducativo.matriculas.repository;

import com.sistemaseducativo.matriculas.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // Importar List

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    // Métodos CRUD básicos ya incluidos.

    // Métodos de búsqueda personalizados (útiles más adelante):
    List<Matricula> findByUsuarioId(Long usuarioId); 
    List<Matricula> findByAsignaturaId(Long asignaturaId);
}