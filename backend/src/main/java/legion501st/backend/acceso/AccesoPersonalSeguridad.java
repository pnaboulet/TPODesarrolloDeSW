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
        // El personal de seguridad puede ingresar las 24 horas del día siempre y cuando esté habilitado
        return persona != null && persona.isHabilitado();
    }
}
