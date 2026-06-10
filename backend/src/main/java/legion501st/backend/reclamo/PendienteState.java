package legion501st.backend.reclamo;

import legion501st.backend.personas.Persona;

public class PendienteState implements EstadoReclamoState {

    @Override
    public void asignarResponsable(Reclamo reclamo, Persona responsable) {
        reclamo.setResponsable(responsable);
        reclamo.setEstado(EstadoReclamo.EN_PROCESO);
    }

    @Override
    public void resolver(Reclamo reclamo, String observacion) {
        throw new IllegalStateException("No se puede resolver un reclamo en estado PENDIENTE");
    }

    @Override
    public void cerrar(Reclamo reclamo) {
        throw new IllegalStateException("No se puede cerrar un reclamo en estado PENDIENTE");
    }

    @Override
    public void cancelar(Reclamo reclamo) {
        reclamo.setEstado(EstadoReclamo.CANCELADO);
    }

    @Override
    public EstadoReclamo getEstado() {
        return EstadoReclamo.PENDIENTE;
    }
}
