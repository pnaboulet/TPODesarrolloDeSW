package legion501st.backend.barrio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import legion501st.backend.barrio.UnidadFuncional;

@Repository
public interface UnidadFuncionalRepository extends JpaRepository<UnidadFuncional, Long> {
    
}
