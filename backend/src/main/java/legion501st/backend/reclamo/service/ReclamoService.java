package legion501st.backend.reclamo.service;

import legion501st.backend.personas.Persona;
import legion501st.backend.personas.Residente;
import legion501st.backend.personas.repository.PersonaRepository;
import legion501st.backend.reclamo.EstadoReclamo;
import legion501st.backend.reclamo.HistorialEstado;
import legion501st.backend.reclamo.Reclamo;
import legion501st.backend.reclamo.ReclamoEstadoChangedEvent;
import legion501st.backend.reclamo.dto.CrearReclamoDto;
import legion501st.backend.reclamo.dto.HistorialEstadoDto;
import legion501st.backend.reclamo.dto.ReclamoDto;
import legion501st.backend.reclamo.repository.HistorialEstadoRepository;
import legion501st.backend.reclamo.repository.ReclamoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final PersonaRepository personaRepository;
    private final HistorialEstadoRepository historialRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReclamoService(ReclamoRepository reclamoRepository,
                          PersonaRepository personaRepository,
                          HistorialEstadoRepository historialRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.reclamoRepository = reclamoRepository;
        this.personaRepository = personaRepository;
        this.historialRepository = historialRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ReclamoDto crearReclamo(CrearReclamoDto dto) {
        Persona persona = personaRepository.findById(dto.residenteId())
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + dto.residenteId()));

        if (!(persona instanceof Residente)) {
            throw new IllegalArgumentException("La persona con ID: " + dto.residenteId() + " no es un residente");
        }

        Residente residente = (Residente) persona;

        Reclamo reclamo = new Reclamo(
                residente,
                dto.tipoReclamo(),
                dto.descripcion(),
                dto.prioridad()
        );

        reclamo = reclamoRepository.save(reclamo);

        // Registrar historial inicial
        HistorialEstado historial = new HistorialEstado(
                reclamo,
                null,
                EstadoReclamo.PENDIENTE,
                "Reclamo creado"
        );
        historialRepository.save(historial);

        return mapToDto(reclamo);
    }

    @Transactional
    public ReclamoDto asignarResponsable(Long reclamoId, Long responsableId) {
        Reclamo reclamo = reclamoRepository.findById(reclamoId)
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado con ID: " + reclamoId));

        Persona responsable = personaRepository.findById(responsableId)
                .orElseThrow(() -> new IllegalArgumentException("Responsable no encontrado con ID: " + responsableId));

        EstadoReclamo estadoAnterior = reclamo.getEstado();

        // Aplicar transición de estado mediante el patrón State
        reclamo.asignarResponsable(responsable);
        reclamo = reclamoRepository.save(reclamo);

        EstadoReclamo estadoNuevo = reclamo.getEstado();

        // Registrar historial
        String observacion = "Responsable asignado: " + responsable.getNombre() + " " + responsable.getApellido();
        HistorialEstado historial = new HistorialEstado(
                reclamo,
                estadoAnterior,
                estadoNuevo,
                observacion
        );
        historialRepository.save(historial);

        // Disparar evento asíncrono si el estado cambió
        if (estadoAnterior != estadoNuevo) {
            eventPublisher.publishEvent(new ReclamoEstadoChangedEvent(this, reclamo, estadoAnterior, estadoNuevo));
        }

        return mapToDto(reclamo);
    }

    @Transactional
    public ReclamoDto cambiarEstado(Long reclamoId, EstadoReclamo nuevoEstado, String observacion) {
        Reclamo reclamo = reclamoRepository.findById(reclamoId)
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado con ID: " + reclamoId));

        EstadoReclamo estadoAnterior = reclamo.getEstado();
        if (estadoAnterior == nuevoEstado) {
            return mapToDto(reclamo);
        }

        // Ejecutar la transición mediante el State pattern
        switch (nuevoEstado) {
            case RESUELTO -> reclamo.resolver(observacion);
            case CERRADO -> reclamo.cerrar();
            case CANCELADO -> reclamo.cancelar();
            default -> throw new IllegalStateException("Transición directa a " + nuevoEstado + " no permitida.");
        }

        reclamo = reclamoRepository.save(reclamo);

        // Registrar historial
        HistorialEstado historial = new HistorialEstado(
                reclamo,
                estadoAnterior,
                nuevoEstado,
                observacion != null ? observacion : "Cambio de estado manual"
        );
        historialRepository.save(historial);

        // Disparar evento asíncrono
        eventPublisher.publishEvent(new ReclamoEstadoChangedEvent(this, reclamo, estadoAnterior, nuevoEstado));

        return mapToDto(reclamo);
    }

    public ReclamoDto obtenerReclamoPorId(Long id) {
        Reclamo reclamo = reclamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado con ID: " + id));
        return mapToDto(reclamo);
    }

    public List<ReclamoDto> listarReclamos() {
        return reclamoRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ReclamoDto> listarPorResidente(Long residenteId) {
        return reclamoRepository.findByResidenteId(residenteId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ReclamoDto> listarPorResponsable(Long responsableId) {
        return reclamoRepository.findByResponsableId(responsableId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<HistorialEstadoDto> obtenerHistorial(Long reclamoId) {
        if (!reclamoRepository.existsById(reclamoId)) {
            throw new IllegalArgumentException("Reclamo no encontrado con ID: " + reclamoId);
        }
        return historialRepository.findByReclamoIdOrderByFechaCambioAsc(reclamoId).stream()
                .map(this::mapToHistorialDto)
                .collect(Collectors.toList());
    }

    private ReclamoDto mapToDto(Reclamo reclamo) {
        Long responsableId = reclamo.getResponsable() != null ? reclamo.getResponsable().getId() : null;
        String responsableNombre = reclamo.getResponsable() != null ?
                reclamo.getResponsable().getNombre() + " " + reclamo.getResponsable().getApellido() : null;

        return new ReclamoDto(
                reclamo.getId(),
                reclamo.getResidente().getId(),
                reclamo.getResidente().getNombre() + " " + reclamo.getResidente().getApellido(),
                reclamo.getTipoReclamo(),
                reclamo.getDescripcion(),
                reclamo.getPrioridad(),
                reclamo.getEstado(),
                reclamo.getFechaCreacion(),
                responsableId,
                responsableNombre
        );
    }

    private HistorialEstadoDto mapToHistorialDto(HistorialEstado historial) {
        return new HistorialEstadoDto(
                historial.getId(),
                historial.getReclamo().getId(),
                historial.getEstadoAnterior(),
                historial.getEstadoNuevo(),
                historial.getFechaCambio(),
                historial.getObservacion()
        );
    }
}
