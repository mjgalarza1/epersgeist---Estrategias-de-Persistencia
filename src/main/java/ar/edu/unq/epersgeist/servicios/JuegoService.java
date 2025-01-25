package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Juego;
import ar.edu.unq.epersgeist.modelo.Jugador;

public interface JuegoService {
    void crearJuego(Juego juego);
    Juego recuperarJuego(Long id);
    void eliminarJuego(Juego juego);
    void actualizarJuego(Juego juego);
    int cantIntentosRestantes(Long id);
    String palabraAdivinando(Long id);
    Jugador empezarJuego(String nombre);
    String letrasEquivocadas(Long id);
    Juego empezarRondaUltimate(Jugador j1, Jugador j2, Jugador j3, Long idJuego);
    String rondaActual(Long id);
    void pasarALaSiguienteRonda(Long idJuego);
}
