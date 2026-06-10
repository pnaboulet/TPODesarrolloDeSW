package legion501st.backend.notificaciones;

import legion501st.backend.reclamo.Reclamo;
import legion501st.backend.reclamo.ReclamoEstadoChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SmsObserver implements NotificacionObserver {

    @Async
    @EventListener
    public void onReclamoEstadoChanged(ReclamoEstadoChangedEvent event) {
        Reclamo reclamo = event.getReclamo();
        System.out.println("[SMS Notification] Reclamo #" + reclamo.getId() + 
                " pasó de " + event.getEstadoAnterior() + 
                " a " + event.getEstadoNuevo() + ".");
    }
}
