package legion501st.backend.personas;

import jakarta.persistence.*;
import legion501st.backend.barrio.UnidadFuncional;

@Entity
@Table(name = "residentes")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("RESIDENTE")
public class Residente extends Persona {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_funcional_id")
    private UnidadFuncional unidadFuncional;

    public Residente() {
        super();
    }

    public Residente(String nombre, String apellido, String dni, String email, UnidadFuncional unidadFuncional) {
        super(nombre, apellido, dni, email);
        this.unidadFuncional = unidadFuncional;
    }

    public UnidadFuncional getUnidadFuncional() {
        return unidadFuncional;
    }

    public void setUnidadFuncional(UnidadFuncional unidadFuncional) {
        this.unidadFuncional = unidadFuncional;
    }
}
