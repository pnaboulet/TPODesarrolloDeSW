package legion501st.backend.barrio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "unidades_funcionales")
public class UnidadFuncional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barrio_id", nullable = false)
    @NotNull(message = "El barrio asociado no puede ser nulo")
    private Barrio barrio;

    @NotBlank(message = "El identificador de la unidad funcional no puede estar vacío")
    @Size(max = 20, message = "El identificador no puede superar los 20 caracteres")
    @Column(name = "identificador", nullable = false, length = 20)
    private String identificador;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_unidad", nullable = false, length = 20)
    @NotNull(message = "El tipo de unidad funcional no puede ser nulo")
    private TipoUnidad tipoUnidad;

    public UnidadFuncional() {}

    public UnidadFuncional(Barrio barrio, String identificador, TipoUnidad tipoUnidad) {
        this.barrio = barrio;
        this.identificador = identificador;
        this.tipoUnidad = tipoUnidad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public TipoUnidad getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(TipoUnidad tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }
}
