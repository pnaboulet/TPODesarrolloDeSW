package legion501st.backend.acceso.controller;

import jakarta.validation.Valid;
import legion501st.backend.acceso.AutorizacionIngreso;
import legion501st.backend.acceso.Visita;
import legion501st.backend.acceso.dto.AutorizacionResponse;
import legion501st.backend.acceso.dto.CrearAutorizacionRequest;
import legion501st.backend.acceso.dto.RegistrarIngresoRequest;
import legion501st.backend.acceso.dto.RegistrarSalidaRequest;
import legion501st.backend.acceso.dto.VisitaResponse;
import legion501st.backend.facade.GestionBarrioFacade;
import legion501st.backend.acceso.service.AccesoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@org.springframework.transaction.annotation.Transactional
public class AccesoController {

    private final AccesoService accesoService;
    private final GestionBarrioFacade barrioFacade;

    public AccesoController(AccesoService accesoService, GestionBarrioFacade barrioFacade) {
        this.accesoService = accesoService;
        this.barrioFacade = barrioFacade;
    }

    @PostMapping("/autorizaciones")
    public ResponseEntity<AutorizacionResponse> crearAutorizacion(@Valid @RequestBody CrearAutorizacionRequest request) {
        // El controller solo recibe datos y delega la lógica al service
        AutorizacionIngreso autorizacion = accesoService.crearAutorizacion(
                request.getResidenteId(),
                request.getVisitanteNombre(),
                request.getVisitanteDni(),
                request.getFechaDesde(),
                request.getFechaHasta()
        );

        // Devolvemos un DTO para no exponer directamente la entidad completa
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AutorizacionResponse.desdeEntidad(autorizacion));
    }

    @GetMapping("/autorizaciones")
    public ResponseEntity<List<AutorizacionResponse>> listarAutorizaciones(@RequestParam(required = false) Long residenteId) {
        List<AutorizacionIngreso> auts;
        if (residenteId != null) {
            auts = accesoService.listarAutorizacionesPorResidente(residenteId);
        } else {
            auts = accesoService.listarAutorizaciones();
        }
        List<AutorizacionResponse> response = auts.stream()
                .map(AutorizacionResponse::desdeEntidad)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/autorizaciones/{id}")
    public ResponseEntity<Void> revocarAutorizacion(@PathVariable Long id) {
        accesoService.revocarAutorizacion(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/visitas/ingreso")
    public ResponseEntity<VisitaResponse> registrarIngreso(@Valid @RequestBody RegistrarIngresoRequest request) {
        // Seguridad registra el ingreso delegando a la fachada
        Visita visita = barrioFacade.registrarIngresoVisitante(
                request.getVisitanteDni(),
                request.getSeguridadId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(VisitaResponse.desdeEntidad(visita));
    }

    @PostMapping("/visitas/salida")
    public ResponseEntity<VisitaResponse> registrarSalida(@RequestBody RegistrarSalidaRequest request) {
        // Registra la salida a través de la fachada
        Visita visita = barrioFacade.registrarSalidaVisitante(request.getVisitanteId());

        return ResponseEntity.ok(VisitaResponse.desdeEntidad(visita));
    }

    @GetMapping("/visitas")
    public ResponseEntity<List<VisitaResponse>> listarVisitas() {
        // Se muestra el historial de visitas con una respuesta simple para la API
        List<VisitaResponse> visitas = accesoService.listarVisitas()
                .stream()
                .map(VisitaResponse::desdeEntidad)
                .toList();

        return ResponseEntity.ok(visitas);
    }
}
