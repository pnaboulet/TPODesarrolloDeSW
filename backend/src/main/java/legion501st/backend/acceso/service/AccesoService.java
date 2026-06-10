package legion501st.backend.acceso.service;

import legion501st.backend.acceso.AutorizacionIngreso;
import legion501st.backend.acceso.EstadoVisita;
import legion501st.backend.acceso.ProtocoloAcceso;
import legion501st.backend.acceso.Visita;
import legion501st.backend.acceso.repository.AutorizacionIngresoRepository;
import legion501st.backend.acceso.repository.VisitaRepository;
import legion501st.backend.personas.Persona;
import legion501st.backend.personas.PersonalSeguridad;
import legion501st.backend.personas.Residente;
import legion501st.backend.personas.Visitante;
import legion501st.backend.personas.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccesoService {

    private final AutorizacionIngresoRepository autorizacionRepository;
    private final VisitaRepository visitaRepository;
    private final PersonaRepository personaRepository;
    private final List<ProtocoloAcceso> protocolos;

    public AccesoService(AutorizacionIngresoRepository autorizacionRepository,
                         VisitaRepository visitaRepository,
                         PersonaRepository personaRepository,
                         List<ProtocoloAcceso> protocolos) {
        this.autorizacionRepository = autorizacionRepository;
        this.visitaRepository = visitaRepository;
        this.personaRepository = personaRepository;
        this.protocolos = protocolos;
    }

    public AutorizacionIngreso crearAutorizacion(Long residenteId, Long visitanteId,
                                                  LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        Persona residente = buscarPersona(residenteId);
        Persona visitante = buscarPersona(visitanteId);

        if (!(residente instanceof Residente)) {
            throw new IllegalArgumentException("La persona que autoriza debe ser un residente");
        }

        if (!(visitante instanceof Visitante)) {
            throw new IllegalArgumentException("La persona autorizada debe ser un visitante");
        }

        if (fechaDesde == null || fechaHasta == null || fechaHasta.isBefore(fechaDesde)) {
            throw new IllegalArgumentException("El rango de fechas de la autorización no es válido");
        }

        AutorizacionIngreso autorizacion = new AutorizacionIngreso(
                (Residente) residente,
                (Visitante) visitante,
                fechaDesde,
                fechaHasta
        );

        return autorizacionRepository.save(autorizacion);
    }

    public Visita registrarIngresoVisitante(Long visitanteId, Long seguridadId) {
        Persona visitante = buscarPersona(visitanteId);
        Persona seguridad = buscarPersona(seguridadId);

        if (!(visitante instanceof Visitante)) {
            throw new IllegalArgumentException("Solo se puede registrar ingreso para visitantes");
        }

        if (!(seguridad instanceof PersonalSeguridad)) {
            throw new IllegalArgumentException("El ingreso debe registrarlo personal de seguridad");
        }

        LocalDateTime ahora = LocalDateTime.now();

        AutorizacionIngreso autorizacion = autorizacionRepository
                .findFirstByVisitanteIdAndUtilizadaFalseAndFechaDesdeLessThanEqualAndFechaHastaGreaterThanEqualOrderByFechaHastaAsc(
                        visitanteId,
                        ahora,
                        ahora
                )
                .orElse(null);

        // Acá se aplica Strategy: el service no valida a mano, le pregunta al protocolo correcto
        ProtocoloAcceso protocolo = buscarProtocolo(visitante);

        if (!protocolo.puedeIngresar(visitante, autorizacion)) {
            throw new IllegalArgumentException("El visitante no tiene una autorización vigente");
        }

        autorizacion.marcarComoUtilizada();
        autorizacionRepository.save(autorizacion);

        Visita visita = new Visita((Visitante) visitante, autorizacion, (PersonalSeguridad) seguridad);
        return visitaRepository.save(visita);
    }

    public Visita registrarSalidaVisitante(Long visitanteId) {
        Visita visita = visitaRepository
                .findFirstByVisitanteIdAndEstadoOrderByFechaIngresoDesc(visitanteId, EstadoVisita.EN_CURSO)
                .orElseThrow(() -> new IllegalArgumentException("No hay una visita en curso para ese visitante"));

        visita.registrarSalida();
        return visitaRepository.save(visita);
    }

    public List<Visita> listarVisitas() {
        return visitaRepository.findAll();
    }

    private Persona buscarPersona(Long personaId) {
        return personaRepository.findById(personaId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la persona con id " + personaId));
    }

    private ProtocoloAcceso buscarProtocolo(Persona persona) {
        return protocolos.stream()
                .filter(protocolo -> protocolo.aplica(persona))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No hay protocolo de acceso para esa persona"));
    }
}
