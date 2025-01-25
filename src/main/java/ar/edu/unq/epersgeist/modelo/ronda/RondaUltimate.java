package ar.edu.unq.epersgeist.modelo.ronda;

import ar.edu.unq.epersgeist.modelo.Jugador;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("Ultimate")
@Entity
public class RondaUltimate extends Ronda {


    public RondaUltimate(Jugador jugador1, Jugador jugador2, Jugador jugador3) {
        super();
        this.settearTurnos(jugador1, jugador2, jugador3);
    }

    @Override
    protected void setComienzoDeRonda(){
        this.palabrasPosibles =  List.of(
                "necromanticos", "transmutaciones", "deshumanizacion", "desmembramiento"
        );
        this.elegirPalabraRandom();
    }

    private void settearTurnos(Jugador jugador1, Jugador jugador2, Jugador jugador3) {
        jugador1.setEsMiTurno(true);
        jugador1.setJugadorSiguiente(jugador2.getNombre());

        jugador2.setEsMiTurno(false);
        jugador2.setJugadorSiguiente(jugador3.getNombre());

        jugador3.setEsMiTurno(false);
        jugador3.setJugadorSiguiente(jugador1.getNombre());
    }

    @Override
    public void cambiarTurnoA(Jugador jugador, Jugador jugadorSiguiente) {
        jugador.cambiarTurno();
        jugadorSiguiente.cambiarTurno();
    }

    @Override
    public Ronda proximaRonda() {
        return new RondaFinalizada();
    }

}
