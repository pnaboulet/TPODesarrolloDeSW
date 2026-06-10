package legion501st.backend.tarea;

public class CompletadaTareaState implements EstadoTareaState {

    @Override
    public void iniciar(TareaMantenimiento tarea) {
        throw new IllegalStateException("No se puede iniciar una tarea ya completada");
    }

    @Override
    public void completar(TareaMantenimiento tarea) {
        throw new IllegalStateException("La tarea ya se encuentra completada");
    }

    @Override
    public void cancelar(TareaMantenimiento tarea) {
        throw new IllegalStateException("No se puede cancelar una tarea ya completada");
    }

    @Override
    public EstadoTarea getEstado() {
        return EstadoTarea.COMPLETADA;
    }
}
