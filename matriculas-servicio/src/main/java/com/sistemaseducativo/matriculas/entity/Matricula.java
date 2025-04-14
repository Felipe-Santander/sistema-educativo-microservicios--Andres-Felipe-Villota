package com.sistemaseducativo.matriculas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import org.hibernate.annotations.CreationTimestamp; // Para fecha automática

@Entity
@Table(name = "matriculas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Guardamos el ID del usuario relacionado
    private Long usuarioId; 

    @Column(nullable = false) // Guardamos el ID de la asignatura relacionada
    private Long asignaturaId;

    @Column(nullable = false, updatable = false) // La fecha no se debe actualizar una vez creada
    @CreationTimestamp // Hibernate asignará automáticamente la fecha actual al crear
    private LocalDate fechaMatricula;

    // Podríamos añadir otros campos si fuera necesario, como 'semestre', 'estado', etc.
}