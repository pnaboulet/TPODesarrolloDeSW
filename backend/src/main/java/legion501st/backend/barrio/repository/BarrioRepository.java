package legion501st.backend.barrio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import legion501st.backend.barrio.Barrio;

@Repository
public interface BarrioRepository extends JpaRepository<Barrio, Long> {
    
}
