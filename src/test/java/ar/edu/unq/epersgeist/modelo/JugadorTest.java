package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JugadorTest {

    @Test
    void crearJugador(){
        Jugador jugador = new Jugador("Juan");
        assertEquals("Juan", jugador.getNombre());
        assertEquals(0, jugador.getPuntuacion());
    }
}
