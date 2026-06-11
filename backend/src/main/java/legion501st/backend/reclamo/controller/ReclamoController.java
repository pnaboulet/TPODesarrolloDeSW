package legion501st.backend.reclamo.controller;

import jakarta.validation.Valid;
import legion501st.backend.reclamo.dto.ActualizarEstadoDto;
import legion501st.backend.reclamo.dto.AsignarResponsableDto;
import legion501st.backend.reclamo.dto.CrearReclamoDto;
import legion501st.backend.reclamo.dto.ReclamoDto;
import legion501st.backend.reclamo.dto.HistorialEstadoDto;
import legion501st.backend.reclamo.service.ReclamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReclamoController {

    private final ReclamoService reclamoService;

    public ReclamoController(ReclamoService reclamoService) {
        this.reclamoService = reclamoService;
    }

    @PostMapping("/reclamos")
    public ResponseEntity<ReclamoDto> crearReclamo(@Valid @RequestBody CrearReclamoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reclamoService.crearReclamo(dto));
    }

    @PutMapping("/reclamos/{id}/estado")
    public ResponseEntity<ReclamoDto> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoDto dto) {
        return ResponseEntity.ok(reclamoService.cambiarEstado(id, dto.nuevoEstado(), dto.observacion()));
    }

    @PutMapping("/reclamos/{id}/asignar")
    public ResponseEntity<ReclamoDto> asignarResponsable(
            @PathVariable Long id,
            @Valid @RequestBody AsignarResponsableDto dto) {
        return ResponseEntity.ok(reclamoService.asignarResponsable(id, dto.responsableId()));
    }

    @GetMapping("/reclamos")
    public ResponseEntity<List<ReclamoDto>> listarReclamos(
            @RequestParam(required = false) Long residenteId,
            @RequestParam(required = false) Long responsableId) {
        if (residenteId != null) {
            return ResponseEntity.ok(reclamoService.listarPorResidente(residenteId));
        }
        if (responsableId != null) {
            return ResponseEntity.ok(reclamoService.listarPorResponsable(responsableId));
        }
        return ResponseEntity.ok(reclamoService.listarReclamos());
    }

    @GetMapping("/reclamos/{id}")
    public ResponseEntity<ReclamoDto> obtenerReclamo(@PathVariable Long id) {
        return ResponseEntity.ok(reclamoService.obtenerReclamoPorId(id));
    }

    @GetMapping("/reclamos/{id}/historial")
    public ResponseEntity<List<HistorialEstadoDto>> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(reclamoService.obtenerHistorial(id));
    }
}
