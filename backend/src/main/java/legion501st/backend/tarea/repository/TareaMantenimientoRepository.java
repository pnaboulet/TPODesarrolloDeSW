package legion501st.backend.tarea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import legion501st.backend.tarea.TareaMantenimiento;

@Repository
public interface TareaMantenimientoRepository extends JpaRepository<TareaMantenimiento, Long> {
    
}
