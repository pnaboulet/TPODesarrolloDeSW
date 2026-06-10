package legion501st.backend.reclamo.repository;

import legion501st.backend.reclamo.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {
    List<Reclamo> findByResidenteId(Long residenteId);
    List<Reclamo> findByResponsableId(Long responsableId);
}
