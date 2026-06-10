package legion501st.backend.personas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import legion501st.backend.personas.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
}
