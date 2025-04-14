package com.sistemaseducativo.usuarios.controller;

import com.sistemaseducativo.usuarios.entity.Usuario;
import com.sistemaseducativo.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones de request mapping

import java.util.List;
import java.util.Optional;

@RestController // Combina @Controller y @ResponseBody, ideal para APIs REST que devuelven JSON/XML
@RequestMapping("/api/usuarios") // Define la ruta base para todas las URLs manejadas por este controlador
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; // Inyecta el servicio

    // Endpoint para OBTENER TODOS los usuarios (GET /api/usuarios)
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
        // Spring convierte automáticamente la List<Usuario> a JSON
    }

    // Endpoint para OBTENER UN usuario por ID (GET /api/usuarios/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        // @PathVariable extrae el valor {id} de la URL
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);

        // Usamos ResponseEntity para controlar la respuesta HTTP completa (status code, body)
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get()); // Devuelve 200 OK con el usuario en el cuerpo
        } else {
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found si no existe
        }
        // Alternativa más corta usando lambdas:
        // return usuarioService.getUsuarioById(id)
        //        .map(ResponseEntity::ok) // Si el Optional tiene valor, envuélvelo en ResponseEntity.ok()
        //        .orElse(ResponseEntity.notFound().build()); // Si está vacío, devuelve 404
    }

    // Endpoint para CREAR un nuevo usuario (POST /api/usuarios)
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        // @RequestBody convierte el cuerpo JSON de la petición en un objeto Usuario
        Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
        // Devuelve 201 Created con el usuario recién creado en el cuerpo
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario); 
    }

    // Endpoint para ACTUALIZAR un usuario existente (PUT /api/usuarios/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        Optional<Usuario> usuarioActualizado = usuarioService.updateUsuario(id, usuarioDetails);

        return usuarioActualizado
                .map(ResponseEntity::ok) // Si la actualización fue exitosa (usuario encontrado), devuelve 200 OK con el usuario actualizado
                .orElse(ResponseEntity.notFound().build()); // Si el usuario no se encontró para actualizar, devuelve 404
    }

    // Endpoint para ELIMINAR un usuario (DELETE /api/usuarios/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        boolean eliminado = usuarioService.deleteUsuario(id);

        if (eliminado) {
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content si se eliminó correctamente
        } else {
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found si no se encontró
        }
    }
}