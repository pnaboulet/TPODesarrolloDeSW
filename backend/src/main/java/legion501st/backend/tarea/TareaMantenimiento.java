package legion501st.backend.tarea;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import legion501st.backend.personas.Persona;
import legion501st.backend.reclamo.Reclamo;

import lombok.Builder;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tareas_mantenimiento")
@Builder
@AllArgsConstructor
public class TareaMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamo_id")
    private Reclamo reclamo;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El estado no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoTarea estado = EstadoTarea.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Persona responsable;

    public TareaMantenimiento() {}

    @PrePersist
    protected void onCreate() {
        if (this.estado == null) {
            this.estado = EstadoTarea.PENDIENTE;
        }
    }

    // Vincular al patrón State
    public EstadoTareaState getEstadoState() {
        return switch (this.estado) {
            case PENDIENTE -> new PendienteTareaState();
            case EN_PROCESO -> new EnProcesoTareaState();
            case COMPLETADA -> new CompletadaTareaState();
            case CANCELADA -> new CanceladaTareaState();
        };
    }

    public void iniciar() {
        getEstadoState().iniciar(this);
    }

    public void completar() {
        getEstadoState().completar(this);
    }

    public void cancelar() {
        getEstadoState().cancelar(this);
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    public Persona getResponsable() {
        return responsable;
    }

    public void setResponsable(Persona responsable) {
        this.responsable = responsable;
    }
}
