package legion501st.backend.notificaciones;

import legion501st.backend.notificaciones.repository.NotificacionRepository;
import legion501st.backend.reclamo.Reclamo;
import legion501st.backend.reclamo.ReclamoEstadoChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SmsObserver implements NotificacionObserver {

    private final NotificacionRepository notificacionRepository;

    public SmsObserver(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Async
    @EventListener
    public void onReclamoEstadoChanged(ReclamoEstadoChangedEvent event) {
        Reclamo reclamo = event.getReclamo();
        String msg = "[SMS Notification] Reclamo #" + reclamo.getId() + 
                " pasó de " + event.getEstadoAnterior() + 
                " a " + event.getEstadoNuevo() + ".";
        System.out.println(msg);
        
        notificacionRepository.save(new Notificacion(msg, reclamo.getResidente().getEmail()));
    }
}
