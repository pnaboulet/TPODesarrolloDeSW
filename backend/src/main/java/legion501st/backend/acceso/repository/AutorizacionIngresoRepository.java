package legion501st.backend.acceso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import legion501st.backend.acceso.AutorizacionIngreso;

@Repository
public interface AutorizacionIngresoRepository extends JpaRepository<AutorizacionIngreso, Long> {
    
}
