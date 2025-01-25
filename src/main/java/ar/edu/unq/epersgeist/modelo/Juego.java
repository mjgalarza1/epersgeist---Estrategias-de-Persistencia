package ar.edu.unq.epersgeist.modelo;
import ar.edu.unq.epersgeist.exception.accionInvalida.RondaSinTerminarException;
import ar.edu.unq.epersgeist.modelo.ronda.Ronda;
import ar.edu.unq.epersgeist.modelo.ronda.RondaUno;
import jakarta.persistence.*;
import lombok.*;


@Getter @Setter @ToString @EqualsAndHashCode
@Entity
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ronda_actual_id")
    private Ronda rondaActual;

    public Juego() {
        this.rondaActual = new RondaUno();
    }

    public Juego(Ronda ronda) {
        this.rondaActual = ronda;
    }

    public int evaluarLetra(Character letra, Jugador jugador){
        return rondaActual.evaluarLetra(letra, jugador);
    }

    public void cambiarProximaRonda() {
        if (!this.esPalabraAcertada() && this.getIntentos() != 0) throw new RondaSinTerminarException();
        rondaActual = rondaActual.proximaRonda();
    }

    public void cambiarTurnoA(Jugador jugador, Jugador jugadorSiguiente) {
        this.rondaActual.cambiarTurnoA(jugador, jugadorSiguiente);
    }

    public String getLetrasEquivocadas(){
        return rondaActual.getLetrasEquivocadas();
    }

    public String getPalabraAAdivinar() {
        return rondaActual.getPalabraAAdivinar();
    }

    public String getLetrasUsadas() {return rondaActual.getLetrasUsadas();}

    public String getPalabraAdivinando() {
        return rondaActual.getPalabraAdivinando();
    }

    public int getIntentos() {
        return rondaActual.getIntentos();
    }

    public boolean esPalabraAcertada() {
        return rondaActual.esPalabraAcertada();
    }
}
