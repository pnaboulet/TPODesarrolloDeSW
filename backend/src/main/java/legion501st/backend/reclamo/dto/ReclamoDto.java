package legion501st.backend.reclamo.dto;

import legion501st.backend.reclamo.EstadoReclamo;
import legion501st.backend.reclamo.Prioridad;
import legion501st.backend.reclamo.TipoReclamo;

import java.time.LocalDateTime;

public record ReclamoDto(
    Long id,
    Long residenteId,
    String residenteNombreCompleto,
    TipoReclamo tipoReclamo,
    String descripcion,
    Prioridad prioridad,
    EstadoReclamo estado,
    LocalDateTime fechaCreacion,
    Long responsableId,
    String responsableNombreCompleto
) {}
