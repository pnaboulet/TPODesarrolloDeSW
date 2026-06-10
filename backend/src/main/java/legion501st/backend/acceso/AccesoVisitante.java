package legion501st.backend.acceso;

import legion501st.backend.personas.Persona;
import legion501st.backend.personas.Visitante;
import org.springframework.stereotype.Component;

@Component
public class AccesoVisitante implements ProtocoloAcceso {

    @Override
    public boolean aplica(Persona persona) {
        return persona instanceof Visitante;
    }

    @Override
    public boolean puedeIngresar(Persona persona, AutorizacionIngreso autorizacionIngreso) {
        // El visitante es el único que necesita autorización previa vigente
        return autorizacionIngreso != null && autorizacionIngreso.estaVigente();
    }
}
