package legion501st.backend.acceso.dto;

public class RegistrarIngresoRequest {

    // En portería se busca al visitante por DNI, no por un id que el guardia no conoce
    private String visitanteDni;

    // Guardia que registra el ingreso en portería
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
