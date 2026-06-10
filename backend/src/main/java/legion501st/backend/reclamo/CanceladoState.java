package legion501st.backend.reclamo;

import legion501st.backend.personas.Persona;

public class CanceladoState implements EstadoReclamoState {

    @Override
    public void asignarResponsable(Reclamo reclamo, Persona responsable) {
        throw new IllegalStateException("No se puede asignar un responsable a un reclamo cancelado");
    }

    @Override
    public void resolver(Reclamo reclamo, String observacion) {
        throw new IllegalStateException("No se puede resolver un reclamo cancelado");
    }

    @Override
    public void cerrar(Reclamo reclamo) {
        throw new IllegalStateException("No se puede cerrar un reclamo cancelado");
    }

    @Override
    public void cancelar(Reclamo reclamo) {
        throw new IllegalStateException("El reclamo ya se encuentra cancelado");
    }

    @Override
    public EstadoReclamo getEstado() {
        return EstadoReclamo.CANCELADO;
    }
}
