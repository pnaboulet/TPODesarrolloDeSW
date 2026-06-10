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
        return persona != null && persona.getId() != null;
    }
}
