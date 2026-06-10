package legion501st.backend.acceso.dto;

public class RegistrarIngresoRequest {

    // Visitante que se presenta en la entrada del barrio
    private Long visitanteId;

    // Guardia que registra el ingreso en portería
    private Long seguridadId;

    public RegistrarIngresoRequest() {
    }

    public RegistrarIngresoRequest(Long visitanteId, Long seguridadId) {
        this.visitanteId = visitanteId;
        this.seguridadId = seguridadId;
    }

    public Long getVisitanteId() {
        return visitanteId;
    }

    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
    }

    public Long getSeguridadId() {
        return seguridadId;
    }

    public void setSeguridadId(Long seguridadId) {
        this.seguridadId = seguridadId;
    }
}
