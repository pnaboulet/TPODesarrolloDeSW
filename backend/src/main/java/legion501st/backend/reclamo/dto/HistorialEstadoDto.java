package legion501st.backend.reclamo.dto;

import legion501st.backend.reclamo.EstadoReclamo;
import java.time.LocalDateTime;

public record HistorialEstadoDto(
    Long id,
    Long reclamoId,
    EstadoReclamo estadoAnterior,
    EstadoReclamo estadoNuevo,
    LocalDateTime fechaCambio,
    String observacion
) {}
