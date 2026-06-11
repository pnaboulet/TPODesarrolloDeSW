package legion501st.backend.personas.repository;

import legion501st.backend.personas.PersonalSeguridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalSeguridadRepository extends JpaRepository<PersonalSeguridad, Long> {
    Optional<PersonalSeguridad> findByDni(String dni);
}
