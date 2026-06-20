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
        // El residente debe estar habilitado y su unidad funcional (si tiene una asignada) también debe estar habilitada
        return residente.isHabilitado() && 
               (residente.getUnidadFuncional() == null || residente.getUnidadFuncional().isHabilitada());
    }
}
