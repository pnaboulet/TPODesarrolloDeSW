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
        if (persona == null) {
            throw new IllegalArgumentException("Persona no encontrada");
        }
        if (!persona.isHabilitado()) {
            throw new IllegalArgumentException("El proveedor se encuentra bloqueado/deshabilitado");
        }

        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        java.time.DayOfWeek diaSemana = ahora.getDayOfWeek();
        java.time.LocalTime horaActual = ahora.toLocalTime();

        if (diaSemana == java.time.DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("No se permite el ingreso de proveedores los domingos");
        } else if (diaSemana == java.time.DayOfWeek.SATURDAY) {
            // Sábados de 08:00 a 13:00
            boolean enHorario = !horaActual.isBefore(java.time.LocalTime.of(8, 0)) && 
                               !horaActual.isAfter(java.time.LocalTime.of(13, 0));
            if (!enHorario) {
                throw new IllegalArgumentException("El horario de ingreso para proveedores los sábados es de 08:00 a 13:00 hs. (Hora actual: " + horaActual.toString().substring(0, 5) + ")");
            }
        } else {
            // Lunes a Viernes de 08:00 a 20:00
            boolean enHorario = !horaActual.isBefore(java.time.LocalTime.of(8, 0)) && 
                               !horaActual.isAfter(java.time.LocalTime.of(20, 0));
            if (!enHorario) {
                throw new IllegalArgumentException("El horario de ingreso para proveedores de lunes a viernes es de 08:00 a 20:00 hs. (Hora actual: " + horaActual.toString().substring(0, 5) + ")");
            }
        }
        return true;
    }
}
