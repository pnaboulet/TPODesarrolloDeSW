package legion501st.backend.reclamo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import legion501st.backend.reclamo.Prioridad;
import legion501st.backend.reclamo.TipoReclamo;

public record CrearReclamoDto(
    @NotNull(message = "El ID del residente no puede ser nulo")
    Long residenteId,

    @NotNull(message = "El tipo de reclamo no puede ser nulo")
    TipoReclamo tipoReclamo,

    @NotBlank(message = "La descripción no puede estar vacía")
    String descripcion,

    @NotNull(message = "La prioridad no puede ser nula")
    Prioridad prioridad
) {}
