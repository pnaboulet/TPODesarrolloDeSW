package legion501st.backend.reclamo;

import legion501st.backend.personas.Persona;

public class ResueltoState implements EstadoReclamoState {

    @Override
    public void asignarResponsable(Reclamo reclamo, Persona responsable) {
        throw new IllegalStateException("No se puede asignar un responsable a un reclamo ya resuelto");
    }

    @Override
    public void resolver(Reclamo reclamo, String observacion) {
        throw new IllegalStateException("El reclamo ya se encuentra resuelto");
    }

    @Override
    public void cerrar(Reclamo reclamo) {
        reclamo.setEstado(EstadoReclamo.CERRADO);
    }

    @Override
    public void cancelar(Reclamo reclamo) {
        throw new IllegalStateException("No se puede cancelar un reclamo ya resuelto");
    }

    @Override
    public EstadoReclamo getEstado() {
        return EstadoReclamo.RESUELTO;
    }
}
