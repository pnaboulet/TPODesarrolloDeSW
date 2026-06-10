package legion501st.backend.acceso;

import legion501st.backend.personas.Persona;
import legion501st.backend.personas.Residente;
import org.springframework.stereotype.Component;

@Component
public class AccesoResidente implements ProtocoloAcceso {

    @Override
    public boolean aplica(Persona persona) {
        return persona instanceof Residente;
    }

    @Override
    public boolean puedeIngresar(Persona persona, AutorizacionIngreso autorizacionIngreso) {
        // si es residente registrado, puede ingresar
        return persona != null && persona.getId() != null;
    }
}
