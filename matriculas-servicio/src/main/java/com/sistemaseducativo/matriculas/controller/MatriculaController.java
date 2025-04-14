package com.sistemaseducativo.matriculas.controller;

import com.sistemaseducativo.matriculas.entity.Matricula;
import com.sistemaseducativo.matriculas.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    // Obtener todas las matrículas
    @GetMapping
    public List<Matricula> getAllMatriculas() {
        return matriculaService.getAllMatriculas();
    }

    // Obtener matrícula por ID
    @GetMapping("/{id}")
    public ResponseEntity<Matricula> getMatriculaById(@PathVariable Long id) {
        return matriculaService.getMatriculaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener matrículas por ID de Usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Matricula> getMatriculasByUsuarioId(@PathVariable Long usuarioId) {
        return matriculaService.getMatriculasByUsuarioId(usuarioId);
        // Podríamos devolver ResponseEntity<List<Matricula>> si quisiéramos manejar el caso de lista vacía con 404,
        // pero devolver una lista vacía con 200 OK también es común.
    }

    // Obtener matrículas por ID de Asignatura
    @GetMapping("/asignatura/{asignaturaId}")
    public List<Matricula> getMatriculasByAsignaturaId(@PathVariable Long asignaturaId) {
        return matriculaService.getMatriculasByAsignaturaId(asignaturaId);
    }


    // Crear una nueva matrícula
    @PostMapping
    public ResponseEntity<Matricula> createMatricula(@RequestBody Matricula matricula) {
        // El JSON de entrada debe incluir "usuarioId" y "asignaturaId"
        // ej: { "usuarioId": 1, "asignaturaId": 5 }
        // La fecha se genera automáticamente.
        try {
             // Más adelante aquí vendrá la validación con Feign
            Matricula nuevaMatricula = matriculaService.createMatricula(matricula);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMatricula);
        } catch (Exception e) {
            // Manejo básico de errores si algo falla al guardar (ej: IDs nulos)
            return ResponseEntity.badRequest().build(); 
        }
    }

    // Eliminar una matrícula por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatricula(@PathVariable Long id) {
        if (matriculaService.deleteMatricula(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}