package legion501st.backend.acceso;

import legion501st.backend.barrio.UnidadFuncional;
import legion501st.backend.personas.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AccesoStrategiesTest {

    private AccesoResidente accesoResidente;
    private AccesoVisitante accesoVisitante;
    private AccesoProveedor accesoProveedor;
    private AccesoPersonalMantenimiento accesoPersonalMantenimiento;
    private AccesoPersonalSeguridad accesoPersonalSeguridad;
    private AccesoAdministrador accesoAdministrador;

    @BeforeEach
    void setUp() {
        accesoResidente = new AccesoResidente();
        accesoVisitante = new AccesoVisitante();
        accesoProveedor = new AccesoProveedor();
        accesoPersonalMantenimiento = new AccesoPersonalMantenimiento();
        accesoPersonalSeguridad = new AccesoPersonalSeguridad();
        accesoAdministrador = new AccesoAdministrador();
    }

    @Test
    void testAccesoResidente() {
        Residente residente = new Residente();
        residente.setHabilitado(true);

        UnidadFuncional uf = new UnidadFuncional();
        uf.setHabilitada(true);
        residente.setUnidadFuncional(uf);

        // Caso exitoso
        assertTrue(accesoResidente.puedeIngresar(residente, null));

        // Caso residente deshabilitado
        residente.setHabilitado(false);
        assertThrows(IllegalArgumentException.class, () -> accesoResidente.puedeIngresar(residente, null));

        // Caso residente habilitado pero unidad funcional deshabilitada
        residente.setHabilitado(true);
        uf.setHabilitada(false);
        assertThrows(IllegalArgumentException.class, () -> accesoResidente.puedeIngresar(residente, null));
    }

    @Test
    void testAccesoVisitante() {
        Visitante visitante = new Visitante();
        visitante.setHabilitado(true);

        // Sin autorizacion
        assertThrows(IllegalArgumentException.class, () -> accesoVisitante.puedeIngresar(visitante, null));

        // Con autorizacion vigente
        AutorizacionIngreso autorizacion = new AutorizacionIngreso();
        autorizacion.setFechaDesde(LocalDateTime.now().minusHours(1));
        autorizacion.setFechaHasta(LocalDateTime.now().plusHours(1));
        autorizacion.setUtilizada(false);

        assertTrue(accesoVisitante.puedeIngresar(visitante, autorizacion));

        // Visitante deshabilitado
        visitante.setHabilitado(false);
        assertThrows(IllegalArgumentException.class, () -> accesoVisitante.puedeIngresar(visitante, autorizacion));
    }

    @Test
    void testAccesoProveedor() {
        Proveedor proveedor = new Proveedor();
        proveedor.setHabilitado(true);

        // Si está deshabilitado, debe dar excepción
        proveedor.setHabilitado(false);
        assertThrows(IllegalArgumentException.class, () -> accesoProveedor.puedeIngresar(proveedor, null));
    }

    @Test
    void testAccesoPersonalMantenimiento() {
        PersonalMantenimiento empleado = new PersonalMantenimiento();
        empleado.setHabilitado(true);

        // Si está deshabilitado, debe dar excepción
        empleado.setHabilitado(false);
        assertThrows(IllegalArgumentException.class, () -> accesoPersonalMantenimiento.puedeIngresar(empleado, null));
    }

    @Test
    void testAccesoPersonalSeguridad() {
        PersonalSeguridad guardia = new PersonalSeguridad();
        guardia.setHabilitado(true);

        assertTrue(accesoPersonalSeguridad.puedeIngresar(guardia, null));

        guardia.setHabilitado(false);
        assertThrows(IllegalArgumentException.class, () -> accesoPersonalSeguridad.puedeIngresar(guardia, null));
    }

    @Test
    void testAccesoAdministrador() {
        Administrador admin = new Administrador();
        admin.setHabilitado(true);

        assertTrue(accesoAdministrador.puedeIngresar(admin, null));

        admin.setHabilitado(false);
        assertThrows(IllegalArgumentException.class, () -> accesoAdministrador.puedeIngresar(admin, null));
    }
}
