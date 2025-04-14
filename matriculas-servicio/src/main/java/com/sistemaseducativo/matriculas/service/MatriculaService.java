package com.sistemaseducativo.matriculas.service;

import com.sistemaseducativo.matriculas.entity.Matricula;
import com.sistemaseducativo.matriculas.repository.MatriculaRepository;
import com.sistemaseducativo.matriculas.feignclients.UsuarioFeignClient; // <-- Importar Feign Client Usuario
import com.sistemaseducativo.matriculas.feignclients.AsignaturaFeignClient; // <-- Importar Feign Client Asignatura
import com.sistemaseducativo.matriculas.dto.UsuarioDTO; // <-- Importar DTO Usuario
import com.sistemaseducativo.matriculas.dto.AsignaturaDTO; // <-- Importar DTO Asignatura
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // <-- Importar HttpStatus
import org.springframework.http.ResponseEntity; // <-- Importar ResponseEntity
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException; // <-- Importar para errores HTTP

import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    // Inyectamos los Feign Clients
    @Autowired
    private UsuarioFeignClient usuarioFeignClient;

    @Autowired
    private AsignaturaFeignClient asignaturaFeignClient;

    public List<Matricula> getAllMatriculas() {
        return matriculaRepository.findAll();
    }

    public Optional<Matricula> getMatriculaById(Long id) {
        return matriculaRepository.findById(id);
    }

    public List<Matricula> getMatriculasByUsuarioId(Long usuarioId) {
        return matriculaRepository.findByUsuarioId(usuarioId);
    }

    public List<Matricula> getMatriculasByAsignaturaId(Long asignaturaId) {
        return matriculaRepository.findByAsignaturaId(asignaturaId);
    }

    // --- MÉTODO MODIFICADO ---
    public Matricula createMatricula(Matricula matricula) {
        // 1. Validar que el usuario exista llamando a usuarios-servicio
        try {
            ResponseEntity<UsuarioDTO> usuarioResponse = usuarioFeignClient.getUsuarioById(matricula.getUsuarioId());
            if (!usuarioResponse.getStatusCode().is2xxSuccessful() || usuarioResponse.getBody() == null) {
                // Si la respuesta no es exitosa (ej. 404) o el cuerpo es nulo, lanza excepción
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + matricula.getUsuarioId());
            }
            // Opcional: Podrías querer verificar si el usuario es un ESTUDIANTE aquí
            // UsuarioDTO usuario = usuarioResponse.getBody();
            // if (!"ESTUDIANTE".equalsIgnoreCase(usuario.getTipoUsuario())) {
            //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no es un estudiante.");
            // }

        } catch (Exception e) {
             // Si ocurre cualquier excepción durante la llamada Feign (ej. servicio caído, 404, etc.)
             System.err.println("Error al validar usuario ID " + matricula.getUsuarioId() + ": " + e.getMessage());
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado o error al validar ID: " + matricula.getUsuarioId(), e);
        }


        // 2. Validar que la asignatura exista llamando a asignaturas-servicio
         try {
            ResponseEntity<AsignaturaDTO> asignaturaResponse = asignaturaFeignClient.getAsignaturaById(matricula.getAsignaturaId());
             if (!asignaturaResponse.getStatusCode().is2xxSuccessful() || asignaturaResponse.getBody() == null) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada con ID: " + matricula.getAsignaturaId());
            }
         } catch (Exception e) {
             System.err.println("Error al validar asignatura ID " + matricula.getAsignaturaId() + ": " + e.getMessage());
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada o error al validar ID: " + matricula.getAsignaturaId(), e);
        }

        // 3. Si ambos existen, guardar la matrícula
        // La fecha se asigna automáticamente por @CreationTimestamp
        return matriculaRepository.save(matricula);
    }

    public boolean deleteMatricula(Long id) {
        if (matriculaRepository.existsById(id)) {
            matriculaRepository.deleteById(id);
            return true;
        }
        return false;
    }

     // -- OPCIONAL: Método para obtener detalles completos (más adelante) --
     // Podríamos crear un método que devuelva una matrícula con los detalles completos
     // del usuario y la asignatura, usando los Feign Clients.
     // public MatriculaCompletaDTO getMatriculaCompletaById(Long id) { ... }

}