package legion501st.backend.acceso;

import legion501st.backend.personas.Administrador;
import legion501st.backend.personas.Persona;
import org.springframework.stereotype.Component;

@Component
public class AccesoAdministrador implements ProtocoloAcceso {

    @Override
    public boolean aplica(Persona persona) {
        return persona instanceof Administrador;
    }

    @Override
    public boolean puedeIngresar(Persona persona, AutorizacionIngreso autorizacionIngreso) {
        return persona != null && persona.getId() != null;
    }
}
