package com.sistemaseducativo.usuarios.service;

import com.sistemaseducativo.usuarios.entity.Usuario;
import com.sistemaseducativo.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un componente de Servicio de Spring
public class UsuarioService {

    // Inyección de dependencias: Spring nos proporcionará una instancia de UsuarioRepository
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para obtener todos los usuarios
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll(); // Llama al método findAll() del repositorio
    }

    // Método para obtener un usuario por su ID
    public Optional<Usuario> getUsuarioById(Long id) {
        // findById devuelve un Optional, que puede contener un Usuario o estar vacío si no se encuentra
        return usuarioRepository.findById(id); 
    }

    // Método para crear un nuevo usuario
    public Usuario createUsuario(Usuario usuario) {
        // Podríamos añadir validaciones aquí antes de guardar
        // Por ejemplo, verificar si el email ya existe, etc.
        return usuarioRepository.save(usuario); // Guarda la entidad usando el repositorio
    }

    // Método para actualizar un usuario existente
    public Optional<Usuario> updateUsuario(Long id, Usuario usuarioDetails) {
        // 1. Buscar el usuario existente por ID
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        // 2. Si existe, actualizar sus datos
        if (optionalUsuario.isPresent()) {
            Usuario usuarioExistente = optionalUsuario.get();
            usuarioExistente.setNombre(usuarioDetails.getNombre());
            usuarioExistente.setApellido(usuarioDetails.getApellido());
            usuarioExistente.setEmail(usuarioDetails.getEmail());
            usuarioExistente.setTipoUsuario(usuarioDetails.getTipoUsuario());
            // Guardamos el usuario actualizado. El método save() también sirve para actualizar si la entidad ya tiene un ID.
            return Optional.of(usuarioRepository.save(usuarioExistente)); 
        } else {
            // 3. Si no existe, devolver un Optional vacío
            return Optional.empty(); 
        }
    }

    // Método para eliminar un usuario por ID
    public boolean deleteUsuario(Long id) {
        // 1. Verificar si el usuario existe antes de intentar borrarlo
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id); // Elimina el usuario
            return true; // Indica que la eliminación fue exitosa
        } else {
            return false; // Indica que el usuario no se encontró
        }
    }
}