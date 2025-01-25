package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.HabilidadInvalidaException;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.io.Serializable;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Node
public class Habilidad  implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String nombre;

    public Habilidad(String nombre) {
        if (nombre == null || nombre.isEmpty()) throw new HabilidadInvalidaException();
        this.nombre = nombre;
    }
}
