package legion501st.backend.tarea;

public interface EstadoTareaState {
    void iniciar(TareaMantenimiento tarea);
    void completar(TareaMantenimiento tarea);
    void cancelar(TareaMantenimiento tarea);
    EstadoTarea getEstado();
}
