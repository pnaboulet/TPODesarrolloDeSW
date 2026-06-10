package legion501st.backend.reclamo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado_reclamos")
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El reclamo no puede ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamo_id", nullable = false)
    private Reclamo reclamo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 20)
    private EstadoReclamo estadoAnterior;

    @NotNull(message = "El estado nuevo no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false, length = 20)
    private EstadoReclamo estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    public HistorialEstado() {
        this.fechaCambio = LocalDateTime.now();
    }

    public HistorialEstado(Reclamo reclamo, EstadoReclamo estadoAnterior, EstadoReclamo estadoNuevo, String observacion) {
        this.reclamo = reclamo;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.observacion = observacion;
        this.fechaCambio = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaCambio == null) {
            this.fechaCambio = LocalDateTime.now();
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reclamo getReclamo() {
        return reclamo;
    }

    public void setReclamo(Reclamo reclamo) {
        this.reclamo = reclamo;
    }

    public EstadoReclamo getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadoReclamo estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoReclamo getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(EstadoReclamo estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
