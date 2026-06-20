package legion501st.backend.personas.repository;

import legion501st.backend.personas.Residente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ResidenteRepository extends JpaRepository<Residente, Long> {
    Optional<Residente> findByDni(String dni);
    List<Residente> findByUnidadFuncionalId(Long unidadFuncionalId);
}
