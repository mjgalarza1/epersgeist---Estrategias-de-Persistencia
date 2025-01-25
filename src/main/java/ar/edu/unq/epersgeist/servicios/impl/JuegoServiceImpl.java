package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.accionInvalida.RondaSinTerminarException;
import ar.edu.unq.epersgeist.modelo.Juego;
import ar.edu.unq.epersgeist.modelo.Jugador;
import ar.edu.unq.epersgeist.modelo.PalabraRondaUltimate;
import ar.edu.unq.epersgeist.modelo.ronda.Ronda;
import ar.edu.unq.epersgeist.modelo.ronda.RondaUltimate;
import ar.edu.unq.epersgeist.persistencia.dao.JuegoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.PalabraRondaUltimateDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.JugadorDAOImpl;
import ar.edu.unq.epersgeist.servicios.JuegoService;
import ar.edu.unq.epersgeist.servicios.JugadorService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ar.edu.unq.epersgeist.exception.notFound.JuegoNoEncontradoException;

@Service
@Transactional
public class JuegoServiceImpl implements JuegoService {

    private final JuegoDAO juegoDAO;
    private final JugadorService jugadorService;
    private final PalabraRondaUltimateDAO palabraDAO;

    public JuegoServiceImpl(JuegoDAO juegoDAO, JugadorDAOImpl jugadorDAO, JugadorService jugadorService, PalabraRondaUltimateDAO palabraDAO) {
        this.juegoDAO = juegoDAO;
        this.jugadorService = jugadorService;
        this.palabraDAO = palabraDAO;
    }

    @Override
    public void crearJuego(Juego juego) {
        juegoDAO.save(juego);
    }

    @Override
    public Juego recuperarJuego(Long id) {
        return juegoDAO.findById(id).orElseThrow(JuegoNoEncontradoException::new);
    }

    @Override
    public void eliminarJuego(Juego juego) {
        juegoDAO.delete(juego);
    }

    @Override
    public void actualizarJuego(Juego juego) {
        juegoDAO.save(juego);
    }

    @Override
    public int cantIntentosRestantes(Long id) {
        return juegoDAO.cantIntentosRestantes(id);
    }

    @Override
    public String palabraAdivinando(Long id) {
        return juegoDAO.palabraAdivinando(id);
    }

    @Override
    public String letrasEquivocadas(Long id) {
        return juegoDAO.letrasEquivocadas(id);
    }

    @Override
    public String rondaActual(Long id) {
        Ronda ronda = juegoDAO.rondaActualDe(id);
        return ronda.getClass().getSimpleName();
    }

    @Override
    public Jugador empezarJuego(String nombreJugador) {
        //crea el juego y lo persiste
        Juego juego = new Juego();
        juegoDAO.save(juego);

        //crea el jugador y lo persiste con el id del juego recien creado
        Jugador jugador = new Jugador(nombreJugador);
        jugadorService.crearJugador(jugador, juego.getId()).subscribe();

        //retorna el jugador
        return jugador;
    }

    @Override
    public Juego empezarRondaUltimate(Jugador j1, Jugador j2, Jugador j3, Long idJuego){
        Juego juego = recuperarJuego(idJuego);
        Ronda rondaUltimate = new RondaUltimate(j1,j2,j3);
        juego.setRondaActual(rondaUltimate);
        actualizarJuego(juego);

        PalabraRondaUltimate palabra = new PalabraRondaUltimate(juego.getPalabraAdivinando(), juego.getLetrasUsadas());
        palabraDAO.crearPalabraAdivinando(palabra);

        j1.setIds(idJuego, palabra.getId());
        j2.setIds(idJuego, palabra.getId());
        j3.setIds(idJuego, palabra.getId());

        // Actualiza a los jugadores en la base de datos
        jugadorService.actualizar(j1).subscribe();
        jugadorService.actualizar(j2).subscribe();
        jugadorService.actualizar(j3).subscribe();
        return juego;
    }

    @Override
    public void pasarALaSiguienteRonda(Long idJuego){
        Juego juego = recuperarJuego(idJuego);
        juego.cambiarProximaRonda();
        juegoDAO.save(juego);
    }

}
