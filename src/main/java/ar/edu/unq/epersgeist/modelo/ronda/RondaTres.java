package ar.edu.unq.epersgeist.modelo.ronda;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;

@DiscriminatorValue("Tercer")
@Entity
public class RondaTres extends Ronda{

    public RondaTres(){
        super();
    }

    @Override
    protected void setComienzoDeRonda(){
        this.palabrasPosibles =  List.of(
                "enloquecer", "omnipresente", "exorcismo", "posesiones", "oscuridad", "dominancia", "invocacion",
                "aterrador", "putrefacto", "sacrificio", "pomberito", "aniquilar", "desesperante", "perseguir", "apocalipsis",
                "inquietante", "infierno", "maleficio", "desgracia", "terrorifico", "calaveras", "cadaveres", "ultratumba",
                "catacumba", "inframundo", "escalofrios"
        );
        this.elegirPalabraRandom();
    }

    @Override
    public Ronda proximaRonda() {
        return new RondaFinalizada();
    }
}
