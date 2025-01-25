package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.accionInvalida.JugadorDuplicadoException;
import ar.edu.unq.epersgeist.exception.notFound.JugadorNoEncontradoException;
import ar.edu.unq.epersgeist.exception.notFound.RondaNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Juego;
import ar.edu.unq.epersgeist.modelo.Jugador;
import ar.edu.unq.epersgeist.modelo.PalabraRondaUltimate;
import ar.edu.unq.epersgeist.modelo.ronda.RondaUltimate;
import ar.edu.unq.epersgeist.persistencia.dao.JuegoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.PalabraRondaUltimateDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.JugadorDAOImpl;
import ar.edu.unq.epersgeist.persistencia.dao.impl.PalabraRondaUltimateDAOImpl;
import ar.edu.unq.epersgeist.servicios.JugadorService;
import com.google.cloud.firestore.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class JugadorServiceImpl implements JugadorService {

    private final JugadorDAOImpl jugadorDAO;
    private final JuegoDAO juegoDAO;
    private final PalabraRondaUltimateDAOImpl palabraDAO;

    public JugadorServiceImpl(JugadorDAOImpl jugadorDAO, JuegoDAO juegoDAO, PalabraRondaUltimateDAOImpl palabraDAO) {
        this.jugadorDAO = jugadorDAO;
        this.juegoDAO = juegoDAO;
        this.palabraDAO = palabraDAO;
    }


//    Mono: Representa un flujo de datos que puede emitir 0 o 1 valor y luego completarse o emitir un error. Es ideal
//    para operaciones que producen un único resultado, como consultas a bases de datos para obtener un registro.
    @Override
    public Mono<Void> crearJugador(Jugador jugador, Long id) {
        jugador.setIdJuego(id);
        return jugadorDAO.crearJugador(jugador)
                .onErrorResume(JugadorDuplicadoException.class, Mono::error);
    }

    @Override
    public Mono<Jugador> buscarJugador(String nombre) {
        return jugadorDAO.recuperarJugador(nombre).switchIfEmpty(Mono.error(new JugadorNoEncontradoException(nombre)));
    }

    @Override
    public Flux<Jugador> obtenerRanking() {
        return jugadorDAO.obtenerRanking();
    }

    @Override
    public Mono<Jugador> adivinarLetra(Jugador jugador, Character letra, Juego juego) {
        // se adivina
        jugador.adivinarLetra(letra, juego);

        //se cambia de turno si se puede al siguiente
        this.cambiarTurnoDelSiguienteSiExiste(jugador, juego);

        // se actualiza el juego
        juegoDAO.save(juego);

        // se actualiza la palabraDeLaRonda
        this.actualizarPalabraDeRondaUltimate(jugador, juego);

        //por ultimo se actualiza el jugador
        return actualizar(jugador);
    }

    private void actualizarPalabraDeRondaUltimate(Jugador jugador, Juego juego) {
        try {
            String id = jugador.getIdPalabraRondaUltimate();
            if (id == null){
                crearSiNoExiste(id, jugador, juego);
                return;
            }
            actualizarConLaUltimaPalabraAdivinando(id, jugador, juego);
        } catch(RondaNoEncontradaException e) {
            return; //no se hace nada
        }
    }

    private void actualizarConLaUltimaPalabraAdivinando(String id, Jugador jugador, Juego juego){
        PalabraRondaUltimate palabra = palabraDAO.recuperar(id).block();
        if (palabra != null){
            palabra.setPalabraAdivinando(juego.getPalabraAdivinando());
            palabra.setLetrasUsadas(juego.getLetrasUsadas());
            palabraDAO.actualizarPalabraAdivinando(palabra).subscribe();
        }
        jugador.setIdPalabraRondaUltimate(id);
    }

    private void crearSiNoExiste(String id, Jugador jugador, Juego juego){
            // crear si no existe
            PalabraRondaUltimate palabraNueva = new PalabraRondaUltimate(juego.getPalabraAdivinando());
            palabraDAO.crearPalabraAdivinando(palabraNueva).subscribe();
            jugador.setIdPalabraRondaUltimate(palabraNueva.getId());

    }

    private void cambiarTurnoDelSiguienteSiExiste(Jugador jugador, Juego juego) {
        if( jugador.getJugadorSiguiente() != null) {
            Jugador jugadorSiguiente = jugadorDAO.recuperarJugador(jugador.getJugadorSiguiente()).block();
            juego.cambiarTurnoA(jugador, jugadorSiguiente);
            actualizar(jugadorSiguiente).block();
        }
    }

    @Override
    public void borrarJugador(String nombre) {
        jugadorDAO.borrarJugador(nombre);
    }

    @Override
    public Mono<Jugador> actualizar(Jugador jugador) {
        return jugadorDAO.actualizarJugador(jugador)
                .onErrorMap(InterruptedException.class, e -> new RuntimeException("Operación interrumpida", e))
                .onErrorMap(ExecutionException.class, Throwable::getCause);
    }

    @Override
    public Mono<Integer> obtenerPuntaje(String nombre) {
        return jugadorDAO.recuperarJugador(nombre)
                .switchIfEmpty(Mono.error(new JugadorNoEncontradoException(nombre)))
                .map(Jugador::getPuntuacion);
    }

    @Override
    public void detenerRanking() {
        jugadorDAO.detenerRanking();
    }

    @Override
    public List<Jugador> obtenerTop() {
        return jugadorDAO.obtenerTop();
    }



}

