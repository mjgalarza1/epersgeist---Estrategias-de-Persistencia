package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.exception.accionInvalida.JugadorDuplicadoException;
import ar.edu.unq.epersgeist.exception.accionInvalida.NoEsTuTurnoException;
import ar.edu.unq.epersgeist.exception.notFound.JugadorNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Juego;
import ar.edu.unq.epersgeist.modelo.Jugador;
import ar.edu.unq.epersgeist.modelo.ronda.RondaUltimate;
import ar.edu.unq.epersgeist.servicios.impl.JuegoServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.JugadorServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EpersgeistApplication.class)
public class JugadorServiceImplTest {

    @Autowired
    private JugadorServiceImpl jugadorService;
    @Autowired
    private JuegoServiceImpl juegoService;
    private Jugador jugador;

    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugador3;

    @BeforeEach
    void setUp()  {
        jugador = new Jugador("Juan");
        jugador.setPuntuacion(10);
        jugadorService.crearJugador(jugador,123L).block();
    }

    //crear
    @Test
    void crearJugador(){
        Jugador nuevoJugador = new Jugador("Osvaldo");
        StepVerifier.create(jugadorService.crearJugador(nuevoJugador,123L))
                .verifyComplete();
        jugadorService.borrarJugador("Osvaldo");
    }

    @Test
    void crearJugadorDuplicado() {
        Jugador otroJuan = new Jugador("Juan");

        StepVerifier.create(jugadorService.crearJugador(otroJuan,12L))
                .expectError(JugadorDuplicadoException.class)
                .verify();
    }

    //recuperar
    @Test
    void recuperarJugador() {
        // Suponemos que el jugador "Juan" ya está en la base de datos
        Mono<Jugador> jugadorRecuperado = jugadorService.buscarJugador("Juan");

        // Usamos block() para bloquear el Mono y obtener el resultado de manera síncrona en el test
        Jugador jugadorRecuperadoSync = jugadorRecuperado.block();

        // Ahora podemos hacer las aserciones sobre el jugador recuperado
        assertNotNull(jugadorRecuperadoSync); // Verificamos que el jugador no sea nulo
        assertEquals(jugador.getNombre(), jugadorRecuperadoSync.getNombre()); // Verificamos el nombre
        assertEquals(jugador.getPuntuacion(), jugadorRecuperadoSync.getPuntuacion()); // Verificamos la puntuación
    }

    @Test
    void recuperarJugadorInexistente(){
        assertThrows(JugadorNoEncontradoException.class,
                () -> jugadorService.buscarJugador("No existe").block());
    }

    //eliminar
    @Test
    void eliminarJugador(){
        Jugador nuevoJugador = new Jugador("Osvaldo");
        jugadorService.crearJugador(nuevoJugador,123L).block();

        jugadorService.borrarJugador("Osvaldo");

        assertThrows(JugadorNoEncontradoException.class,
                () ->  jugadorService.buscarJugador("Osvaldo").block());
    }

    //actualizar
    @Test
    void actualizarPuntuacion() {
        jugador.setPuntuacion(20);

        // Primero se actualiza el jugador
        Mono<Jugador> futuroActualizar = jugadorService.actualizar(jugador);

        // Luego, encadenamos la búsqueda del jugador después de que se complete la actualización
        Jugador jugadorRecuperado = futuroActualizar
                .flatMap(j -> jugadorService.buscarJugador("Juan")) // Buscamos el jugador después de la actualización
                .block(); // Bloqueamos el flujo para obtener el resultado

        assertNotNull(jugadorRecuperado);
        assertEquals(20, jugadorRecuperado.getPuntuacion());
    }

    @Test
    void obtenerRankingPeroNoHayNadieEnLaBase(){
        jugadorService.borrarJugador("Juan");
        Flux<Jugador> rankingFlux = jugadorService.obtenerRanking();
    }

    @Test
    void obtenerRanking(){
        Jugador carola = new Jugador("Carola");
        carola.setPuntuacion(20000);
        Jugador karina = new Jugador("Karina");
        karina.setPuntuacion(600000);
        Jugador lucas = new Jugador("Lucas");
        lucas.setPuntuacion(800000000);

        jugadorService.crearJugador(carola,123L).block();
        jugadorService.crearJugador(karina,124L).block();
        jugadorService.crearJugador(lucas,125L).block();

        // Creamos un flujo con StepVerifier y controlamos la cancelación manualmente
        Flux<Jugador> rankingFlux = jugadorService.obtenerRanking()
                .take(3) // Solo nos interesa el ranking con los 3 primeros jugadores
                .doOnTerminate(() -> System.out.println("Flujo terminado"));

        StepVerifier.create(rankingFlux)
                .expectNext(lucas)
                .expectNext(karina)
                .expectNext(carola)
                .verifyComplete();

        jugadorService.borrarJugador("Lucas");
        jugadorService.borrarJugador("Carola");
        jugadorService.borrarJugador("Karina");
    }

    //adivinar letra
    @Test
    void adivinarLetraEnRondaUltimate(){
        //los creo y persisto
        jugador1 = new Jugador("j1");
        jugador2 = new Jugador("j2");
        jugador3 = new Jugador("j3");

        Juego juego = new Juego(new RondaUltimate(jugador1, jugador2, jugador3));
        juegoService.crearJuego(juego);

        jugadorService.crearJugador(jugador1, juego.getId()).subscribe();
        jugadorService.crearJugador(jugador2, juego.getId()).subscribe();
        jugadorService.crearJugador(jugador3, juego.getId()).subscribe();

        //adivinan por turno
        assertTrue(jugador1.isEsMiTurno());


        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador2, 'a', juego).block());
        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador3, 'a', juego).block());
        jugadorService.adivinarLetra(jugador1, 'a', juego).block();

        jugador2 = jugadorService.buscarJugador("j2").block();
        jugador3 = jugadorService.buscarJugador("j3").block();
        jugador1 = jugadorService.buscarJugador("j1").block();

        assertFalse(jugador1.isEsMiTurno());
        assertTrue(jugador2.isEsMiTurno());

        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador1, 'b', juego).block());
        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador3, 'b', juego).block());
        jugadorService.adivinarLetra(jugador2, 'b', juego).block();


        jugador2 = jugadorService.buscarJugador("j2").block();
        jugador3 = jugadorService.buscarJugador("j3").block();
        jugador1 = jugadorService.buscarJugador("j1").block();

        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador2, 'c', juego).block());
        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador1, 'c', juego).block());
        jugadorService.adivinarLetra(jugador3, 'c', juego).block();


        //RONDA 2 DE ULTIMATE
        //adivinan por turno

        jugador2 = jugadorService.buscarJugador("j2").block();
        jugador3 = jugadorService.buscarJugador("j3").block();
        jugador1 = jugadorService.buscarJugador("j1").block();


        assertTrue(jugador1.isEsMiTurno());


        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador2, 'd', juego).block());
        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador3, 'd', juego).block());
        jugadorService.adivinarLetra(jugador1, 'd', juego).block();

        jugador2 = jugadorService.buscarJugador("j2").block();
        jugador3 = jugadorService.buscarJugador("j3").block();
        jugador1 = jugadorService.buscarJugador("j1").block();

        assertFalse(jugador1.isEsMiTurno());
        assertTrue(jugador2.isEsMiTurno());

        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador1, 'e', juego).block());
        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador3, 'e', juego).block());
        jugadorService.adivinarLetra(jugador2, 'e', juego).block();

        jugador2 = jugadorService.buscarJugador("j2").block();
        jugador3 = jugadorService.buscarJugador("j3").block();
        jugador1 = jugadorService.buscarJugador("j1").block();

        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador2, 'f', juego).block());
        assertThrows(NoEsTuTurnoException.class, () -> jugadorService.adivinarLetra(jugador1, 'f', juego).block());
        jugadorService.adivinarLetra(jugador3, 'f', juego).block();

        //los borro
        jugadorService.borrarJugador("j1");
        jugadorService.borrarJugador("j2");
        jugadorService.borrarJugador("j3");
        juegoService.eliminarJuego(juego);
    }



    @AfterEach
    void tearDown(){
        jugadorService.borrarJugador("Juan");
    }
}
