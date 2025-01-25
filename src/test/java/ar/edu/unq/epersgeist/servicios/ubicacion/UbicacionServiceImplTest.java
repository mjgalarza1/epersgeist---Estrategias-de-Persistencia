package ar.edu.unq.epersgeist.servicios.ubicacion;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.exception.notFound.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.PoligonoService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes =  EpersgeistApplication.class)
public class UbicacionServiceImplTest {


    @Autowired
    private UbicacionService serviceUbicacion;
    @Autowired
    private EspirituService serviceEspiritu;
    @Autowired
    private MediumService serviceMedium;
    @Autowired
    private PoligonoService poligonoService;

    private Poligono poligonoUbicacion;

    private Ubicacion ubicacion;

    private Medium unMedium;
    private Medium otroMedium;

    private Espiritu espDemon;
    private Espiritu espDemon2;
    private Espiritu espAngel;
    private Espiritu  espAngel2;

    @BeforeEach
    void setUp() {
        //POLIGONOS
        poligonoUbicacion = new Poligono((List.of(
                new Point(0, 0),
                new Point(3, 6),
                new Point(6, 1),
                new Point(0, 0)
        )));

        poligonoService.crear(poligonoUbicacion);

        //UBICACION
        ubicacion = new Cementerio("SUIZA",99);
        serviceUbicacion.crear(ubicacion, poligonoUbicacion);

        espDemon = new EspirituDemoniaco(10, "Bruno", 10, ubicacion);
        espDemon2 = new EspirituDemoniaco(10, "Carlos", 12, ubicacion);
        espAngel = new EspirituAngelical(10, "Daniel", 12, ubicacion);
        espAngel2 = new EspirituAngelical(10, "Esteban", 12, ubicacion);

        unMedium = new Medium("Pepe", 200, 100, ubicacion);
        otroMedium = new Medium("Violeta", 500, 500, ubicacion);

        serviceEspiritu.crear(espDemon);
        serviceEspiritu.crear(espDemon2);
        serviceEspiritu.crear(espAngel);
        serviceEspiritu.crear(espAngel2);

        serviceMedium.crear(unMedium);
        serviceMedium.crear(otroMedium);

    }

    @Test
    void recuperarUbicaciones() {
        List<Ubicacion> ubis = serviceUbicacion.recuperarTodos();
        assertEquals(1, ubis.size());
    }


    @Test
    void eliminarUbicacionInexistente() {
        Long idInexistente = 123L;
        assertDoesNotThrow(()-> serviceUbicacion.eliminar(idInexistente));
    }

    @Test
    void recuperarUbicacionInexistente() {
        Long idInexistente = 123L;
        assertThrows(UbicacionNoEncontradaException.class, () -> {
            serviceUbicacion.recuperar(idInexistente);
        });
    }

    @Test
    void ubicacionPersistidaCorrectamente() {
        assertNotNull(ubicacion);
    }

    @Test
    void espiritusEnIdInexistente(){
        Long idInexistente = 123L;

        assertTrue(serviceUbicacion.espiritusEn(idInexistente).isEmpty());
    }

    @Test
    void espiritusEnUbiSinEspiritus(){
        Poligono poligonoUbiVacia = new Poligono((List.of(
                new Point(-10, -10),
                new Point(-13, -16),
                new Point(-16, -11),
                new Point(-10, -10)
        )));
        poligonoService.crear(poligonoUbiVacia);
        Ubicacion ubiVacia = new Cementerio("La Pampa",99);
        serviceUbicacion.crear(ubiVacia,poligonoUbiVacia);

        assertTrue(serviceUbicacion.espiritusEn(ubiVacia.getId()).isEmpty());

        poligonoService.eliminar(poligonoUbiVacia.getId());
        serviceUbicacion.eliminar(ubiVacia.getId());
    }


    @Test
    void mediumsSinEspiritusEnUbicacionVacia(){
        Poligono poligonoUbiVacia = new Poligono((List.of(
                new Point(55, 55),
                new Point(55, 57),
                new Point(60, 57),
                new Point(55, 55)
        )));
        poligonoService.crear(poligonoUbiVacia);

        Ubicacion ubiVacia = new Cementerio("La Pampa",99);
        serviceUbicacion.crear(ubiVacia,poligonoUbiVacia);

        assertTrue(serviceUbicacion.mediumsSinEspiritusEn(ubiVacia.getId()).isEmpty());
        serviceUbicacion.eliminar(ubiVacia.getId());
        poligonoService.eliminar(poligonoUbiVacia.getId());
    }

    @Test
    void mediumsSinEspiritusEnUbicacionConMediums(){
        List<Medium> medium = serviceUbicacion.mediumsSinEspiritusEn(ubicacion.getId());

        assertEquals(2,medium.size());
    }


    @Test
    void  mediumsSinEspiritusEnIdInexistente(){
        Long idInexistente = 123L;

        assertTrue(serviceUbicacion.mediumsSinEspiritusEn(idInexistente).isEmpty());
    }

    @Test
    void recuperarUbicacion() {
        Ubicacion ubiRecuperada = serviceUbicacion.recuperar(ubicacion.getId());
        assertEquals(ubicacion, ubiRecuperada);
    }


    @Test
    void actualizarUbicacionAEcuador() {
        ubicacion.setNombre("Ecuador");
        serviceUbicacion.actualizar(ubicacion);
        Ubicacion ubiRecuperada = serviceUbicacion.recuperar(ubicacion.getId());

        assertEquals("Ecuador", ubiRecuperada.getNombre());
    }

    @Test
    void eliminarUbicacion() {
        serviceUbicacion.eliminar(ubicacion.getId());
        assertThrows(UbicacionNoEncontradaException.class, () -> serviceUbicacion.recuperar(ubicacion.getId()));
}

    @Test
    void espiritusEnUbicacion() {
        List<Espiritu> esps = serviceUbicacion.espiritusEn(ubicacion.getId());
        assertEquals(4, esps.size());
    }

    @AfterEach
    void cleanUp() {
        poligonoService.eliminar(poligonoUbicacion.getId());

        serviceEspiritu.eliminar(espAngel.getId());
        serviceEspiritu.eliminar(espAngel2.getId());
        serviceEspiritu.eliminar(espDemon.getId());
        serviceEspiritu.eliminar(espDemon2.getId());

        serviceMedium.eliminar(unMedium.getId());
        serviceMedium.eliminar(otroMedium.getId());

        serviceUbicacion.eliminar(ubicacion.getId());
    }
}
