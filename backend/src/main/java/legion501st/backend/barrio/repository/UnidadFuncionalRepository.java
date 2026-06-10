package legion501st.backend.barrio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import legion501st.backend.barrio.UnidadFuncional;

import java.util.List;

@Repository
public interface UnidadFuncionalRepository extends JpaRepository<UnidadFuncional, Long> {
    List<UnidadFuncional> findByBarrioId(Long barrioId);
}
