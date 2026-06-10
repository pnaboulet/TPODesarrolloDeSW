package legion501st.backend.personas;

import jakarta.persistence.*;

@Entity
@Table(name = "personal_mantenimiento")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("MANTENIMIENTO")
public class PersonalMantenimiento extends Persona {

    public PersonalMantenimiento() {
        super();
    }

    public PersonalMantenimiento(String nombre, String apellido, String dni, String email) {
        super(nombre, apellido, dni, email);
    }
}
