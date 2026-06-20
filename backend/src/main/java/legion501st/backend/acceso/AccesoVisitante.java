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
        if (persona == null) {
            throw new IllegalArgumentException("Persona no encontrada");
        }
        if (!persona.isHabilitado()) {
            throw new IllegalArgumentException("El visitante se encuentra deshabilitado/bloqueado");
        }
        if (autorizacionIngreso == null) {
            throw new IllegalArgumentException("El visitante no cuenta con una autorización previa registrada para este barrio");
        }
        if (!autorizacionIngreso.estaVigente()) {
            throw new IllegalArgumentException("La autorización del visitante ha expirado o aún no está vigente");
        }
        return true;
    }
}
