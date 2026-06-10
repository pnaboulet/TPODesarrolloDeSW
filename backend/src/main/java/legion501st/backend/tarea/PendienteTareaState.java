package legion501st.backend.tarea;

public class PendienteTareaState implements EstadoTareaState {

    @Override
    public void iniciar(TareaMantenimiento tarea) {
        tarea.setEstado(EstadoTarea.EN_PROCESO);
    }

    @Override
    public void completar(TareaMantenimiento tarea) {
        throw new IllegalStateException("No se puede completar una tarea en estado PENDIENTE");
    }

    @Override
    public void cancelar(TareaMantenimiento tarea) {
        tarea.setEstado(EstadoTarea.CANCELADA);
    }

    @Override
    public EstadoTarea getEstado() {
        return EstadoTarea.PENDIENTE;
    }
}
