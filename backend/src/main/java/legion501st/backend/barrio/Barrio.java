package legion501st.backend.barrio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "barrios")
public class Barrio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del barrio no puede estar vacío")
    @Size(max = 100, message = "El nombre del barrio no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La dirección del barrio no puede estar vacía")
    @Size(max = 150, message = "La dirección del barrio no puede superar los 150 caracteres")
    @Column(name = "direccion", nullable = false, length = 150)
    private String direccion;

    public Barrio() {}

    public Barrio(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
