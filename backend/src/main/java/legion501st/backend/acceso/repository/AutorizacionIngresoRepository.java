package legion501st.backend.acceso.repository;

import legion501st.backend.acceso.AutorizacionIngreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AutorizacionIngresoRepository extends JpaRepository<AutorizacionIngreso, Long> {

    // Spring arma la consulta por el nombre del método: busca una autorización no usada y dentro del rango de fechas
    Optional<AutorizacionIngreso> findFirstByVisitanteIdAndUtilizadaFalseAndFechaDesdeLessThanEqualAndFechaHastaGreaterThanEqualOrderByFechaHastaAsc(
            Long visitanteId,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta
    );

    java.util.List<AutorizacionIngreso> findByVisitanteIdAndUtilizadaFalse(Long visitanteId);

    java.util.List<AutorizacionIngreso> findByResidenteAutorizaId(Long residenteId);
}
