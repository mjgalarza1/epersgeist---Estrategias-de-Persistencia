package ar.edu.unq.epersgeist.modelo.ronda;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@DiscriminatorValue("Terminada")
@Entity
public class RondaFinalizada extends Ronda {

    @Override
    protected void setComienzoDeRonda() {}

    @Override
    public Ronda proximaRonda() {
        return null;
    }
}
