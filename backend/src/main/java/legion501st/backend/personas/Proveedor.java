package legion501st.backend.personas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "proveedores")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("PROVEEDOR")
public class Proveedor extends Persona {

    @NotBlank(message = "El tipo de servicio no puede estar vacío")
    @Size(max = 50, message = "El tipo de servicio no puede superar los 50 caracteres")
    @Column(name = "tipo_servicio", nullable = false, length = 50)
    private String tipoServicio;

    public Proveedor() {
        super();
    }

    public Proveedor(String nombre, String apellido, String dni, String email, String tipoServicio) {
        super(nombre, apellido, dni, email);
        this.tipoServicio = tipoServicio;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
}
