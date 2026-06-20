package legion501st.backend.barrio.controller;

import jakarta.validation.Valid;
import legion501st.backend.barrio.dto.BarrioDto;
import legion501st.backend.barrio.dto.UnidadFuncionalDto;
import legion501st.backend.barrio.service.BarrioService;
import legion501st.backend.barrio.service.UnidadFuncionalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BarrioController {

    private final BarrioService barrioService;
    private final UnidadFuncionalService unidadFuncionalService;

    public BarrioController(BarrioService barrioService, UnidadFuncionalService unidadFuncionalService) {
        this.barrioService = barrioService;
        this.unidadFuncionalService = unidadFuncionalService;
    }

    // --- BARRIOS ---

    @PostMapping("/barrios")
    public ResponseEntity<BarrioDto> crearBarrio(@Valid @RequestBody BarrioDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(barrioService.crearBarrio(dto));
    }

    @GetMapping("/barrios")
    public ResponseEntity<List<BarrioDto>> listarBarrios() {
        return ResponseEntity.ok(barrioService.listarBarrios());
    }

    @GetMapping("/barrios/{id}")
    public ResponseEntity<BarrioDto> obtenerBarrio(@PathVariable Long id) {
        return ResponseEntity.ok(barrioService.obtenerBarrioPorId(id));
    }

    // --- UNIDADES FUNCIONALES ---

    @PostMapping("/unidades")
    public ResponseEntity<UnidadFuncionalDto> crearUnidadFuncional(@Valid @RequestBody UnidadFuncionalDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadFuncionalService.crearUnidadFuncional(dto));
    }

    @GetMapping("/unidades")
    public ResponseEntity<List<UnidadFuncionalDto>> listarUnidadesFuncionales(
            @RequestParam(required = false) Long barrioId) {
        if (barrioId != null) {
            return ResponseEntity.ok(unidadFuncionalService.listarPorBarrio(barrioId));
        }
        return ResponseEntity.ok(unidadFuncionalService.listarUnidadesFuncionales());
    }

    @GetMapping("/unidades/{id}")
    public ResponseEntity<UnidadFuncionalDto> obtenerUnidadFuncional(@PathVariable Long id) {
        return ResponseEntity.ok(unidadFuncionalService.obtenerUnidadFuncionalPorId(id));
    }

    @PutMapping("/unidades/{id}/toggle-habilitacion")
    public ResponseEntity<UnidadFuncionalDto> toggleHabilitada(@PathVariable Long id) {
        return ResponseEntity.ok(unidadFuncionalService.toggleHabilitada(id));
    }
}
