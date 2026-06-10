package legion501st.backend.reclamo;

import legion501st.backend.personas.Persona;

public interface EstadoReclamoState {
    void asignarResponsable(Reclamo reclamo, Persona responsable);
    void resolver(Reclamo reclamo, String observacion);
    void cerrar(Reclamo reclamo);
    void cancelar(Reclamo reclamo);
    EstadoReclamo getEstado();
}
