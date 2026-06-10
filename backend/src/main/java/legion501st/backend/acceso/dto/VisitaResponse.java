package legion501st.backend.acceso.dto;

import legion501st.backend.acceso.EstadoVisita;
import legion501st.backend.acceso.Visita;

import java.time.LocalDateTime;

public class VisitaResponse {

    private Long id;
    private Long visitanteId;
    private String nombreVisitante;
    private String dniVisitante;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaSalida;
    private EstadoVisita estado;

    public VisitaResponse() {
    }

    public VisitaResponse(Long id, Long visitanteId, String nombreVisitante, String dniVisitante, LocalDateTime fechaIngreso, LocalDateTime fechaSalida, EstadoVisita estado) {
        this.id = id;
        this.visitanteId = visitanteId;
        this.nombreVisitante = nombreVisitante;
        this.dniVisitante = dniVisitante;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
    }

    public static VisitaResponse desdeEntidad(Visita visita) {
        // Para devolver historial no hace falta mandar toda la entidad Visitante, solo datos útiles
        Long visitanteId = visita.getVisitante() != null ? visita.getVisitante().getId() : null;
        String nombre = visita.getVisitante() != null ? visita.getVisitante().getNombre() : null;
        String dni = visita.getVisitante() != null ? visita.getVisitante().getDni() : null;

        return new VisitaResponse(
                visita.getId(),
                visitanteId,
                nombre,
                dni,
                visita.getFechaIngreso(),
                visita.getFechaSalida(),
                visita.getEstado()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVisitanteId() {
        return visitanteId;
    }

    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
    }

    public String getNombreVisitante() {
        return nombreVisitante;
    }

    public void setNombreVisitante(String nombreVisitante) {
        this.nombreVisitante = nombreVisitante;
    }

    public String getDniVisitante() {
        return dniVisitante;
    }

    public void setDniVisitante(String dniVisitante) {
        this.dniVisitante = dniVisitante;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public EstadoVisita getEstado() {
        return estado;
    }

    public void setEstado(EstadoVisita estado) {
        this.estado = estado;
    }
}
