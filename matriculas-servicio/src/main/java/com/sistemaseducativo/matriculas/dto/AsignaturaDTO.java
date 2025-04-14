package com.sistemaseducativo.matriculas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaDTO {

    // Campos que coinciden con la entidad Asignatura del otro servicio
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer creditos;

}