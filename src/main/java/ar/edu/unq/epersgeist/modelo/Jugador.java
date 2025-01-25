package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.NoEsTuTurnoException;
import ar.edu.unq.epersgeist.exception.accionInvalida.NoHaySuficienteJugadoresException;
import ar.edu.unq.epersgeist.exception.accionInvalida.RecursionException;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import lombok.*;

@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor  @IgnoreExtraProperties
public class Jugador {

    private String nombre;
    private boolean esMiTurno;
    private int puntuacion;
    private Long idJuego;
    private String jugadorSiguiente;
    private String idPalabraRondaUltimate;

    public Jugador(String nombre, Long idJuego) {
        this.nombre = nombre;
        this.puntuacion = 0;
        this.idJuego = idJuego;
        this.esMiTurno = true;
    }

    public void setJugadorSiguiente(String jugadorNombre){
        if (this.nombre != null && this.nombre.equals(jugadorNombre)) {
            throw new RecursionException(this.nombre, jugadorNombre);
        }
        this.jugadorSiguiente = jugadorNombre;
    }

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntuacion = 0;
        this.esMiTurno = true;
    }

    public void adivinarLetra(Character letra, Juego juego){
        if (!this.esMiTurno) throw new NoEsTuTurnoException();
        puntuacion += juego.evaluarLetra(letra, this);
    }

    public void cambiarTurno() {
        this.esMiTurno = !this.esMiTurno;
    }

    public void setIds(Long idJuego, String idPalabraRondaUltimate){
        this.idJuego = idJuego;
        this.idPalabraRondaUltimate = idPalabraRondaUltimate;
    }

}
