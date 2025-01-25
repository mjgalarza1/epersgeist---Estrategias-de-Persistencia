package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.CantidadNegativaNulaException;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

@RelationshipProperties
public class Condicion {
    @RelationshipId
    private Long id;

    @Min(0)
    private Integer cantidad;
    private Evaluacion evaluacion;

    @TargetNode
    private Habilidad habilidad;

    public Condicion(Integer cantidad, Evaluacion evaluacion){
        if (cantidad == null || cantidad < 0 ){
            throw new CantidadNegativaNulaException();
        }
        this.cantidad = cantidad;
        this.evaluacion = evaluacion;
    }
}
