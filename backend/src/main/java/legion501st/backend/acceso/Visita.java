package legion501st.backend.acceso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import legion501st.backend.personas.PersonalSeguridad;
import legion501st.backend.personas.Visitante;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitas")
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Persona externa que ingresó al barrio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitante_id")
    private Visitante visitante;

    // Autorización que se usó para permitir el ingreso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autorizacion_id")
    private AutorizacionIngreso autorizacionIngreso;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;

    // Se guarda como texto en la base: EN_CURSO, FINALIZADA o IRREGULAR
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoVisita estado;

    // Guardia que registró el ingreso en portería
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por_seguridad_id")
    private PersonalSeguridad registradoPor;

    public Visita() {
    }

    public Visita(Visitante visitante, AutorizacionIngreso autorizacionIngreso,
                  PersonalSeguridad registradoPor) {
        this.visitante = visitante;
        this.autorizacionIngreso = autorizacionIngreso;
        this.registradoPor = registradoPor;
        this.fechaIngreso = LocalDateTime.now();
        this.estado = EstadoVisita.EN_CURSO;
    }

    public void registrarSalida() {
        // Cuando sale, guardamos la hora y cerramos la visita
        this.fechaSalida = LocalDateTime.now();
        this.estado = EstadoVisita.FINALIZADA;
    }

    public void marcarIrregular() {
        this.estado = EstadoVisita.IRREGULAR;
    }

    public boolean estaEnCurso() {
        return EstadoVisita.EN_CURSO.equals(this.estado);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
