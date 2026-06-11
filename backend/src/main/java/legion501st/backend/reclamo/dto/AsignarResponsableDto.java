package legion501st.backend.reclamo.dto;

import jakarta.validation.constraints.NotNull;

public record AsignarResponsableDto(
    @NotNull(message = "El ID del responsable no puede ser nulo")
    Long responsableId
) {}
