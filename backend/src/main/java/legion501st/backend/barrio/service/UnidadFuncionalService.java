package legion501st.backend.barrio.service;

import legion501st.backend.barrio.Barrio;
import legion501st.backend.barrio.UnidadFuncional;
import legion501st.backend.barrio.dto.UnidadFuncionalDto;
import legion501st.backend.barrio.repository.BarrioRepository;
import legion501st.backend.barrio.repository.UnidadFuncionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UnidadFuncionalService {

    private final UnidadFuncionalRepository repository;
    private final BarrioRepository barrioRepository;
    private final legion501st.backend.personas.repository.ResidenteRepository residenteRepository;

    public UnidadFuncionalService(UnidadFuncionalRepository repository, BarrioRepository barrioRepository, legion501st.backend.personas.repository.ResidenteRepository residenteRepository) {
        this.repository = repository;
        this.barrioRepository = barrioRepository;
        this.residenteRepository = residenteRepository;
    }

    @Transactional
    public UnidadFuncionalDto crearUnidadFuncional(UnidadFuncionalDto dto) {
        Barrio barrio = barrioRepository.findById(dto.barrioId())
                .orElseThrow(() -> new IllegalArgumentException("Barrio no encontrado con ID: " + dto.barrioId()));
        UnidadFuncional uf = new UnidadFuncional(barrio, dto.identificador(), dto.tipoUnidad());
        if (dto.habilitada() != null) {
            uf.setHabilitada(dto.habilitada());
        }
        uf = repository.save(uf);
        return mapToDto(uf);
    }

    @Transactional
    public UnidadFuncionalDto toggleHabilitada(Long id) {
        UnidadFuncional uf = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidad funcional no encontrada con ID: " + id));
        boolean nuevaHabilitada = !uf.isHabilitada();
        uf.setHabilitada(nuevaHabilitada);
        uf = repository.save(uf);

        // Actualizar el habilitado individual de los residentes de esa unidad
        List<legion501st.backend.personas.Residente> residentes = residenteRepository.findByUnidadFuncionalId(uf.getId());
        for (legion501st.backend.personas.Residente residente : residentes) {
            residente.setHabilitado(nuevaHabilitada);
            residenteRepository.save(residente);
        }

        return mapToDto(uf);
    }

    public UnidadFuncionalDto obtenerUnidadFuncionalPorId(Long id) {
        UnidadFuncional uf = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidad funcional no encontrada con ID: " + id));
        return mapToDto(uf);
    }

    public List<UnidadFuncionalDto> listarUnidadesFuncionales() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<UnidadFuncionalDto> listarPorBarrio(Long barrioId) {
        return repository.findByBarrioId(barrioId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private UnidadFuncionalDto mapToDto(UnidadFuncional uf) {
        return new UnidadFuncionalDto(uf.getId(), uf.getBarrio().getId(), uf.getIdentificador(), uf.getTipoUnidad(), uf.isHabilitada());
    }
}
