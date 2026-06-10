package legion501st.backend.notificaciones;

import legion501st.backend.reclamo.Reclamo;
import legion501st.backend.reclamo.ReclamoEstadoChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailObserver implements NotificacionObserver {

    @Async
    @EventListener
    public void onReclamoEstadoChanged(ReclamoEstadoChangedEvent event) {
        Reclamo reclamo = event.getReclamo();
        String residenteEmail = reclamo.getResidente().getEmail();
        System.out.println("[Email Notification] Reclamo #" + reclamo.getId() + 
                " cambió su estado de " + event.getEstadoAnterior() + 
                " a " + event.getEstadoNuevo() + 
                ". Notificación enviada al email: " + residenteEmail);
    }
}
