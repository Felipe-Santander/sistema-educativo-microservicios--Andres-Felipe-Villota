package com.sistemaseducativo.asignaturas.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemaseducativo.asignaturas.entity.Asignatura;
import com.sistemaseducativo.asignaturas.service.AsignaturaService;

@WebMvcTest(AsignaturaController.class) // Probamos AsignaturaController
public class AsignaturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Simulamos AsignaturaService
    private AsignaturaService asignaturaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Asignatura asignatura1;
    private Asignatura asignatura2;
    private Asignatura nuevaAsignatura;
    private Asignatura asignaturaActualizada;

    @BeforeEach
    void setUp() {
        // Datos de prueba para Asignatura
        asignatura1 = new Asignatura(1L, "Cálculo I", "Introducción al cálculo diferencial", 4);
        asignatura2 = new Asignatura(2L, "Programación Orientada a Objetos", "Conceptos de POO con Java", 3);
        
        // Asignatura sin ID para creación
        nuevaAsignatura = new Asignatura(null, "Física I", "Mecánica clásica", 4);
        
        // Asignatura con ID para simular respuesta de creación/actualización
        asignaturaActualizada = new Asignatura(1L, "Cálculo Diferencial", "Cálculo diferencial avanzado", 5);
    }

    @Test
    void getAllAsignaturas_deberiaRetornarListaAsignaturas() throws Exception {
        // Given
        List<Asignatura> listaAsignaturas = Arrays.asList(asignatura1, asignatura2);
        given(asignaturaService.getAllAsignaturas()).willReturn(listaAsignaturas);

        // When & Then
        mockMvc.perform(get("/api/asignaturas")) // Ruta base /api/asignaturas
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Cálculo I")))
                .andExpect(jsonPath("$[1].creditos", is(3)));
    }

    @Test
    void getAsignaturaById_cuandoExiste_deberiaRetornarAsignatura() throws Exception {
        // Given
        Long asignaturaId = 1L;
        given(asignaturaService.getAsignaturaById(asignaturaId)).willReturn(Optional.of(asignatura1));

        // When & Then
        mockMvc.perform(get("/api/asignaturas/{id}", asignaturaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Cálculo I")))
                .andExpect(jsonPath("$.descripcion", is("Introducción al cálculo diferencial")));
    }

    @Test
    void getAsignaturaById_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        // Given
        Long asignaturaId = 99L;
        given(asignaturaService.getAsignaturaById(asignaturaId)).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/asignaturas/{id}", asignaturaId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAsignatura_deberiaCrearYRetornarAsignatura() throws Exception {
        // Given: Simulamos que el servicio devuelve la asignatura con ID 1 al crear
        given(asignaturaService.createAsignatura(any(Asignatura.class))).willReturn(asignatura1); 

        // When & Then
        mockMvc.perform(post("/api/asignaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaAsignatura))) // Enviamos nuevaAsignatura
                .andExpect(status().isCreated()) // Esperamos 201 Created
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1))) // Verificamos la respuesta simulada
                .andExpect(jsonPath("$.nombre", is("Cálculo I")));
    }

    @Test
    void updateAsignatura_cuandoExiste_deberiaActualizarYRetornarAsignatura() throws Exception {
        // Given
        Long asignaturaId = 1L;
        given(asignaturaService.updateAsignatura(eq(asignaturaId), any(Asignatura.class))).willReturn(Optional.of(asignaturaActualizada));

        // When & Then
        mockMvc.perform(put("/api/asignaturas/{id}", asignaturaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asignaturaActualizada)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Cálculo Diferencial")))
                .andExpect(jsonPath("$.creditos", is(5)));
    }

    @Test
    void updateAsignatura_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        // Given
        Long asignaturaId = 99L;
        given(asignaturaService.updateAsignatura(eq(asignaturaId), any(Asignatura.class))).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/asignaturas/{id}", asignaturaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asignaturaActualizada)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAsignatura_cuandoExiste_deberiaRetornarNoContent() throws Exception {
        // Given
        Long asignaturaId = 1L;
        when(asignaturaService.deleteAsignatura(asignaturaId)).thenReturn(true); // Simula éxito

        // When & Then
        mockMvc.perform(delete("/api/asignaturas/{id}", asignaturaId))
                .andExpect(status().isNoContent()); // Espera 204 No Content
    }

    @Test
    void deleteAsignatura_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        // Given
        Long asignaturaId = 99L;
        when(asignaturaService.deleteAsignatura(asignaturaId)).thenReturn(false); // Simula que no se encontró

        // When & Then
        mockMvc.perform(delete("/api/asignaturas/{id}", asignaturaId))
                .andExpect(status().isNotFound()); // Espera 404 Not Found
    }
}