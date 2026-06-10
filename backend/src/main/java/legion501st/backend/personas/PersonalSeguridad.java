package legion501st.backend.personas;

import jakarta.persistence.*;

@Entity
@Table(name = "personal_seguridad")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("SEGURIDAD")
public class PersonalSeguridad extends Persona {

    public PersonalSeguridad() {
        super();
    }

    public PersonalSeguridad(String nombre, String apellido, String dni, String email) {
        super(nombre, apellido, dni, email);
    }
}
