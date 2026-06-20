package legion501st.backend.acceso;

import legion501st.backend.personas.Persona;
import legion501st.backend.personas.Proveedor;
import org.springframework.stereotype.Component;

@Component
public class AccesoProveedor implements ProtocoloAcceso {

    @Override
    public boolean aplica(Persona persona) {
        return persona instanceof Proveedor;
    }

    @Override
    public boolean puedeIngresar(Persona persona, AutorizacionIngreso autorizacionIngreso) {
        if (persona == null || !persona.isHabilitado()) {
            return false;
        }

        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        java.time.DayOfWeek diaSemana = ahora.getDayOfWeek();
        java.time.LocalTime horaActual = ahora.toLocalTime();

        if (diaSemana == java.time.DayOfWeek.SUNDAY) {
            return false; // No se permite el ingreso los domingos
        } else if (diaSemana == java.time.DayOfWeek.SATURDAY) {
            // Sábados de 08:00 a 13:00
            return !horaActual.isBefore(java.time.LocalTime.of(8, 0)) && 
                   !horaActual.isAfter(java.time.LocalTime.of(13, 0));
        } else {
            // Lunes a Viernes de 08:00 a 20:00
            return !horaActual.isBefore(java.time.LocalTime.of(8, 0)) && 
                   !horaActual.isAfter(java.time.LocalTime.of(20, 0));
        }
    }
}
