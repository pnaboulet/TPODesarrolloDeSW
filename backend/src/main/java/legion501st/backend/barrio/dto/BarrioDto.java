package legion501st.backend.barrio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BarrioDto(
    Long id,
    
    @NotBlank(message = "El nombre del barrio no puede estar vacío")
    @Size(max = 100, message = "El nombre del barrio no puede superar los 100 caracteres")
    String nombre,
    
    @NotBlank(message = "La dirección del barrio no puede estar vacía")
    @Size(max = 150, message = "La dirección del barrio no puede superar los 150 caracteres")
    String direccion
) {}
