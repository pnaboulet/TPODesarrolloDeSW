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
        // El visitante debe estar habilitado en el sistema (no estar en lista negra/suspendido)
        // y contar con una autorización previa vigente
        return persona != null && 
               persona.isHabilitado() && 
               autorizacionIngreso != null && 
               autorizacionIngreso.estaVigente();
    }
}
