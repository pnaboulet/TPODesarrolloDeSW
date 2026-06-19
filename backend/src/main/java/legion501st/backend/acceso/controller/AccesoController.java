package legion501st.backend.acceso.controller;

import jakarta.validation.Valid;
import legion501st.backend.acceso.AutorizacionIngreso;
import legion501st.backend.acceso.Visita;
import legion501st.backend.acceso.dto.AutorizacionResponse;
import legion501st.backend.acceso.dto.CrearAutorizacionRequest;
import legion501st.backend.acceso.dto.RegistrarIngresoRequest;
import legion501st.backend.acceso.dto.RegistrarSalidaRequest;
import legion501st.backend.acceso.dto.VisitaResponse;
import legion501st.backend.acceso.service.AccesoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccesoController {

    private final AccesoService accesoService;

    public AccesoController(AccesoService accesoService) {
        this.accesoService = accesoService;
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

    @PostMapping("/visitas/ingreso")
    public ResponseEntity<VisitaResponse> registrarIngreso(@Valid @RequestBody RegistrarIngresoRequest request) {
        // Seguridad registra el ingreso y el service valida si el visitante puede entrar
        Visita visita = accesoService.registrarIngresoVisitante(
                request.getVisitanteDni(),
                request.getSeguridadId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(VisitaResponse.desdeEntidad(visita));
    }

    @PostMapping("/visitas/salida")
    public ResponseEntity<VisitaResponse> registrarSalida(@RequestBody RegistrarSalidaRequest request) {
        // Busca la visita que quedó EN_CURSO y la marca como finalizada
        Visita visita = accesoService.registrarSalidaVisitante(request.getVisitanteId());

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
