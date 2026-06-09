package legion501st.backend.acceso;

import legion501st.backend.personas.PersonalSeguridad;
import legion501st.backend.personas.Visitante;

import java.time.LocalDateTime;

public class Visita {

    private int id;
    private Visitante visitante;
    private AutorizacionIngreso autorizacionIngreso;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaSalida;
    private EstadoVisita estado;
    private PersonalSeguridad registradoPor;

    public Visita() {
        this.estado = EstadoVisita.EN_CURSO;
    }

    public Visita(int id, Visitante visitante, AutorizacionIngreso autorizacionIngreso,
                  PersonalSeguridad registradoPor) {
        this.id = id;
        this.visitante = visitante;
        this.autorizacionIngreso = autorizacionIngreso;
        this.registradoPor = registradoPor;
        this.fechaIngreso = LocalDateTime.now();
        this.estado = EstadoVisita.EN_CURSO;

        if (autorizacionIngreso != null) {
            autorizacionIngreso.marcarComoUtilizada();
        }
    }

    // Caso de uso: seguridad registra la salida del visitante
    public void registrarSalida() {
        this.fechaSalida = LocalDateTime.now();
        this.estado = EstadoVisita.FINALIZADA;
    }

    public void marcarIrregular() {
        this.estado = EstadoVisita.IRREGULAR;
    }

    public boolean estaEnCurso() {
        return EstadoVisita.EN_CURSO.equals(estado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Visitante getVisitante() {
        return visitante;
    }

    public void setVisitante(Visitante visitante) {
        this.visitante = visitante;
    }

    public AutorizacionIngreso getAutorizacionIngreso() {
        return autorizacionIngreso;
    }

    public void setAutorizacionIngreso(AutorizacionIngreso autorizacionIngreso) {
        this.autorizacionIngreso = autorizacionIngreso;
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

    public PersonalSeguridad getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(PersonalSeguridad registradoPor) {
        this.registradoPor = registradoPor;
    }
}
