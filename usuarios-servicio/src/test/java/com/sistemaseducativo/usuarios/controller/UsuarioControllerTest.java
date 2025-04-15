package com.sistemaseducativo.usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper; // Para convertir objetos a JSON
import com.sistemaseducativo.usuarios.entity.Usuario;
import com.sistemaseducativo.usuarios.service.UsuarioService; // Asegúrate que UsuarioService.java no tenga errores propios
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; // Anotación clave
import org.springframework.boot.test.mock.mockito.MockBean; // Para simular el servicio
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc; // Para realizar peticiones HTTP simuladas

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any; // Para matchers de Mockito
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given; // BDDMockito para 'given'
// import static org.mockito.Mockito.doNothing; // No necesario en este test si no se usa delete con void
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // Métodos HTTP (get, post, put, delete)
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // Verificadores de resultado (status, content, jsonPath)
import static org.hamcrest.Matchers.*; // Hamcrest matchers (is, hasSize)


@WebMvcTest(UsuarioController.class) // Indica que es una prueba para UsuarioController
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc; // Objeto para simular peticiones HTTP

    @MockBean // Crea un Mock de UsuarioService y lo inyecta en el contexto de la prueba
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper; // Utilidad para convertir objetos Java a/desde JSON

    private Usuario usuario1;
    private Usuario usuario2;
    private Usuario nuevoUsuario;
    private Usuario usuarioActualizado;


    // --- MÉTODO setUp() CORREGIDO ---
    @BeforeEach // Se ejecuta antes de cada método @Test
    void setUp() {
        // Preparamos algunos datos de prueba - ¡AÑADIR CONTRASEÑA DE PRUEBA (6to argumento)!
        // El orden correcto es: id, nombre, apellido, email, password, tipoUsuario
        usuario1 = new Usuario(1L, "Andres", "Villota", "andres@test.com", "pass123", "ESTUDIANTE"); // CORREGIDO
        usuario2 = new Usuario(2L, "Ana", "Lopez", "ana@test.com", "pass456", "DOCENTE"); // CORREGIDO

        // Usuario sin ID para la creación
        nuevoUsuario = new Usuario(null, "Carlos", "Ruiz", "carlos@test.com", "pass789", "ESTUDIANTE"); // CORREGIDO

        // Usuario con ID para simular la respuesta de creación o actualización
        usuarioActualizado = new Usuario(1L, "Andres Felipe", "Villota Cabrera", "andres.update@test.com", "newPass", "ESTUDIANTE"); // CORREGIDO
    }
    // --- FIN MÉTODO setUp() CORREGIDO ---

    @Test
    void getAllUsuarios_deberiaRetornarListaUsuarios() throws Exception {
        // Given (Dado): Definimos el comportamiento del mock del servicio
        List<Usuario> listaUsuarios = Arrays.asList(usuario1, usuario2);
        given(usuarioService.getAllUsuarios()).willReturn(listaUsuarios);

        // When (Cuando) & Then (Entonces): Ejecutamos la petición y verificamos
        mockMvc.perform(get("/api/usuarios")) // Simula un GET a /api/usuarios
                .andExpect(status().isOk()) // Espera un código de estado 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Espera contenido JSON
                .andExpect(jsonPath("$", hasSize(2))) // Espera que la lista JSON tenga 2 elementos
                .andExpect(jsonPath("$[0].nombre", is("Andres"))) // Verifica el nombre del primer usuario
                .andExpect(jsonPath("$[1].email", is("ana@test.com"))); // Verifica el email del segundo
    }

    @Test
    void getUsuarioById_cuandoExiste_deberiaRetornarUsuario() throws Exception {
        // Given
        Long usuarioId = 1L;
        given(usuarioService.getUsuarioById(usuarioId)).willReturn(Optional.of(usuario1));

        // When & Then
        mockMvc.perform(get("/api/usuarios/{id}", usuarioId)) // Simula GET a /api/usuarios/1
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Andres")))
                .andExpect(jsonPath("$.email", is("andres@test.com")));
                // Nota: No exponemos la contraseña en las respuestas JSON normalmente
    }

    @Test
    void getUsuarioById_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        // Given
        Long usuarioId = 99L;
        given(usuarioService.getUsuarioById(usuarioId)).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isNotFound()); // Espera un código 404 Not Found
    }

    @Test
    void createUsuario_deberiaCrearYRetornarUsuario() throws Exception {
        // Given: Simulamos que el servicio recibe cualquier objeto Usuario y devuelve
        // el usuario1 (que ya tiene ID como si hubiera sido creado)
        given(usuarioService.createUsuario(any(Usuario.class))).willReturn(usuario1); // Simula que devuelve el usuario con ID 1

        // When & Then
        mockMvc.perform(post("/api/usuarios") // Simula un POST a /api/usuarios
                        .contentType(MediaType.APPLICATION_JSON) // Indica que enviamos JSON
                        .content(objectMapper.writeValueAsString(nuevoUsuario))) // Convierte el objeto a JSON para el cuerpo
                .andExpect(status().isCreated()) // Espera un código 201 Created
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1))) // Verifica el ID del usuario devuelto
                .andExpect(jsonPath("$.nombre", is("Andres"))); // Verifica el nombre (usamos usuario1 como respuesta simulada)
    }

    @Test
    void updateUsuario_cuandoExiste_deberiaActualizarYRetornarUsuario() throws Exception {
        // Given
        Long usuarioId = 1L;
        given(usuarioService.updateUsuario(eq(usuarioId), any(Usuario.class))).willReturn(Optional.of(usuarioActualizado));

        // When & Then
        mockMvc.perform(put("/api/usuarios/{id}", usuarioId) // Simula PUT a /api/usuarios/1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioActualizado))) // Envía los datos actualizados
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Andres Felipe")))
                .andExpect(jsonPath("$.email", is("andres.update@test.com")));
    }

    @Test
    void updateUsuario_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        // Given
        Long usuarioId = 99L;
        given(usuarioService.updateUsuario(eq(usuarioId), any(Usuario.class))).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteUsuario_cuandoExiste_deberiaRetornarNoContent() throws Exception {
        // Given
        Long usuarioId = 1L;
        when(usuarioService.deleteUsuario(usuarioId)).thenReturn(true); // Simula que la eliminación fue exitosa

        // When & Then
        mockMvc.perform(delete("/api/usuarios/{id}", usuarioId)) // Simula DELETE a /api/usuarios/1
                .andExpect(status().isNoContent()); // Espera un código 204 No Content
    }

    @Test
    void deleteUsuario_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        // Given
        Long usuarioId = 99L;
        when(usuarioService.deleteUsuario(usuarioId)).thenReturn(false); // Simula que no se encontró para eliminar

        // When & Then
        mockMvc.perform(delete("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isNotFound()); // Espera un código 404 Not Found
    }

}