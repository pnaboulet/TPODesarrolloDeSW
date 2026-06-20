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
        if (!(persona instanceof Residente residente)) {
            return false;
        }
        if (!residente.isHabilitado()) {
            throw new IllegalArgumentException("El residente se encuentra deshabilitado/bloqueado");
        }
        if (residente.getUnidadFuncional() != null && !residente.getUnidadFuncional().isHabilitada()) {
            throw new IllegalArgumentException("La unidad funcional del residente se encuentra suspendida o deshabilitada");
        }
        return true;
    }
}
