package ar.edu.unq.epersgeist.modelo.ronda;

import ar.edu.unq.epersgeist.modelo.RandomizerEspiritual;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.util.List;

@DiscriminatorValue("Primer")
@Entity
public class RondaUno extends Ronda{

    public RondaUno(){
        super();
    }

    public RondaUno(RandomizerEspiritual random){
        super();
        this.palabrasPosibles = List.of(
                "tumba", "secta", "miedo", "bruja", "hueso", "grito", "ratas", "dagas", "pacto",
                "fosas", "furia", "peste", "alma", "fosil", "vudu", "sombra", "cruel", "sufrir", "angel",
                "corran", "horror", "maldad", "ayuda", "rencor", "pecar"
        );
        this.setRandomizer(random);
        this.setComienzoDeRonda();
    }

    @Override
    protected void setComienzoDeRonda() {
        this.palabrasPosibles = List.of(
                "tumba", "secta", "miedo", "bruja", "hueso", "grito", "ratas", "dagas", "pacto",
                "fosas", "furia", "peste", "alma", "fosil", "vudu", "sombra", "cruel", "sufrir", "angel",
                "corran", "horror", "maldad", "ayuda", "rencor", "pecar"
        );
        this.elegirPalabraRandom();
    }


    @Override
    public Ronda proximaRonda() {
        return new RondaDos();
    }
}
