package legion501st.backend.tarea;

public class CanceladaTareaState implements EstadoTareaState {

    @Override
    public void iniciar(TareaMantenimiento tarea) {
        throw new IllegalStateException("No se puede iniciar una tarea cancelada");
    }

    @Override
    public void completar(TareaMantenimiento tarea) {
        throw new IllegalStateException("No se puede completar una tarea cancelada");
    }

    @Override
    public void cancelar(TareaMantenimiento tarea) {
        throw new IllegalStateException("La tarea ya se encuentra cancelada");
    }

    @Override
    public EstadoTarea getEstado() {
        return EstadoTarea.CANCELADA;
    }
}
