package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.LetraUsadaException;
import ar.edu.unq.epersgeist.exception.accionInvalida.NoEsTuTurnoException;
import ar.edu.unq.epersgeist.modelo.estadoJuego.Equivocado;
import ar.edu.unq.epersgeist.modelo.ronda.Ronda;
import ar.edu.unq.epersgeist.modelo.ronda.RondaUltimate;
import ar.edu.unq.epersgeist.modelo.ronda.RondaUno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RondaTest {
    private RandomizerEspiritual randomizerEspiritual;
    private Ronda ronda;
    private Jugador jugador;

    @BeforeEach
    public void setUp(){
        randomizerEspiritual = mock(RandomizerEspiritual.class);
        when(randomizerEspiritual.getRandomHasta(25)).thenReturn(0); //la primera que seria "alma"
        jugador = mock(Jugador.class);
        ronda = new RondaUno(randomizerEspiritual);
    }

    @Test
    void crearRondaSeteaLosValoresInicialesCorrectamente(){
        assertEquals(6, ronda.getIntentos());
        assertEquals(Equivocado.class, ronda.getEstado().getClass());
        assertEquals( "tumba", ronda.getPalabraAAdivinar());
        assertEquals("_____", ronda.getPalabraAdivinando());
        assertTrue(ronda.getLetrasUsadas().isEmpty());
        assertTrue(ronda.getLetrasEquivocadas().isEmpty());
    }

    @Test
    void evaluarLetraEquivocadaRestaUnPunto(){
        assertEquals(-1, ronda.evaluarLetra('j', jugador));
    }

    @Test
    void evaluarLetraAcertadaSumaUnPunto(){
        assertEquals(1, ronda.evaluarLetra('a',jugador));
    }

    @Test
    void adivinarLaPalabraCompletaSuma7Puntos(){
        int puntaje = ronda.evaluarLetra('t',jugador);
        puntaje += ronda.evaluarLetra('u',jugador);
        puntaje += ronda.evaluarLetra('m',jugador);
        puntaje += ronda.evaluarLetra('b',jugador);
        puntaje += ronda.evaluarLetra('a',jugador);

        assertEquals(9, puntaje);
    }

    @Test
    void seGuardanLasLetrasUsadasYEquivocadas(){
        ronda.evaluarLetra('h',jugador);
        ronda.evaluarLetra('k',jugador);
        ronda.evaluarLetra('r',jugador);
        ronda.evaluarLetra('w',jugador);
        ronda.evaluarLetra('t',jugador);

        String letrasUsadas = "hkrwt";
        String letrasEquivocadas = "hkrw";

        assertEquals(letrasEquivocadas, ronda.getLetrasEquivocadas());
        assertEquals(letrasUsadas, ronda.getLetrasUsadas());
    }

    @Test
    void noAdivinaLaPalabraYResta10Puntos(){
        int puntaje = ronda.evaluarLetra('h',jugador);
        puntaje += ronda.evaluarLetra('k',jugador);
        puntaje += ronda.evaluarLetra('r',jugador);
        puntaje += ronda.evaluarLetra('q',jugador);
        puntaje += ronda.evaluarLetra('w',jugador);
        puntaje += ronda.evaluarLetra('z',jugador);

        assertEquals(-10, puntaje);
    }


    @Test
    void intentarAdivinarConLetraAnteriormenteUsadaTiraLetraUsadaException(){
        ronda.evaluarLetra('h',jugador);

        assertThrows(LetraUsadaException.class, () -> ronda.evaluarLetra('h',jugador));
    }

    @Test
    void settearPalabraAAdivinar(){
        ronda.settearPalabraAAdivinar("hola");
        assertEquals("hola", ronda.getPalabraAAdivinar());
        assertEquals("____" , ronda.getPalabraAdivinando());
    }

    @Test
    void settearPalabraAAdivinarRespetaEspacios(){
        ronda.settearPalabraAAdivinar("hola mundo");
        assertEquals("hola mundo", ronda.getPalabraAAdivinar());
        assertEquals("____ _____" , ronda.getPalabraAdivinando());
    }



    //ultimate test

    @Test
    void rondaUltimateTest(){
        Jugador jugador1 = new Jugador("Jugador1");
        Jugador jugador2 = new Jugador("Jugador2");
        Jugador jugador3 = new Jugador("Jugador3");
        RondaUltimate rondaUltimate = new RondaUltimate(jugador1, jugador2, jugador3);
        Juego juego = new Juego(rondaUltimate);

        assertThrows(NoEsTuTurnoException.class, () -> jugador2.adivinarLetra('a', juego));
        assertThrows(NoEsTuTurnoException.class, () -> jugador3.adivinarLetra('a', juego));
        assertDoesNotThrow(() -> jugador1.adivinarLetra('a', juego));

    }


    @Test
    void cambiarDeTurnoACambiaElTurnoAlJugador1Y2(){
        Jugador jugador1 = new Jugador("Jugador1");
        Jugador jugador2 = new Jugador("Jugador2");
        Jugador jugador3 = new Jugador("Jugador3");
        RondaUltimate rondaUltimate = new RondaUltimate(jugador1, jugador2, jugador3);

        assertTrue(jugador1.isEsMiTurno());

        rondaUltimate.cambiarTurnoA(jugador1, jugador2);

        assertFalse(jugador1.isEsMiTurno());
        assertTrue(jugador2.isEsMiTurno());
    }

}
