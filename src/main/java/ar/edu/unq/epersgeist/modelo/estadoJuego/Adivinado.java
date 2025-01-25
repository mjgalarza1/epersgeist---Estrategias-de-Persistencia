package ar.edu.unq.epersgeist.modelo.estadoJuego;

import ar.edu.unq.epersgeist.modelo.ronda.Ronda;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Adivinado")
@NoArgsConstructor
public class Adivinado extends EstadoJuego{
    @Override
    public int cantPuntosObtenidos(Ronda ronda, Character letra) {
        return ronda.letraAcertada(letra);
    }
}
