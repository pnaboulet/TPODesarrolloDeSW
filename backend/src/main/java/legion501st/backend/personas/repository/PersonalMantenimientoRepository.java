package legion501st.backend.personas.repository;

import legion501st.backend.personas.PersonalMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalMantenimientoRepository extends JpaRepository<PersonalMantenimiento, Long> {
    Optional<PersonalMantenimiento> findByDni(String dni);
}
