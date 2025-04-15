package com.sistemaseducativo.matriculas.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any; // Para simular errores del servicio
import static org.mockito.BDDMockito.given; // Para usar en ResponseStatusException
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; // Para simular excepciones
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemaseducativo.matriculas.entity.Matricula;
import com.sistemaseducativo.matriculas.service.MatriculaService;

@WebMvcTest(MatriculaController.class) // Probamos MatriculaController
public class MatriculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Solo simulamos la dependencia directa: MatriculaService
    private MatriculaService matriculaService;

    // No necesitamos @MockBean para los Feign Clients aquí

    @Autowired
    private ObjectMapper objectMapper;

    private Matricula matricula1;
    private Matricula matricula2;
    private Matricula nuevaMatriculaInput; // Datos de entrada para POST
    private Matricula matriculaCreada; // Simulación de respuesta del POST

    @BeforeEach
    void setUp() {
        // Usamos una fecha fija o LocalDate.now()
        LocalDate fecha = LocalDate.of(2025, 4, 14); // O LocalDate.now()

        matricula1 = new Matricula(1L, 10L, 101L, fecha); // (id, usuarioId, asignaturaId, fecha)
        matricula2 = new Matricula(2L, 11L, 102L, fecha.plusDays(1));

        // Datos que enviaríamos en un POST (sin ID, sin fecha)
        nuevaMatriculaInput = new Matricula(null, 12L, 103L, null);

        // Cómo se vería la matrícula una vez creada por el servicio (con ID y fecha)
        matriculaCreada = new Matricula(3L, 12L, 103L, fecha.plusDays(2));
    }

    @Test
    void getAllMatriculas_deberiaRetornarListaMatriculas() throws Exception {
        // Given
        List<Matricula> listaMatriculas = Arrays.asList(matricula1, matricula2);
        given(matriculaService.getAllMatriculas()).willReturn(listaMatriculas);

        // When & Then
        mockMvc.perform(get("/api/matriculas")) // Ruta base /api/matriculas
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].usuarioId", is(10)))
                .andExpect(jsonPath("$[1].asignaturaId", is(102)));
    }

    @Test
    void getMatriculaById_cuandoExiste_deberiaRetornarMatricula() throws Exception {
        // Given
        Long matriculaId = 1L;
        given(matriculaService.getMatriculaById(matriculaId)).willReturn(Optional.of(matricula1));

        // When & Then
        mockMvc.perform(get("/api/matriculas/{id}", matriculaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.usuarioId", is(10)))
                // Verificar la fecha como String es más simple
                .andExpect(jsonPath("$.fechaMatricula", is("2025-04-14")));
    }

    @Test
    void getMatriculaById_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        // Given
        Long matriculaId = 99L;
        given(matriculaService.getMatriculaById(matriculaId)).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/matriculas/{id}", matriculaId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMatriculasByUsuarioId_deberiaRetornarListaMatriculas() throws Exception {
        // Given
        Long usuarioId = 10L;
        List<Matricula> matriculasUsuario = Collections.singletonList(matricula1); // Solo la matricula1 tiene usuarioId 10
        given(matriculaService.getMatriculasByUsuarioId(usuarioId)).willReturn(matriculasUsuario);

        // When & Then
        mockMvc.perform(get("/api/matriculas/usuario/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getMatriculasByAsignaturaId_deberiaRetornarListaMatriculas() throws Exception {
        // Given
        Long asignaturaId = 102L;
        List<Matricula> matriculasAsignatura = Collections.singletonList(matricula2); // Solo matricula2 tiene asignaturaId 102
        given(matriculaService.getMatriculasByAsignaturaId(asignaturaId)).willReturn(matriculasAsignatura);

        // When & Then
        mockMvc.perform(get("/api/matriculas/asignatura/{asignaturaId}", asignaturaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)));
    }


    @Test
    void createMatricula_cuandoExitoso_deberiaRetornarCreated() throws Exception {
        // Given: Simulamos que el servicio procesa la entrada y devuelve la matrícula completa
        given(matriculaService.createMatricula(any(Matricula.class))).willReturn(matriculaCreada);

        // When & Then
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        // Enviamos el objeto sin ID ni fecha
                        .content(objectMapper.writeValueAsString(nuevaMatriculaInput)))
                .andExpect(status().isCreated()) // Esperamos 201 Created
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3))) // Verificamos la respuesta simulada
                .andExpect(jsonPath("$.usuarioId", is(12)))
                .andExpect(jsonPath("$.asignaturaId", is(103)))
                // Verifica que la fecha esté presente en la respuesta simulada
                .andExpect(jsonPath("$.fechaMatricula", notNullValue()));
    }

    @Test
    void createMatricula_cuandoServicioFalla_deberiaRetornarBadRequest() throws Exception {
        // Given: Simulamos que el servicio lanza una excepción
        // (podría ser por fallo en Feign, validación, etc.)
        given(matriculaService.createMatricula(any(Matricula.class)))
               .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")); // O cualquier otra excepción

        // When & Then
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaMatriculaInput)))
                 // El controlador tiene un try-catch genérico que devuelve BadRequest
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteMatricula_cuandoExiste_deberiaRetornarNoContent() throws Exception {
        // Given
        Long matriculaId = 1L;
        when(matriculaService.deleteMatricula(matriculaId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/matriculas/{id}", matriculaId))
                .andExpect(status().isNoContent()); // Espera 204 No Content
    }

    @Test
    void deleteMatricula_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        // Given
        Long matriculaId = 99L;
        when(matriculaService.deleteMatricula(matriculaId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/matriculas/{id}", matriculaId))
                .andExpect(status().isNotFound()); // Espera 404 Not Found
    }
}