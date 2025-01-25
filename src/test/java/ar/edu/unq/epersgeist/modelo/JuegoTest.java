package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.RondaSinTerminarException;
import ar.edu.unq.epersgeist.exception.accionInvalida.SinIntentosException;
import ar.edu.unq.epersgeist.modelo.ronda.Ronda;
import ar.edu.unq.epersgeist.modelo.ronda.RondaUltimate;
import ar.edu.unq.epersgeist.modelo.ronda.RondaUno;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JuegoTest {
    private Juego juego;
    private RandomizerEspiritual randomizerEspiritual;
    private Jugador jugador;
    private Ronda rondauno;

    @BeforeEach
    public void setUp(){
        randomizerEspiritual = mock(RandomizerEspiritual.class);
        when(randomizerEspiritual.getRandomHasta(25)).thenReturn(0); //la primera que seria "alma"
        rondauno = new RondaUno(randomizerEspiritual);
        juego = new Juego(rondauno);
        jugador = new Jugador("Jugador 1");
    }

    @Test
    void adivinarPalabra(){
        assertEquals(RondaUno.class, juego.getRondaActual().getClass());
        juego.evaluarLetra('a',jugador);
        juego.evaluarLetra('t',jugador);
        juego.evaluarLetra('u',jugador);
        juego.evaluarLetra('m',jugador);
        juego.evaluarLetra('b',jugador);
        assertEquals("tumba", juego.getPalabraAdivinando());
    }

    @Test
    void gastarLos6IntentosNoTePermiteSeguirEvaluandoLetras(){
        assertEquals(RondaUno.class, juego.getRondaActual().getClass());
        juego.evaluarLetra('e', jugador);
        juego.evaluarLetra('f', jugador);
        juego.evaluarLetra('g', jugador);
        juego.evaluarLetra('h', jugador);
        juego.evaluarLetra('i', jugador);
        juego.evaluarLetra('j', jugador);
        assertThrows(SinIntentosException.class, () -> juego.evaluarLetra('z', jugador));
    }

    @Test
    void noPasaDeRondaPorqueNoHizoNada(){
        assertThrows(RondaSinTerminarException.class, () -> juego.cambiarProximaRonda());
    }

    @Test
    void pasaDeRondaPorqueSeQuedoSinIntentos(){
        juego.getRondaActual().setIntentos(0);
        assertDoesNotThrow(() -> juego.cambiarProximaRonda());
    }

    @Test
    void sePuedePasarDeRondaPorqueSeAdivino(){
        juego.evaluarLetra('t',jugador);
        juego.evaluarLetra('u',jugador);
        juego.evaluarLetra('m',jugador);
        juego.evaluarLetra('b',jugador);
        juego.evaluarLetra('a',jugador);
        assertDoesNotThrow(() -> juego.cambiarProximaRonda());
    }

    @Test
    void letrasEquivocadasSonXYZDespuesDeQueJugadorSeEquivoqueConXYZ(){
        juego.evaluarLetra('x',jugador);
        juego.evaluarLetra('y',jugador);
        juego.evaluarLetra('z',jugador);
        assertEquals("xyz", juego.getLetrasEquivocadas());
    }

    @Test
    void laPalabraAAdivinarEsalma(){
        assertEquals("tumba", juego.getPalabraAAdivinar());
    }

    @Test
    void lasLetrasUsadasSonAXYZDespuesDeQueJugadorUseAXYZ(){
        juego.evaluarLetra('a',jugador);
        juego.evaluarLetra('x',jugador);
        juego.evaluarLetra('y',jugador);
        juego.evaluarLetra('z',jugador);
        assertEquals("axyz", juego.getLetrasUsadas());
    }

    @Test
    void cambiarDeTurnoACambiaElTurnoAlJugador1Y2(){
        Jugador jugador1 = new Jugador("Jugador1");
        Jugador jugador2 = new Jugador("Jugador2");
        Jugador jugador3 = new Jugador("Jugador3");
        RondaUltimate rondaUltimate = new RondaUltimate(jugador1, jugador2, jugador3);
        Juego juego = new Juego(rondaUltimate);

        assertTrue(jugador1.isEsMiTurno());

        juego.cambiarTurnoA(jugador1, jugador2);

        assertFalse(jugador1.isEsMiTurno());
        assertTrue(jugador2.isEsMiTurno());
    }

    @AfterEach
    void tearDown(){
        juego = null;
        jugador = null;
    }
}
