package legion501st.backend.reclamo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import legion501st.backend.reclamo.HistorialEstado;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {
    
}
