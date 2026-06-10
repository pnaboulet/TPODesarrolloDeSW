package legion501st.backend.acceso;

import legion501st.backend.personas.Residente;
import legion501st.backend.personas.Visitante;

import java.time.LocalDateTime;

public class AutorizacionIngreso {

    private int id;
    private Residente residenteAutoriza;
    private Visitante visitante;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    private boolean utilizada;

    public AutorizacionIngreso() {
        this.utilizada = false;
    }

    public AutorizacionIngreso(int id, Residente residenteAutoriza, Visitante visitante,
                               LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        this.id = id;
        this.residenteAutoriza = residenteAutoriza;
        this.visitante = visitante;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.utilizada = false;
    }

    // Revisa si la autorización todavía sirve para que el visitante pueda entrar
    public boolean estaVigente() {
        LocalDateTime ahora = LocalDateTime.now();
        return fechaDesde != null
                && fechaHasta != null
                && !utilizada
                && !ahora.isBefore(fechaDesde)
                && !ahora.isAfter(fechaHasta);
    }

    public void marcarComoUtilizada() {
        this.utilizada = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Residente getResidenteAutoriza() {
        return residenteAutoriza;
    }

    public void setResidenteAutoriza(Residente residenteAutoriza) {
        this.residenteAutoriza = residenteAutoriza;
    }

    public Visitante getVisitante() {
        return visitante;
    }

    public void setVisitante(Visitante visitante) {
        this.visitante = visitante;
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
