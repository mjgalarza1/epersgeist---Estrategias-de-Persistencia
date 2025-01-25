package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.exception.accionInvalida.InerseccionEntrePoligonosException;
import ar.edu.unq.epersgeist.exception.accionInvalida.PoligonoInvalidoException;
import ar.edu.unq.epersgeist.exception.notFound.PoligonoNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EpersgeistApplication.class)
public class PoligonoServiceImplTest {

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private PoligonoService poligonoService;

    @Autowired
    private EspirituService espirituService;


    private Poligono  ciudadela;
    private Poligono  pehuajo;
    private Ubicacion capilla;
    private Ubicacion santuario;

    @BeforeEach
    void setUp(){
        capilla = new Santuario("Capilla", 100);
        santuario = new Santuario("Santuario", 100);
        ciudadela = new Poligono(List.of(
                new Point(1.0, 1.0),
                new Point(1.0, 1.2),
                new Point(1.2, 1.2),
                new Point(1.2, 1.0),
                new Point(1.0, 1.0)
        ));

        pehuajo = new Poligono(List.of(
                new Point(1.3, 1.3),
                new Point(1.3, 1.5),
                new Point(1.5, 1.5),
                new Point(1.5, 1.3),
                new Point(1.3, 1.3)
        ));

        poligonoService.crear(ciudadela);
        poligonoService.crear(pehuajo);
        ubicacionService.crear(capilla, ciudadela);
        ubicacionService.crear(santuario, pehuajo);
    }

    //CREAR

    @Test
    void crearPoligonoCiudadela() {
        ciudadela = poligonoService.recuperar(ciudadela.getId());
        assertEquals(ciudadela.getPosicion().getPoints(),
                List.of(
                new Point(1.0, 1.0),
                new Point(1.0, 1.2),
                new Point(1.2, 1.2),
                new Point(1.2, 1.0),
                new Point(1.0, 1.0)
        ));
    }

    @Test
    void crearPoligonoPeroNoCierra() {
        Poligono poligonoInvalido = new Poligono(List.of(
                new Point(1.0, 1.0),
                new Point(1.0, 1.2),
                new Point(1.2, 1.2),
                new Point(1.2, 1.0),
                new Point(1.1, 1.1)
        ));

        assertThrows(PoligonoInvalidoException.class, () -> poligonoService.crear(poligonoInvalido));
    }

    @Test
    void crearPeroNoTieneFormaDePoligono() {
        Poligono poligonoInvalido = new Poligono(List.of(
                new Point(-10.0, -10.0),
                new Point(-10.0, -10.2),
                new Point(-10.0, -10.0)
        ));

        assertThrows(PoligonoInvalidoException.class, () -> poligonoService.crear(poligonoInvalido));
    }

    @Test
    void crearPeroHayMasRepetidos() {
        Poligono poligonoInvalido = new Poligono(List.of(
                new Point(10.0, 10.0),
                new Point(20.0, 20.0),
                new Point(20.0, 20.0),
                new Point(10.0, 10.0)
        ));

        assertThrows(PoligonoInvalidoException.class, () -> poligonoService.crear(poligonoInvalido));
    }

    @Test
    void crearPeroLasCoordenadasEstanFueraDeRango() {
        Poligono poligonoInvalido = new Poligono(List.of(
                new Point(1000.0, 1000.0),
                new Point(2000.0, 2000.0),
                new Point(2000.0, 2000.0),
                new Point(1000.0, 1000.0)
        ));

        assertThrows(PoligonoInvalidoException.class, () -> poligonoService.crear(poligonoInvalido));
    }

    @Test
    void crearEnLasMismasCoordenadasException() {
        Poligono poligono = new Poligono(List.of(
                new Point(-1.3, 1.3),
                new Point(-1.3, 1.5),
                new Point(-1.5, 1.5),
                new Point(-1.5, 1.3),
                new Point(-1.3, 1.3)
        ));

        poligonoService.crear(poligono);

        Poligono poligonoConMismasCoordenadas = new Poligono(List.of(
                new Point(-1.3, 1.3),
                new Point(-1.3, 1.5),
                new Point(-1.5, 1.5),
                new Point(-1.5, 1.3),
                new Point(-1.3, 1.3)
        ));

        assertThrows(InerseccionEntrePoligonosException.class, () -> poligonoService.crear(poligonoConMismasCoordenadas));
        poligonoService.eliminar(poligono.getId());
    }

    @Test
    void crearConPoligonosQueIntersectanException() {
        poligonoService.eliminar(ciudadela.getId());
        poligonoService.eliminar(pehuajo.getId());

        Poligono poligono = new Poligono(List.of(
                new Point(24.797, -9.896),
                new Point(24.797, -9.903),
                new Point(24.809, -9.903),
                new Point(24.809, -9.896),
                new Point(24.797, -9.896)
        ));

        poligonoService.crear(poligono);

        Poligono poligonoQueIntersecta = new Poligono(List.of(
                new Point(24.8068, -9.8980),
                new Point(24.8068, -9.9051),
                new Point(24.8153, -9.9051),
                new Point(24.8153, -9.8980),
                new Point(24.8068, -9.8980)
        ));

        assertThrows(InerseccionEntrePoligonosException.class, () -> poligonoService.crear(poligonoQueIntersecta));
        poligonoService.eliminar(poligono.getId());
    }

    @Test
    void recuperar(){
        Poligono poligono = new Poligono(List.of(
                new Point(24.8068, -9.8980),
                new Point(24.8068, -9.9051),
                new Point(24.8153, -9.9051),
                new Point(24.8153, -9.8980),
                new Point(24.8068, -9.8980)
        ));

        poligonoService.crear(poligono);

        assertEquals(poligono, poligonoService.recuperar(poligono.getId()));
        poligonoService.eliminar(poligono.getId());
    }

    @Test
    void recuperarInvalido(){
        assertThrows(PoligonoNoEncontradoException.class, () ->poligonoService.recuperar("123L"));
    }

    @Test
    void eliminar(){
        poligonoService.eliminar(ciudadela.getId());
        assertThrows(PoligonoNoEncontradoException.class, () ->poligonoService.recuperar(ciudadela.getId()));
    }

    @Test
    void recuperarTodos(){
        List<Poligono> poligonos = poligonoService.recuperarTodos();
        assertTrue(poligonos.contains(ciudadela));
        assertTrue(poligonos.contains(pehuajo));
    }

    @Test
    void recuperarTodosSinPoligonosPersistidos(){
        poligonoService.eliminar(pehuajo.getId());
        poligonoService.eliminar(ciudadela.getId());

        List<Poligono> poligonos = poligonoService.recuperarTodos();

        assertTrue(poligonos.isEmpty());
    }

    @Test
    void actualizar(){
        ciudadela.setPosicion(new GeoJsonPolygon(List.of(
                new Point(1.0, 1.0),
                new Point(1.0, 1.2),
                new Point(1.2, 1.0),
                new Point(1.0, 1.0)
        )));

        poligonoService.actualizar(ciudadela);
        assertEquals(List.of(
                new Point(1.0, 1.0),
                new Point(1.0, 1.2),
                new Point(1.2, 1.0),
                new Point(1.0, 1.0)
        ), ciudadela.getPosicion().getPoints());
    }

    //UBICACIONES

    @Test
    void lasUbicacionesNoEstanPersistidas(){
        assertThrows(PoligonoNoEncontradoException.class, () -> poligonoService.existeUbicacionEnRango(123L, santuario.getId(), 0.0, 100.0));
        assertThrows(PoligonoNoEncontradoException.class, () -> poligonoService.existeUbicacionEnRango(santuario.getId(), 123L, 0.0, 100.0));
    }

    @Test
    void existeUbicacionEnRangoNoFuncionaPorCantidadDeKmsNegativos(){
        assertThrows(IllegalArgumentException.class, () -> poligonoService.existeUbicacionEnRango(capilla.getId(), santuario.getId(), -10.0, 100.0));
        assertThrows(IllegalArgumentException.class, () -> poligonoService.existeUbicacionEnRango(capilla.getId(), santuario.getId(), 0.0, -100.0));
    }

    //A MENOS DE

    @Test
    void lasUbicacionesEstanAMenosDe100km(){
        assertTrue(poligonoService.existeUbicacionEnRango(capilla.getId(), santuario.getId(), 0.0, 100.0));
    }

    @Test
    void lasUbicacionesNoEstanAMenosDe100km(){
        Ubicacion ubi3 = new Santuario("Santuario 3", 100);
        Poligono pol3 = new Poligono(List.of(
                new Point(2.0, 2.0),
                new Point(2.0, 2.2),
                new Point(2.2, 2.2),
                new Point(2.2, 2.0),
                new Point(2.0, 2.0)
        ));

        poligonoService.crear(pol3);
        ubicacionService.crear(ubi3,pol3);

        assertFalse(poligonoService.existeUbicacionEnRango(capilla.getId(), ubi3.getId(), 0.0, 100.0));

        poligonoService.eliminar(pol3.getId());
        ubicacionService.eliminar(ubi3.getId());

    }

    //A MAS DE
    @Test
    void lasUbicacionesEstanAMasDe2km(){
        assertTrue(poligonoService.existeUbicacionEnRango(capilla.getId(), santuario.getId(), 2.0, 100.0));
    }

    @Test
    void lasUbicacionesNoEstanAMasDe2km(){
        Ubicacion estrella = new Santuario("Estrella", 100);
        Poligono p_estrella = new Poligono(List.of(
                new Point(24.797, -9.896),
                new Point(24.797, -9.903),
                new Point(24.809, -9.903),
                new Point(24.809, -9.896),
                new Point(24.797, -9.896)
        ));

        poligonoService.crear(p_estrella);
        ubicacionService.crear(estrella, p_estrella);

        Ubicacion planeta = new Santuario("Planeta", 100);
        Poligono p_planeta = new Poligono(List.of(
                new Point(24.8102, -9.8959),
                new Point(24.8102, -9.9037),
                new Point(24.8217, -9.9037),
                new Point(24.8217, -9.8959),
                new Point(24.8102, -9.8959)
        ));

        poligonoService.crear(p_planeta);
        ubicacionService.crear(planeta, p_planeta);

        assertFalse(poligonoService.existeUbicacionEnRango(planeta.getId(), estrella.getId(), 2.0, 100.0));

        poligonoService.eliminar(p_estrella.getId());
        poligonoService.eliminar(p_planeta.getId());
        ubicacionService.eliminar(estrella.getId());
        ubicacionService.eliminar(planeta.getId());
    }

    @AfterEach
    void cleanUp(){
        ubicacionService.eliminar(capilla.getId());
        ubicacionService.eliminar(santuario.getId());
        poligonoService.eliminar(ciudadela.getId());
        poligonoService.eliminar(pehuajo.getId());
    }

}