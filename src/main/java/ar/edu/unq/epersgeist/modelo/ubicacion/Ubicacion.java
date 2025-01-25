package ar.edu.unq.epersgeist.modelo.ubicacion;
import ar.edu.unq.epersgeist.exception.accionInvalida.EnergiaInvalidaException;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Getter @Setter @EqualsAndHashCode @NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@Entity
public abstract class Ubicacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    @Range(min= 0, max = 100)
    protected Integer energiaUbicacion;

    @Column(name = "poligono", unique = true)
    protected String idPoligono;

    public Ubicacion(@NonNull String nombre, @NonNull Integer energiaUbicacion) {
        this.nombre = nombre;
        this.setearEnergiaValida(energiaUbicacion);
    }

    public void setearEnergiaValida(Integer energia) {
        if (energia >= 0 && energia <= 100) {
            this.energiaUbicacion = energia;
        } else {
            throw new EnergiaInvalidaException(energia);
        }
    }

    public abstract void invocarAngel (Espiritu espiritu);
    public abstract void invocarDemonio(Espiritu espiritu);
    public abstract Integer getEnergiaAAumentar();
    public abstract void recuperarEnergiaAAngel(Espiritu espiritu);
    public abstract void recuperarEnergiaADemonio(Espiritu espiritu);
    public abstract void moverAngel(Espiritu espiritu);
    public abstract void moverDemonio(Espiritu espiritu);
}