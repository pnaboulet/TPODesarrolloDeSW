package legion501st.backend.reclamo;

import org.springframework.context.ApplicationEvent;

public class ReclamoEstadoChangedEvent extends ApplicationEvent {
    private final Reclamo reclamo;
    private final EstadoReclamo estadoAnterior;
    private final EstadoReclamo estadoNuevo;

    public ReclamoEstadoChangedEvent(Object source, Reclamo reclamo, EstadoReclamo estadoAnterior, EstadoReclamo estadoNuevo) {
        super(source);
        this.reclamo = reclamo;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
    }

    public Reclamo getReclamo() {
        return reclamo;
    }

    public EstadoReclamo getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoReclamo getEstadoNuevo() {
        return estadoNuevo;
    }
}
