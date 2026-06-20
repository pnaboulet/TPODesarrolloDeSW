package legion501st.backend.acceso.dto;

import legion501st.backend.acceso.AutorizacionIngreso;

import java.time.LocalDateTime;

public class AutorizacionResponse {

    private Long id;
    private Long residenteId;
    private Long visitanteId;
    private String visitanteNombre;
    private String visitanteDni;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    private boolean utilizada;

    public AutorizacionResponse() {
    }

    public AutorizacionResponse(Long id, Long residenteId, Long visitanteId, String visitanteNombre, String visitanteDni, LocalDateTime fechaDesde, LocalDateTime fechaHasta, boolean utilizada) {
        this.id = id;
        this.residenteId = residenteId;
        this.visitanteId = visitanteId;
        this.visitanteNombre = visitanteNombre;
        this.visitanteDni = visitanteDni;
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

        String visitanteNombre = null;
        String visitanteDni = null;
        if (autorizacion.getVisitante() != null) {
            visitanteNombre = autorizacion.getVisitante().getNombre() + " " + autorizacion.getVisitante().getApellido();
            visitanteDni = autorizacion.getVisitante().getDni();
        }

        return new AutorizacionResponse(
                autorizacion.getId(),
                residenteId,
                visitanteId,
                visitanteNombre,
                visitanteDni,
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

    public boolean isUtilizada() {
        return utilizada;
    }

    public void setUtilizada(boolean utilizada) {
        this.utilizada = utilizada;
    }
}
