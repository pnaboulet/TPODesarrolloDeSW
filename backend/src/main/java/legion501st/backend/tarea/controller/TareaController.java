package legion501st.backend.tarea.controller;

import jakarta.validation.Valid;
import legion501st.backend.tarea.dto.CrearTareaMantenimientoDto;
import legion501st.backend.tarea.dto.TareaMantenimientoDto;
import legion501st.backend.tarea.service.TareaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @PostMapping("/tareas")
    public ResponseEntity<TareaMantenimientoDto> crearTarea(@Valid @RequestBody CrearTareaMantenimientoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaService.crearTarea(dto));
    }

    @PutMapping("/tareas/{id}/iniciar")
    public ResponseEntity<TareaMantenimientoDto> iniciarTarea(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.iniciarTarea(id));
    }

    @PutMapping("/tareas/{id}/completar")
    public ResponseEntity<TareaMantenimientoDto> completarTarea(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.completarTarea(id));
    }

    @PutMapping("/tareas/{id}/cancelar")
    public ResponseEntity<TareaMantenimientoDto> cancelarTarea(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.cancelarTarea(id));
    }

    @GetMapping("/tareas")
    public ResponseEntity<List<TareaMantenimientoDto>> listarTareas(
            @RequestParam(required = false) Long reclamoId,
            @RequestParam(required = false) Long responsableId) {
        if (reclamoId != null) {
            return ResponseEntity.ok(tareaService.listarPorReclamo(reclamoId));
        }
        if (responsableId != null) {
            return ResponseEntity.ok(tareaService.listarPorResponsable(responsableId));
        }
        return ResponseEntity.ok(tareaService.listarTareas());
    }

    @GetMapping("/tareas/{id}")
    public ResponseEntity<TareaMantenimientoDto> obtenerTarea(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.obtenerTareaPorId(id));
    }
}
