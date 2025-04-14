package com.sistemaseducativo.asignaturas.service;

import com.sistemaseducativo.asignaturas.entity.Asignatura;
import com.sistemaseducativo.asignaturas.repository.AsignaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    public List<Asignatura> getAllAsignaturas() {
        return asignaturaRepository.findAll();
    }

    public Optional<Asignatura> getAsignaturaById(Long id) {
        return asignaturaRepository.findById(id);
    }

    public Asignatura createAsignatura(Asignatura asignatura) {
        // Podríamos añadir validación, ej: verificar que el nombre no exista ya
        return asignaturaRepository.save(asignatura);
    }

    public Optional<Asignatura> updateAsignatura(Long id, Asignatura asignaturaDetails) {
        return asignaturaRepository.findById(id).map(asignaturaExistente -> {
            asignaturaExistente.setNombre(asignaturaDetails.getNombre());
            asignaturaExistente.setDescripcion(asignaturaDetails.getDescripcion());
            asignaturaExistente.setCreditos(asignaturaDetails.getCreditos());
            return asignaturaRepository.save(asignaturaExistente);
        }); // .map() es una forma más concisa de hacer el if/else del Optional
    }

    public boolean deleteAsignatura(Long id) {
        if (asignaturaRepository.existsById(id)) {
            asignaturaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}