package legion501st.backend.acceso.dto;

import java.time.LocalDateTime;

public class CrearAutorizacionRequest {

    // Residente que está creando la autorización desde su vista
    private Long residenteId;

    // Datos mínimos del visitante que informa el residente en el momento
    private String visitanteNombre;
    private String visitanteDni;

    // Rango en el que el visitante puede presentarse en portería
    private LocalDateTime fechaDesde;
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
