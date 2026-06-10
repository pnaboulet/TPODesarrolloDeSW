package legion501st.backend.reclamo.repository;

import legion501st.backend.reclamo.HistorialEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {
    List<HistorialEstado> findByReclamoIdOrderByFechaCambioAsc(Long reclamoId);
}
