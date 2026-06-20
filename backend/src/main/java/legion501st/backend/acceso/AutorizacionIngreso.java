package legion501st.backend.acceso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import legion501st.backend.personas.Residente;
import legion501st.backend.personas.Visitante;

import java.time.LocalDateTime;

@Entity
@Table(name = "autorizaciones_ingreso")
public class AutorizacionIngreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Residente que autoriza la entrada. En la tabla se guarda como residente_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residente_id")
    private Residente residenteAutoriza;

    // Visitante autorizado. En la tabla se guarda como visitante_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitante_id")
    private Visitante visitante;

    @Column(name = "fecha_desde", nullable = false)
    private LocalDateTime fechaDesde;

    @Column(name = "fecha_hasta", nullable = false)
    private LocalDateTime fechaHasta;

    // para que una misma autorización no se use varias veces
    @Column(name = "utilizada")
    private boolean utilizada = false;

    public AutorizacionIngreso() {
    }

    public AutorizacionIngreso(Residente residenteAutoriza, Visitante visitante,
                               LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        this.residenteAutoriza = residenteAutoriza;
        this.visitante = visitante;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    public boolean estaVigente() {
        LocalDateTime ahora = LocalDateTime.now();

        // esta vigente si no se usó y la fecha actual cae dentro del rango permitido (con 24 hs de tolerancia para inicio)
        return !utilizada
                && fechaDesde != null
                && fechaHasta != null
                && !ahora.isBefore(fechaDesde.minusHours(24))
                && !ahora.isAfter(fechaHasta);
    }

    public void marcarComoUtilizada() {
        this.utilizada = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Residente getResidenteAutoriza() {
        return residenteAutoriza;
    }

    public void setResidenteAutoriza(Residente residenteAutoriza) {
        this.residenteAutoriza = residenteAutoriza;
    }

    public Visitante getVisitante() {
        return visitante;
    }

    public void setVisitante(Visitante visitante) {
        this.visitante = visitante;
    }

    public LocalDateTime getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDateTime fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDateTime getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDateTime fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public boolean isUtilizada() {
        return utilizada;
    }

    public void setUtilizada(boolean utilizada) {
        this.utilizada = utilizada;
    }
}
