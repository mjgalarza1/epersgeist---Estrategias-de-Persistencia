package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Jugador;
import com.google.cloud.firestore.ListenerRegistration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;

public interface JugadorDAO {
    Mono<Void> crearJugador(Jugador jugador);
    Mono<Jugador> recuperarJugador(String nombre);
    Mono<Jugador> actualizarJugador(Jugador jugador);
    void borrarJugador(String nombre);
    Mono<Integer> obtenerPuntaje(String nombre);
    Flux<Jugador> obtenerRanking();
    List<Jugador> obtenerTop();
}
