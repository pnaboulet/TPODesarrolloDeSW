package legion501st.backend.tarea.service;

import legion501st.backend.personas.Persona;
import legion501st.backend.personas.repository.PersonaRepository;
import legion501st.backend.reclamo.Reclamo;
import legion501st.backend.reclamo.repository.ReclamoRepository;
import legion501st.backend.tarea.TareaMantenimiento;
import legion501st.backend.tarea.dto.CrearTareaMantenimientoDto;
import legion501st.backend.tarea.dto.TareaMantenimientoDto;
import legion501st.backend.tarea.repository.TareaMantenimientoRepository;
import legion501st.backend.tarea.EstadoTarea;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TareaService {

    private final TareaMantenimientoRepository tareaRepository;
    private final ReclamoRepository reclamoRepository;
    private final PersonaRepository personaRepository;

    public TareaService(TareaMantenimientoRepository tareaRepository,
                        ReclamoRepository reclamoRepository,
                        PersonaRepository personaRepository) {
        this.tareaRepository = tareaRepository;
        this.reclamoRepository = reclamoRepository;
        this.personaRepository = personaRepository;
    }

    @Transactional
    public TareaMantenimientoDto crearTarea(CrearTareaMantenimientoDto dto) {
        Reclamo reclamo = null;
        if (dto.reclamoId() != null) {
            reclamo = reclamoRepository.findById(dto.reclamoId())
                    .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado con ID: " + dto.reclamoId()));
        }

        Persona responsable = personaRepository.findById(dto.responsableId())
                .orElseThrow(() -> new IllegalArgumentException("Responsable no encontrado con ID: " + dto.responsableId()));

        if (responsable instanceof legion501st.backend.personas.Proveedor) {
            throw new IllegalArgumentException("No se puede asignar una tarea de mantenimiento a un proveedor");
        }
        if (!(responsable instanceof legion501st.backend.personas.PersonalMantenimiento)) {
            throw new IllegalArgumentException("Una tarea de mantenimiento solo puede ser asignada a personal de mantenimiento");
        }

        TareaMantenimiento tarea = TareaMantenimiento.builder()
                .reclamo(reclamo)
                .descripcion(dto.descripcion())
                .responsable(responsable)
                .estado(EstadoTarea.PENDIENTE)
                .build();

        tarea = tareaRepository.save(tarea);
        return mapToDto(tarea);
    }

    @Transactional
    public TareaMantenimientoDto iniciarTarea(Long id) {
        TareaMantenimiento tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con ID: " + id));

        tarea.iniciar();
        tarea = tareaRepository.save(tarea);
        return mapToDto(tarea);
    }

    @Transactional
    public TareaMantenimientoDto completarTarea(Long id) {
        TareaMantenimiento tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con ID: " + id));

        tarea.completar();
        tarea = tareaRepository.save(tarea);
        return mapToDto(tarea);
    }

    @Transactional
    public TareaMantenimientoDto cancelarTarea(Long id) {
        TareaMantenimiento tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con ID: " + id));

        tarea.cancelar();
        tarea = tareaRepository.save(tarea);
        return mapToDto(tarea);
    }

    public TareaMantenimientoDto obtenerTareaPorId(Long id) {
        TareaMantenimiento tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con ID: " + id));
        return mapToDto(tarea);
    }

    public List<TareaMantenimientoDto> listarTareas() {
        return tareaRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TareaMantenimientoDto> listarPorReclamo(Long reclamoId) {
        return tareaRepository.findByReclamoId(reclamoId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TareaMantenimientoDto> listarPorResponsable(Long responsableId) {
        return tareaRepository.findByResponsableId(responsableId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TareaMantenimientoDto mapToDto(TareaMantenimiento tarea) {
        Long reclamoId = tarea.getReclamo() != null ? tarea.getReclamo().getId() : null;
        Long responsableId = tarea.getResponsable() != null ? tarea.getResponsable().getId() : null;
        String responsableNombre = tarea.getResponsable() != null ?
                tarea.getResponsable().getNombre() + " " + tarea.getResponsable().getApellido() : null;

        return new TareaMantenimientoDto(
                tarea.getId(),
                reclamoId,
                tarea.getDescripcion(),
                tarea.getEstado(),
                responsableId,
                responsableNombre
        );
    }
}
