package legion501st.backend.acceso.dto;

import java.time.LocalDateTime;

public class CrearAutorizacionRequest {

    // Id del residente que está autorizando al visitante
    private Long residenteId;

    // Id del visitante que va a poder ingresar
    private Long visitanteId;

    // Rango de validez de la autorización
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;

    public CrearAutorizacionRequest() {
    }

    public CrearAutorizacionRequest(Long residenteId, Long visitanteId, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        this.residenteId = residenteId;
        this.visitanteId = visitanteId;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    public Long getResidenteId() {
        return residenteId;
    }

    public void setResidenteId(Long residenteId) {
        this.residenteId = residenteId;
    }

    public Long getVisitanteId() {
        return visitanteId;
    }

    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
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
