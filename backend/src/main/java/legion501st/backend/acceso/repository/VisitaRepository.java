package legion501st.backend.acceso.repository;

import legion501st.backend.acceso.EstadoVisita;
import legion501st.backend.acceso.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    // Sirve para encontrar una visita que entró pero todavía no salió
    Optional<Visita> findFirstByVisitanteIdAndEstadoOrderByFechaIngresoDesc(Long visitanteId, EstadoVisita estado);
}
