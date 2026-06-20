package legion501st.backend.personas.service;

import legion501st.backend.barrio.UnidadFuncional;
import legion501st.backend.barrio.Barrio;
import legion501st.backend.barrio.repository.UnidadFuncionalRepository;
import legion501st.backend.barrio.repository.BarrioRepository;
import legion501st.backend.personas.*;
import legion501st.backend.personas.dto.PersonalDto;
import legion501st.backend.personas.dto.ResidenteDto;
import legion501st.backend.personas.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final ResidenteRepository residenteRepository;
    private final PersonalSeguridadRepository seguridadRepository;
    private final PersonalMantenimientoRepository mantenimientoRepository;
    private final ProveedorRepository proveedorRepository;
    private final UnidadFuncionalRepository unidadFuncionalRepository;
    private final VisitanteRepository visitanteRepository;
    private final PersonaFactory personaFactory;
    private final BarrioRepository barrioRepository;

    public PersonaService(PersonaRepository personaRepository,
                          ResidenteRepository residenteRepository,
                          PersonalSeguridadRepository seguridadRepository,
                          PersonalMantenimientoRepository mantenimientoRepository,
                          ProveedorRepository proveedorRepository,
                          UnidadFuncionalRepository unidadFuncionalRepository,
                          VisitanteRepository visitanteRepository,
                          PersonaFactory personaFactory,
                          BarrioRepository barrioRepository) {
        this.personaRepository = personaRepository;
        this.residenteRepository = residenteRepository;
        this.seguridadRepository = seguridadRepository;
        this.mantenimientoRepository = mantenimientoRepository;
        this.proveedorRepository = proveedorRepository;
        this.unidadFuncionalRepository = unidadFuncionalRepository;
        this.visitanteRepository = visitanteRepository;
        this.personaFactory = personaFactory;
        this.barrioRepository = barrioRepository;
    }

    @Transactional
    public ResidenteDto registrarResidente(ResidenteDto dto) {
        UnidadFuncional uf = unidadFuncionalRepository.findById(dto.unidadFuncionalId())
                .orElseThrow(() -> new IllegalArgumentException("Unidad Funcional no encontrada con ID: " + dto.unidadFuncionalId()));

        Persona persona = personaFactory.crearPersona(TipoPersona.RESIDENTE, dto.nombre(), dto.apellido(), dto.dni(), dto.email(), null);
        Residente residente = (Residente) persona;
        residente.setUnidadFuncional(uf);

        residente = residenteRepository.save(residente);
        return mapToResidenteDto(residente);
    }

    @Transactional
    public PersonalDto registrarPersonal(PersonalDto dto) {
        Map<String, Object> attrs = new HashMap<>();
        if (dto.tipo() == TipoPersona.PROVEEDOR && dto.tipoServicio() != null) {
            attrs.put("tipoServicio", dto.tipoServicio());
        }

        Persona persona = personaFactory.crearPersona(dto.tipo(), dto.nombre(), dto.apellido(), dto.dni(), dto.email(), attrs);
        if (dto.barrioId() != null) {
            Barrio barrio = barrioRepository.findById(dto.barrioId())
                    .orElseThrow(() -> new IllegalArgumentException("Barrio no encontrado con ID: " + dto.barrioId()));
            persona.setBarrio(barrio);
        }
        persona = personaRepository.save(persona);

        return mapToPersonalDto(persona);
    }

    public List<ResidenteDto> listarResidentes() {
        return residenteRepository.findAll().stream()
                .map(this::mapToResidenteDto)
                .collect(Collectors.toList());
    }

    public List<PersonalDto> listarPersonal() {
        // Obtenemos todas las personas y filtramos aquellas que son personal de seguridad, mantenimiento o proveedores
        return personaRepository.findAll().stream()
                .filter(p -> !(p instanceof Residente) && !(p instanceof Visitante))
                .map(this::mapToPersonalDto)
                .collect(Collectors.toList());
    }

    public List<Visitante> listarVisitantes() {
        return visitanteRepository.findAll();
    }

    @Transactional
    public void toggleHabilitado(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));
        if (!(persona instanceof Residente)) {
            throw new IllegalArgumentException("Solo los residentes se pueden deshabilitar/habilitar");
        }
        persona.setHabilitado(!persona.isHabilitado());
        personaRepository.save(persona);
    }

    private ResidenteDto mapToResidenteDto(Residente residente) {
        return new ResidenteDto(
                residente.getId(),
                residente.getNombre(),
                residente.getApellido(),
                residente.getDni(),
                residente.getEmail(),
                residente.getUnidadFuncional().getId(),
                residente.isHabilitado()
        );
    }

    private PersonalDto mapToPersonalDto(Persona persona) {
        TipoPersona tipo = switch (persona) {
            case Residente r -> TipoPersona.RESIDENTE;
            case Proveedor p -> TipoPersona.PROVEEDOR;
            case PersonalSeguridad ps -> TipoPersona.SEGURIDAD;
            case PersonalMantenimiento pm -> TipoPersona.MANTENIMIENTO;
            case Visitante v -> TipoPersona.VISITANTE;
            case Administrador a -> TipoPersona.ADMINISTRADOR;
            default -> throw new IllegalStateException("Subtipo de persona desconocido: " + persona.getClass());
        };

        String tipoServicio = (persona instanceof Proveedor p) ? p.getTipoServicio() : null;
        Long barrioId = persona.getBarrio() != null ? persona.getBarrio().getId() : null;
        String barrioNombre = persona.getBarrio() != null ? persona.getBarrio().getNombre() : null;

        return new PersonalDto(
                persona.getId(),
                persona.getNombre(),
                persona.getApellido(),
                persona.getDni(),
                persona.getEmail(),
                tipo,
                tipoServicio,
                persona.isHabilitado(),
                barrioId,
                barrioNombre
        );
    }
}
