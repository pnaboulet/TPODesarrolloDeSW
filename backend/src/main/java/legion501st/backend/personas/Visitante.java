package legion501st.backend.personas;

import jakarta.persistence.*;

@Entity
@Table(name = "visitantes")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("VISITANTE")
public class Visitante extends Persona {

    public Visitante() {
        super();
    }

    public Visitante(String nombre, String apellido, String dni, String email) {
        super(nombre, apellido, dni, email);
    }
}
