package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.exception.accionInvalida.RondaSinTerminarException;
import ar.edu.unq.epersgeist.exception.notFound.JuegoNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Juego;
import ar.edu.unq.epersgeist.modelo.Jugador;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EpersgeistApplication.class)
public class JuegoServiceImplTest {
    @Autowired
    private JuegoService juegoService;
    @Autowired
    private JugadorService jugadorService;

    private Jugador jugador;

    private Juego juego;
    private Juego juegoRecuperado;

    @BeforeEach
    void setUp(){
        // se crea el juego
        juego = new Juego();
        juegoService.crearJuego(juego);

        // se crea el jugador con su juego
        jugador = juegoService.empezarJuego("Alberto");
        juegoRecuperado = juegoService.recuperarJuego(jugador.getIdJuego());

        juegoRecuperado.getRondaActual().settearPalabraAAdivinar("alma");
        juegoService.actualizarJuego(juegoRecuperado);
    }
    //crear
    @Test
    void crearJuego(){
        assertNotNull(juego.getId());
    }

    //recuperar
    @Test
    void recuperarJuego(){
        Juego juegoRecuperado = juegoService.recuperarJuego(juego.getId());

        assertNotNull(juegoRecuperado);
    }

    @Test
    void recuperarJuegoInexistente(){
        assertThrows(JuegoNoEncontradoException.class, () -> juegoService.recuperarJuego(11223L));
    }

    //actualizar
    @Test
    void actualizarJuego(){
        juegoRecuperado.getRondaActual().settearPalabraAAdivinar("soul");
        juegoService.actualizarJuego(juegoRecuperado);

        Juego juegoActualizado = juegoService.recuperarJuego(juegoRecuperado.getId());
        assertEquals("soul", juegoActualizado.getRondaActual().getPalabraAAdivinar());
    }

    //eliminar
    @Test
    void eliminarJuego(){
        juegoService.eliminarJuego(juego);
        assertThrows(JuegoNoEncontradoException.class, () -> juegoService.recuperarJuego(juego.getId()));
    }

    //empezar juego
    @Test
    void empezarJuego(){
        Jugador alberto = juegoService.empezarJuego("Juli");
        Juego partida = juegoService.recuperarJuego(alberto.getIdJuego());

        assertNotNull(alberto);
        assertEquals(partida.getId(), alberto.getIdJuego());

        jugadorService.borrarJugador("Juli");
    }

    //cantidad de intentos hechos por jugador
    @Test
    void cantIntentosRestantesInicial(){
        int cantIntentos = juegoService.cantIntentosRestantes(juego.getId());

        assertEquals(6, cantIntentos);
    }

    @Test
    void juegoRestaIntentoCuendoErraElJugador(){
        jugadorService.adivinarLetra(jugador, 'z', juegoRecuperado).subscribe();

        assertEquals(5, juegoService.cantIntentosRestantes(juegoRecuperado.getId()));
    }

    @Test
    void juegoDejaLaMismaCantidadDeIntentosCuendoAdivinaElJugador(){
        jugadorService.adivinarLetra(jugador, 'a', juegoRecuperado).subscribe();
        assertEquals(6, juegoService.cantIntentosRestantes(juegoRecuperado.getId()));
    }

    // se guardan las letras equivocadas del jugador
    @Test
    void sePersistenLasLetrasEquivocadas(){
        jugadorService.adivinarLetra(jugador, 'j', juegoRecuperado).subscribe();
        jugadorService.adivinarLetra(jugador, 'e', juegoRecuperado).subscribe();
        assertEquals("je", juegoService.letrasEquivocadas(juegoRecuperado.getId()));
    }

    // se guarda la palabra a adivinar
    @Test
    void sePersisteLaPalabraAdivinando(){
        assertEquals("____", juegoService.palabraAdivinando(juegoRecuperado.getId()));
    }

    //ronda actual se persiste
    @Test
    void sePersisteLaRondaActualYEsLaPrimera(){
        assertEquals("RondaUno", juegoService.rondaActual(juegoRecuperado.getId()));
    }

    @Test
    void alAdivinarPalabraPasaALaRonda2(){
        jugadorService.adivinarLetra(jugador, 'a', juegoRecuperado).subscribe();
        jugadorService.adivinarLetra(jugador, 'l', juegoRecuperado).subscribe();
        jugadorService.adivinarLetra(jugador, 'm', juegoRecuperado).subscribe();
        juegoService.pasarALaSiguienteRonda(juegoRecuperado.getId());
        assertEquals("RondaDos", juegoService.rondaActual(juegoRecuperado.getId()));
    }

    @Test
    void noSePuedeAvanzarDeRondaSiLaActualNoEstaCompleta(){
        assertThrows(RondaSinTerminarException.class, () -> juegoService.pasarALaSiguienteRonda(juegoRecuperado.getId()));
    }

    // puntuacion del jugador cambia al adivinar o errar
    @Test
    void jugadorAciertaLetra(){
        assertEquals(1,jugadorService.adivinarLetra(jugador, 'a', juegoRecuperado).block().getPuntuacion());
    }

    @Test
    void jugadorNoAciertaLetra(){
        assertEquals(-1,jugadorService.adivinarLetra(jugador, 'r', juegoRecuperado).block().getPuntuacion());
    }

    @Test
    void jugadorAdivinaLaPalabraAlmaYSuma8Puntos(){
        jugadorService.adivinarLetra(jugador, 'a', juegoRecuperado).subscribe();
        jugadorService.adivinarLetra(jugador, 'l', juegoRecuperado).subscribe();
        jugadorService.adivinarLetra(jugador, 'm', juegoRecuperado).subscribe();
        assertEquals(7,jugadorService.buscarJugador("Alberto").block().getPuntuacion());
    }

    @Test
    void jugadorNoAdivinaNuncaYPierde(){
        jugadorService.adivinarLetra(jugador, 'r', juegoRecuperado).subscribe();
        jugadorService.adivinarLetra(jugador, 't', juegoRecuperado).subscribe();
        jugadorService.adivinarLetra(jugador, 'y', juegoRecuperado).subscribe();
        jugadorService.adivinarLetra(jugador, 'o', juegoRecuperado).subscribe();
        jugadorService.adivinarLetra(jugador, 'p', juegoRecuperado).subscribe();
        jugadorService.adivinarLetra(jugador, 's', juegoRecuperado).subscribe();
        assertEquals(-10, jugadorService.buscarJugador("Alberto").block().getPuntuacion());
    }


    @AfterEach
    void tearDown(){
        juegoService.eliminarJuego(juego);
        juegoService.eliminarJuego(juegoRecuperado);
        jugadorService.borrarJugador("Alberto");
    }
}
