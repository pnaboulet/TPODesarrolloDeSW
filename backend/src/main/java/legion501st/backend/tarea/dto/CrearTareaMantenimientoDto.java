package legion501st.backend.tarea.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearTareaMantenimientoDto(
    Long reclamoId,

    @NotBlank(message = "La descripción no puede estar vacía")
    String descripcion,

    @NotNull(message = "El ID del responsable no puede ser nulo")
    Long responsableId
) {}
