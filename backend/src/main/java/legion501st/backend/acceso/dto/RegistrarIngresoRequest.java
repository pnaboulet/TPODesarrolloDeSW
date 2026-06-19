package legion501st.backend.acceso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RegistrarIngresoRequest {

    @NotBlank(message = "Debe ingresar el DNI del visitante")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
    private String visitanteDni;

    @NotNull(message = "Debe seleccionar el guardia de seguridad")
    private Long seguridadId;

    public RegistrarIngresoRequest() {
    }

    public RegistrarIngresoRequest(String visitanteDni, Long seguridadId) {
        this.visitanteDni = visitanteDni;
        this.seguridadId = seguridadId;
    }

    public String getVisitanteDni() {
        return visitanteDni;
    }

    public void setVisitanteDni(String visitanteDni) {
        this.visitanteDni = visitanteDni;
    }

    public Long getSeguridadId() {
        return seguridadId;
    }

    public void setSeguridadId(Long seguridadId) {
        this.seguridadId = seguridadId;
    }
}
