package legion501st.backend.acceso.dto;

import legion501st.backend.acceso.AutorizacionIngreso;

import java.time.LocalDateTime;

public class AutorizacionResponse {

    private Long id;
    private Long residenteId;
    private Long visitanteId;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    private boolean utilizada;

    public AutorizacionResponse() {
    }

    public AutorizacionResponse(Long id, Long residenteId, Long visitanteId, LocalDateTime fechaDesde, LocalDateTime fechaHasta, boolean utilizada) {
        this.id = id;
        this.residenteId = residenteId;
        this.visitanteId = visitanteId;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.utilizada = utilizada;
    }

    public static AutorizacionResponse desdeEntidad(AutorizacionIngreso autorizacion) {
        // Este método transforma la entidad en una respuesta más simple para la API
        Long residenteId = autorizacion.getResidenteAutoriza() != null
                ? autorizacion.getResidenteAutoriza().getId()
                : null;

        Long visitanteId = autorizacion.getVisitante() != null
                ? autorizacion.getVisitante().getId()
                : null;

        return new AutorizacionResponse(
                autorizacion.getId(),
                residenteId,
                visitanteId,
                autorizacion.getFechaDesde(),
                autorizacion.getFechaHasta(),
                autorizacion.isUtilizada()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isUtilizada() {
        return utilizada;
    }

    public void setUtilizada(boolean utilizada) {
        this.utilizada = utilizada;
    }
}
