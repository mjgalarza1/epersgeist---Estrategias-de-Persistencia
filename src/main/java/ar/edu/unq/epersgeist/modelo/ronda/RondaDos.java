package ar.edu.unq.epersgeist.modelo.ronda;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;

@DiscriminatorValue("Segunda")
@Entity
public class RondaDos extends Ronda{

    public RondaDos(){
        super();
    }

    @Override
    protected void setComienzoDeRonda(){
        this.palabrasPosibles =  List.of(
                "espiritu", "lamento", "macabro", "sepulcro", "angustia", "maldicion",
                "craneos", "exorcismo", "penumbra", "funebre", "necrosis", "reliquia", "fantasma",
                "tortura", "maldito", "ofrenda", "diablo", "sangren", "silencio", "mentiras",
                "traicion", "corrupto", "perdedor", "macumba", "satanico"
        );
        this.elegirPalabraRandom();
    }


    @Override
    public Ronda proximaRonda() {
        return new RondaTres();
    }
}
