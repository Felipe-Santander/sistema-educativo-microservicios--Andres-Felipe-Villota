package com.sistemaseducativo.asignaturas.repository;

import com.sistemaseducativo.asignaturas.entity.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    // Métodos CRUD básicos ya incluidos por JpaRepository.
    // Podríamos añadir búsquedas personalizadas si fueran necesarias, ej:
    // List<Asignatura> findByCreditos(Integer creditos); 
}