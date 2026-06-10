package legion501st.backend.acceso.dto;

public class RegistrarSalidaRequest {

    // Con este id buscamos la visita que quedó EN_CURSO y la cerramos
    private Long visitanteId;

    public RegistrarSalidaRequest() {
    }

    public RegistrarSalidaRequest(Long visitanteId) {
        this.visitanteId = visitanteId;
    }

    public Long getVisitanteId() {
        return visitanteId;
    }

    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
    }
}
