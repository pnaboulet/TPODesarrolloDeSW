package legion501st.backend.acceso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class CrearAutorizacionRequest {

    @NotNull(message = "Debe seleccionar un residente")
    private Long residenteId;

    @NotBlank(message = "Debe ingresar el nombre del visitante")
    private String visitanteNombre;

    @NotBlank(message = "Debe ingresar el DNI del visitante")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
    private String visitanteDni;

    @NotNull(message = "Debe ingresar la fecha de inicio")
    private LocalDateTime fechaDesde;

    @NotNull(message = "Debe ingresar la fecha de fin")
    private LocalDateTime fechaHasta;

    public CrearAutorizacionRequest() {
    }

    public CrearAutorizacionRequest(Long residenteId, String visitanteNombre, String visitanteDni,
                                    LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        this.residenteId = residenteId;
        this.visitanteNombre = visitanteNombre;
        this.visitanteDni = visitanteDni;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    public Long getResidenteId() {
        return residenteId;
    }

    public void setResidenteId(Long residenteId) {
        this.residenteId = residenteId;
    }

    public String getVisitanteNombre() {
        return visitanteNombre;
    }

    public void setVisitanteNombre(String visitanteNombre) {
        this.visitanteNombre = visitanteNombre;
    }

    public String getVisitanteDni() {
        return visitanteDni;
    }

    public void setVisitanteDni(String visitanteDni) {
        this.visitanteDni = visitanteDni;
    }

    public LocalDateTime getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDateTime fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDateTime getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDateTime fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}
