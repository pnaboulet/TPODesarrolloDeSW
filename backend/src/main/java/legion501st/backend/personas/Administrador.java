package legion501st.backend.personas;

import jakarta.persistence.*;

@Entity
@Table(name = "administradores")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Persona {

    public Administrador() {
        super();
    }

    public Administrador(String nombre, String apellido, String dni, String email) {
        super(nombre, apellido, dni, email);
    }
}
