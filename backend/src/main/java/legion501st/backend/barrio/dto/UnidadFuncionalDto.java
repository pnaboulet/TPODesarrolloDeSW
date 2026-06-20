package legion501st.backend.barrio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import legion501st.backend.barrio.TipoUnidad;

public record UnidadFuncionalDto(
    Long id,
    
    @NotNull(message = "El ID del barrio no puede ser nulo")
    Long barrioId,
    
    @NotBlank(message = "El identificador no puede estar vacío")
    @Size(max = 20, message = "El identificador no puede superar los 20 caracteres")
    String identificador,
    
    @NotNull(message = "El tipo de unidad no puede ser nulo")
    TipoUnidad tipoUnidad,

    Boolean habilitada
) {}
