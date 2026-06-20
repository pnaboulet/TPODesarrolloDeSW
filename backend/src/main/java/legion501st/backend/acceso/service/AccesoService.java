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
import legion501st.backend.personas.repository.VisitanteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccesoService {

    private final AutorizacionIngresoRepository autorizacionRepository;
    private final VisitaRepository visitaRepository;
    private final PersonaRepository personaRepository;
    private final VisitanteRepository visitanteRepository;
    private final List<ProtocoloAcceso> protocolos;

    public AccesoService(AutorizacionIngresoRepository autorizacionRepository,
                         VisitaRepository visitaRepository,
                         PersonaRepository personaRepository,
                         VisitanteRepository visitanteRepository,
                         List<ProtocoloAcceso> protocolos) {
        this.autorizacionRepository = autorizacionRepository;
        this.visitaRepository = visitaRepository;
        this.personaRepository = personaRepository;
        this.visitanteRepository = visitanteRepository;
        this.protocolos = protocolos;
    }

    public AutorizacionIngreso crearAutorizacion(Long residenteId, String visitanteNombre, String visitanteDni,
                                                  LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        Persona residente = buscarPersona(residenteId);

        if (!(residente instanceof Residente)) {
            throw new IllegalArgumentException("La persona que autoriza debe ser un residente");
        }

        validarDatosVisitante(visitanteNombre, visitanteDni);
        validarRangoFechas(fechaDesde, fechaHasta);

        Visitante visitante = buscarOCrearVisitante(visitanteNombre, visitanteDni);

        AutorizacionIngreso autorizacion = new AutorizacionIngreso(
                (Residente) residente,
                visitante,
                fechaDesde,
                fechaHasta
        );

        return autorizacionRepository.save(autorizacion);
    }

    public Visita registrarIngresoVisitante(String visitanteDni, Long seguridadId) {
        validarDni(visitanteDni);

        Visitante visitante = visitanteRepository.findByDni(visitanteDni.trim())
                .orElseThrow(() -> new IllegalArgumentException("No existe un visitante con ese DNI"));

        return registrarIngresoVisitante(visitante.getId(), seguridadId);
    }

    public Visita registrarIngresoVisitante(Long visitanteId, Long seguridadId) {
        Persona visitante = buscarPersona(visitanteId);
        Persona seguridad = buscarPersona(seguridadId);

        if (!(seguridad instanceof PersonalSeguridad)) {
            throw new IllegalArgumentException("El ingreso debe registrarlo personal de seguridad");
        }

        LocalDateTime ahora = LocalDateTime.now();

        AutorizacionIngreso autorizacion = null;
        if (visitante instanceof Visitante) {
            autorizacion = autorizacionRepository
                    .findFirstByVisitanteIdAndUtilizadaFalseAndFechaDesdeLessThanEqualAndFechaHastaGreaterThanEqualOrderByFechaHastaAsc(
                            visitanteId,
                            ahora,
                            ahora
                    )
                    .orElse(null);
        }

        // Strategy: el service delega la regla de ingreso al protocolo que corresponda
        ProtocoloAcceso protocolo = buscarProtocolo(visitante);

        if (!protocolo.puedeIngresar(visitante, autorizacion)) {
            throw new IllegalArgumentException("La persona no cumple con el protocolo de acceso o no tiene autorización vigente");
        }

        if (autorizacion != null) {
            autorizacion.marcarComoUtilizada();
            autorizacionRepository.save(autorizacion);
        }

        Visita visita = Visita.builder()
                .visitante(visitante)
                .autorizacionIngreso(autorizacion)
                .registradoPor((PersonalSeguridad) seguridad)
                .fechaIngreso(ahora)
                .estado(EstadoVisita.EN_CURSO)
                .build();
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

    private Visitante buscarOCrearVisitante(String nombre, String dni) {
        String dniLimpio = dni.trim();

        return visitanteRepository.findByDni(dniLimpio)
                .orElseGet(() -> {
                    // La tabla personas exige apellido y email; para visitantes se generan internamente
                    Visitante nuevoVisitante = new Visitante(
                            nombre.trim(),
                            "Visitante",
                            dniLimpio,
                            generarEmailVisitante(dniLimpio)
                    );
                    return visitanteRepository.save(nuevoVisitante);
                });
    }

    private void validarDatosVisitante(String nombre, String dni) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar el nombre del visitante");
        }

        validarDni(dni);
    }

    private void validarDni(String dni) {
        if (dni == null || !dni.trim().matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos");
        }
    }

    private void validarRangoFechas(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        if (fechaDesde == null || fechaHasta == null || !fechaHasta.isAfter(fechaDesde)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    private String generarEmailVisitante(String dni) {
        String dniParaEmail = dni.replaceAll("[^0-9A-Za-z]", "");
        return "visitante." + dniParaEmail + "@barrio.com";
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
