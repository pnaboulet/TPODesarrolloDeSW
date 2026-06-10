package legion501st.backend.reclamo;

import legion501st.backend.personas.Persona;

public class EnProcesoState implements EstadoReclamoState {

    @Override
    public void asignarResponsable(Reclamo reclamo, Persona responsable) {
        reclamo.setResponsable(responsable);
    }

    @Override
    public void resolver(Reclamo reclamo, String observacion) {
        // EnProceso can be resolved.
        reclamo.setEstado(EstadoReclamo.RESUELTO);
    }

    @Override
    public void cerrar(Reclamo reclamo) {
        throw new IllegalStateException("No se puede cerrar un reclamo en estado EN_PROCESO sin haber sido resuelto primero");
    }

    @Override
    public void cancelar(Reclamo reclamo) {
        reclamo.setEstado(EstadoReclamo.CANCELADO);
    }

    @Override
    public EstadoReclamo getEstado() {
        return EstadoReclamo.EN_PROCESO;
    }
}
