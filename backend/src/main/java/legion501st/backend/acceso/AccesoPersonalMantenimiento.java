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
        return persona != null && persona.getId() != null;
    }
}
