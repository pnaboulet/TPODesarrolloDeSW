package legion501st.backend.personas.controller;

import jakarta.validation.Valid;
import legion501st.backend.personas.dto.PersonalDto;
import legion501st.backend.personas.dto.ResidenteDto;
import legion501st.backend.personas.service.PersonaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping("/residentes")
    public ResponseEntity<ResidenteDto> registrarResidente(@Valid @RequestBody ResidenteDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personaService.registrarResidente(dto));
    }

    @GetMapping("/residentes")
    public ResponseEntity<List<ResidenteDto>> listarResidentes() {
        return ResponseEntity.ok(personaService.listarResidentes());
    }

    @PostMapping("/personal")
    public ResponseEntity<PersonalDto> registrarPersonal(@Valid @RequestBody PersonalDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personaService.registrarPersonal(dto));
    }

    @GetMapping("/personal")
    public ResponseEntity<List<PersonalDto>> listarPersonal() {
        return ResponseEntity.ok(personaService.listarPersonal());
    }
}
