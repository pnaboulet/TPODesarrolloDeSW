package legion501st.backend.acceso;

import legion501st.backend.personas.Persona;
import legion501st.backend.personas.PersonalMantenimiento;
import org.springframework.stereotype.Component;

@Component
public class AccesoPersonalMantenimiento implements ProtocoloAcceso {

    @Override
    public boolean aplica(Persona persona) {
        return persona instanceof PersonalMantenimiento;
    }

    @Override
    public boolean puedeIngresar(Persona persona, AutorizacionIngreso autorizacionIngreso) {
        if (persona == null) {
            throw new IllegalArgumentException("Persona no encontrada");
        }
        if (!persona.isHabilitado()) {
            throw new IllegalArgumentException("El personal de mantenimiento se encuentra deshabilitado/bloqueado");
        }

        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        java.time.DayOfWeek diaSemana = ahora.getDayOfWeek();
        java.time.LocalTime horaActual = ahora.toLocalTime();

        if (diaSemana == java.time.DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("El personal de mantenimiento de rutina no trabaja los domingos");
        }

        // Lunes a Sábado de 07:00 a 20:00
        boolean enHorario = !horaActual.isBefore(java.time.LocalTime.of(7, 0)) && 
                           !horaActual.isAfter(java.time.LocalTime.of(20, 0));
        if (!enHorario) {
            throw new IllegalArgumentException("El horario de ingreso para personal de mantenimiento de lunes a sábados es de 07:00 a 20:00 hs. (Hora actual: " + horaActual.toString().substring(0, 5) + ")");
        }
        return true;
    }
}
