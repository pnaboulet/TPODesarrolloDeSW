package legion501st.backend.barrio.service;

import legion501st.backend.barrio.Barrio;
import legion501st.backend.barrio.dto.BarrioDto;
import legion501st.backend.barrio.repository.BarrioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BarrioService {

    private final BarrioRepository repository;

    public BarrioService(BarrioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BarrioDto crearBarrio(BarrioDto dto) {
        Barrio barrio = new Barrio(dto.nombre(), dto.direccion());
        barrio = repository.save(barrio);
        return mapToDto(barrio);
    }

    public BarrioDto obtenerBarrioPorId(Long id) {
        Barrio barrio = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Barrio no encontrado con ID: " + id));
        return mapToDto(barrio);
    }

    public List<BarrioDto> listarBarrios() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private BarrioDto mapToDto(Barrio barrio) {
        return new BarrioDto(barrio.getId(), barrio.getNombre(), barrio.getDireccion());
    }
}
