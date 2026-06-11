package legion501st.backend.reclamo.dto;

import jakarta.validation.constraints.NotNull;
import legion501st.backend.reclamo.EstadoReclamo;

public record ActualizarEstadoDto(
    @NotNull(message = "El nuevo estado no puede ser nulo")
    EstadoReclamo nuevoEstado,
    
    String observacion
) {}
