package legion501st.backend.tarea.dto;

import legion501st.backend.tarea.EstadoTarea;

public record TareaMantenimientoDto(
    Long id,
    Long reclamoId,
    String descripcion,
    EstadoTarea estado,
    Long responsableId,
    String responsableNombreCompleto
) {}
