package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.modelo.Juego;
import ar.edu.unq.epersgeist.modelo.Jugador;
import ar.edu.unq.epersgeist.modelo.PalabraRondaUltimate;
import ar.edu.unq.epersgeist.persistencia.dao.impl.JugadorDAOImpl;
import ar.edu.unq.epersgeist.persistencia.dao.impl.PalabraRondaUltimateDAOImpl;
import ar.edu.unq.epersgeist.servicios.impl.JuegoServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.JugadorServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.PalabraRondaUltimateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EpersgeistApplication.class)
public class PalabraRondaUltimateServiceTest {
    @Autowired
    private PalabraRondaUltimateDAOImpl palabraDao;
    @Autowired
    private PalabraRondaUltimateServiceImpl palabraService;
    @Autowired
    private JugadorServiceImpl jugadorService;
    @Autowired
    private JugadorDAOImpl jugadordao;
    @Autowired
    private JuegoServiceImpl juegoService;
    private PalabraRondaUltimate palabra;
    private Jugador jugador;
    private Juego juegoRecuperado;

    @BeforeEach
    void setUp(){
        palabra = new PalabraRondaUltimate("Monstruoso");
        palabraService.crearPalabraAdivinando(palabra);
        jugador = juegoService.empezarJuego("Susana");
        juegoRecuperado = juegoService.recuperarJuego(jugador.getIdJuego());

        juegoRecuperado.getRondaActual().settearPalabraAAdivinar("alma");
        juegoService.actualizarJuego(juegoRecuperado);
    }

    @Test
    void crearPalabraAdivinando(){
        assertEquals("Monstruoso", palabraDao.recuperar(palabra.getId()).block().getPalabraAdivinando());

        jugadorService.borrarJugador("Susana");
        juegoService.eliminarJuego(juegoRecuperado);
    }

}
