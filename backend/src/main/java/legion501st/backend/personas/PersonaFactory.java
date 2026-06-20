package legion501st.backend.personas;

import java.util.Map;

public interface PersonaFactory {
    Persona crearPersona(TipoPersona tipo, String nombre, String apellido, String dni, String email, Map<String, Object> atributosAdicionales);
}
