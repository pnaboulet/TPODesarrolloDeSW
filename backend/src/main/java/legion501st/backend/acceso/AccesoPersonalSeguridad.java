package legion501st.backend.acceso;

import legion501st.backend.personas.Persona;
import legion501st.backend.personas.PersonalSeguridad;
import org.springframework.stereotype.Component;

@Component
public class AccesoPersonalSeguridad implements ProtocoloAcceso {

    @Override
    public boolean aplica(Persona persona) {
        return persona instanceof PersonalSeguridad;
    }

    @Override
    public boolean puedeIngresar(Persona persona, AutorizacionIngreso autorizacionIngreso) {
        if (persona == null) {
            throw new IllegalArgumentException("Persona no encontrada");
        }
        if (!persona.isHabilitado()) {
            throw new IllegalArgumentException("El personal de seguridad se encuentra deshabilitado/bloqueado");
        }
        return true;
    }
}
