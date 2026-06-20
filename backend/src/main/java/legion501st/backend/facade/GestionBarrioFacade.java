package legion501st.backend.facade;

import legion501st.backend.acceso.Visita;
import legion501st.backend.acceso.service.AccesoService;
import legion501st.backend.reclamo.EstadoReclamo;
import legion501st.backend.reclamo.dto.CrearReclamoDto;
import legion501st.backend.reclamo.dto.ReclamoDto;
import legion501st.backend.reclamo.service.ReclamoService;
import org.springframework.stereotype.Component;

@Component
public class GestionBarrioFacade {

    private final ReclamoService reclamoService;
    private final AccesoService accesoService;

    public GestionBarrioFacade(ReclamoService reclamoService, AccesoService accesoService) {
        this.reclamoService = reclamoService;
        this.accesoService = accesoService;
    }

    public ReclamoDto crearReclamo(CrearReclamoDto dto) {
        // Enlaza la creación del reclamo y de forma indirecta las notificaciones a través de eventos
        return reclamoService.crearReclamo(dto);
    }

    public ReclamoDto asignarResponsable(Long reclamoId, Long responsableId) {
        return reclamoService.asignarResponsable(reclamoId, responsableId);
    }

    public ReclamoDto cambiarEstadoReclamo(Long reclamoId, EstadoReclamo nuevoEstado, String observacion) {
        return reclamoService.cambiarEstado(reclamoId, nuevoEstado, observacion);
    }

    public Visita registrarIngresoVisitante(String visitanteDni, Long seguridadId) {
        return accesoService.registrarIngresoVisitante(visitanteDni, seguridadId);
    }

    public Visita registrarIngresoVisitante(Long visitanteId, Long seguridadId) {
        return accesoService.registrarIngresoVisitante(visitanteId, seguridadId);
    }

    public Visita registrarSalidaVisitante(Long visitanteId) {
        return accesoService.registrarSalidaVisitante(visitanteId);
    }
}
