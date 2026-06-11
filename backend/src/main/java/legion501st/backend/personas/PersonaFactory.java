package legion501st.backend.personas;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PersonaFactory {

    public Persona crearPersona(TipoPersona tipo, String nombre, String apellido, String dni, String email, Map<String, Object> atributosAdicionales) {
        return switch (tipo) {
            case RESIDENTE -> {
                Residente residente = new Residente();
                residente.setNombre(nombre);
                residente.setApellido(apellido);
                residente.setDni(dni);
                residente.setEmail(email);
                yield residente;
            }
            case PROVEEDOR -> {
                Proveedor proveedor = new Proveedor();
                proveedor.setNombre(nombre);
                proveedor.setApellido(apellido);
                proveedor.setDni(dni);
                proveedor.setEmail(email);
                if (atributosAdicionales != null && atributosAdicionales.containsKey("tipoServicio")) {
                    proveedor.setTipoServicio((String) atributosAdicionales.get("tipoServicio"));
                }
                yield proveedor;
            }
            case SEGURIDAD -> {
                PersonalSeguridad seg = new PersonalSeguridad();
                seg.setNombre(nombre);
                seg.setApellido(apellido);
                seg.setDni(dni);
                seg.setEmail(email);
                yield seg;
            }
            case MANTENIMIENTO -> {
                PersonalMantenimiento mant = new PersonalMantenimiento();
                mant.setNombre(nombre);
                mant.setApellido(apellido);
                mant.setDni(dni);
                mant.setEmail(email);
                yield mant;
            }
            case VISITANTE -> {
                Visitante vis = new Visitante();
                vis.setNombre(nombre);
                vis.setApellido(apellido);
                vis.setDni(dni);
                vis.setEmail(email);
                yield vis;
            }
            case ADMINISTRADOR -> {
                Administrador adm = new Administrador();
                adm.setNombre(nombre);
                adm.setApellido(apellido);
                adm.setDni(dni);
                adm.setEmail(email);
                yield adm;
            }
        };
    }
}
