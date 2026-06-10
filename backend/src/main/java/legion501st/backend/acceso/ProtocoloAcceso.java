package legion501st.backend.acceso;

import legion501st.backend.personas.Persona;

public interface ProtocoloAcceso {

    boolean aplica(Persona persona);

    // cada tipo de persona valida distinto
    boolean puedeIngresar(Persona persona, AutorizacionIngreso autorizacionIngreso);
}
