package legion501st.backend.tarea;

public class EnProcesoTareaState implements EstadoTareaState {

    @Override
    public void iniciar(TareaMantenimiento tarea) {
        // Already in process, do nothing or throw exception
    }

    @Override
    public void completar(TareaMantenimiento tarea) {
        tarea.setEstado(EstadoTarea.COMPLETADA);
    }

    @Override
    public void cancelar(TareaMantenimiento tarea) {
        tarea.setEstado(EstadoTarea.CANCELADA);
    }

    @Override
    public EstadoTarea getEstado() {
        return EstadoTarea.EN_PROCESO;
    }
}
