package legion501st.backend.tarea.repository;

import legion501st.backend.tarea.TareaMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaMantenimientoRepository extends JpaRepository<TareaMantenimiento, Long> {
    List<TareaMantenimiento> findByReclamoId(Long reclamoId);
    List<TareaMantenimiento> findByResponsableId(Long responsableId);
}
