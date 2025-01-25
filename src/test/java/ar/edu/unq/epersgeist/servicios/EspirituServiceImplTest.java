package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.exception.accionInvalida.*;
import ar.edu.unq.epersgeist.exception.notFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.exception.notFound.EspirituOMediumNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@SpringBootTest(classes = EpersgeistApplication.class)
public class EspirituServiceImplTest {

    @Autowired
    private EspirituService serviceEspiritu;
    @Autowired
    private UbicacionService serviceUbicacion;
    @Autowired
    private MediumService serviceMedium;
    @Autowired
    private PoligonoService poligonoService;

    private Poligono poligono1;
    private Poligono poligono2;
    private Poligono poligonoDominante;
    private Poligono poligonoADominar;

    private Medium medium;

    private Espiritu espDemon;
    private Espiritu espDemon2;
    private Espiritu espAngel;
    private Espiritu espAngel2;
    private Espiritu espDominante;
    private Espiritu espADominar;

    private Ubicacion ubi1;
    private Ubicacion ubi2;
    private Ubicacion ubiDominante;
    private Ubicacion ubiADominar;

    @BeforeEach
    void crearModelo() {
        //POLIGONOS
        poligono1 = new Poligono((List.of(
                new Point(0, 0),
                new Point(3, 6),
                new Point(6, 1),
                new Point(0, 0)
        )));
        poligono2 = new Poligono((List.of(
                new Point(10, 10),
                new Point(13, 16),
                new Point(16, 11),
                new Point(10, 10)
        )));
        poligonoDominante = new Poligono(List.of(
                new Point(81.035, 81.035),
                new Point(80.0, 81.0),
                new Point(80.0, 79.0),
                new Point(81.0, 79.0),
                new Point(81.035, 81.035)
        ));

        poligonoADominar = new Poligono(List.of(
                new Point(81.063, 81.063),
                new Point(81.073, 81.063),
                new Point(81.073, 81.083),
                new Point(81.063, 81.083),
                new Point(81.063, 81.063)
        ));

        poligonoService.crear(poligono1);
        poligonoService.crear(poligono2);
        poligonoService.crear(poligonoDominante);
        poligonoService.crear(poligonoADominar);

        //UBICACIONES
        ubi1 = new Cementerio("Peru", 99);
        ubi2 = new Cementerio("Canada",99);
        ubiDominante = new Cementerio("España", 99);
        ubiADominar = new Cementerio("Portugal",99);

        serviceUbicacion.crear(ubi1,poligono1);
        serviceUbicacion.crear(ubi2,poligono2);
        serviceUbicacion.crear(ubiDominante,poligonoDominante);
        serviceUbicacion.crear(ubiADominar,poligonoADominar);

        medium = new Medium("Juanito", 100, 20, ubi1);

        espDemon = new EspirituDemoniaco(10, "Bruno", 10, ubi1);
        espDemon2 = new EspirituDemoniaco(10, "Carlos", 12, ubi2);
        espAngel = new EspirituAngelical(10, "Daniel", 12, ubi1);
        espAngel2 = new EspirituAngelical(10, "Esteban", 12, ubi2);
        espDominante = new EspirituDemoniaco(30, "Juan", 20, ubiDominante);
        espADominar = new EspirituDemoniaco(33, "Pepe", 25, ubiADominar);

        serviceMedium.crear(medium);

        serviceEspiritu.crear(espDemon);
        serviceEspiritu.crear(espDemon2);
        serviceEspiritu.crear(espAngel);
        serviceEspiritu.crear(espAngel2);
        serviceEspiritu.crear(espDominante);
        serviceEspiritu.crear(espADominar);
    }

    @Test
    void espiritusPersistidosCorrectamente() {

        assertNotNull(serviceEspiritu.recuperar(espDemon.getId()));
        assertNotNull(serviceEspiritu.recuperar(espDemon2.getId()));
        assertNotNull(serviceEspiritu.recuperar(espAngel.getId()));
        assertNotNull(serviceEspiritu.recuperar(espAngel2.getId()));
    }

    @Test
    void recuperarEspirituDemoniaco() {
        Espiritu espRecuperado = serviceEspiritu.recuperar(espDemon.getId());

        assertEquals(espDemon, espRecuperado);
    }


    @Test
    void crearEspirituInexistente() {
        Long idInexistente = 123L;
        Espiritu esp = mock(Espiritu.class);
        when(esp.getId()).thenReturn(idInexistente);
        assertThrows(RuntimeException.class, ()-> serviceEspiritu.crear(esp));
    }


    @Test
    void actualizarEspirituDemoniaco() {
        espDemon.setNombre("Beto");
        serviceEspiritu.actualizar(espDemon);
        Espiritu espRecuperado = serviceEspiritu.recuperar(espDemon.getId());

        assertEquals("Beto", espRecuperado.getNombre());
    }

    @Test
    void eliminarEspirituDemoniaco() {
        serviceEspiritu.eliminar(espDemon.getId());

        assertThrows(EspirituNoEncontradoException.class, () -> serviceEspiritu.recuperar(espDemon.getId()));
    }

    @Test
    void recuperarEspiritusDemoniaciosDeLaPrimerPaginaDeFormaDescendentePorEnergia() {
        serviceEspiritu.eliminar(espDominante.getId());
        serviceEspiritu.eliminar(espADominar.getId());
        List<Espiritu> espDemons = serviceEspiritu.espiritusDemoniacos(Direccion.DESCENDENTE, 1, 2);

        assertEquals(12, espDemons.get(0).getEnergia());
        assertEquals(10, espDemons.get(1).getEnergia());
        assertEquals(2, espDemons.size());
    }

    @Test
    void recuperarEspiritusDemoniaciosDeLaSegundaPaginaDeFormaAscendentePorEnergia() {
        List<Espiritu> espDemons = serviceEspiritu.espiritusDemoniacos(Direccion.ASCENDENTE, 2, 2);

        assertEquals(20, espDemons.get(0).getEnergia());
        assertEquals(25, espDemons.get(1).getEnergia());
    }

    @Test
    void recuperarEspiritusDemoniaciosDePaginaInexistente() {
        List<Espiritu> espDemons = serviceEspiritu.espiritusDemoniacos(Direccion.DESCENDENTE, 3, 3);
        assertTrue(espDemons.isEmpty());
    }

    @Test
    void recuperarEspiritusDemoniaciosDePaginaNegativa() {
        assertThrows(PaginaOCantidadNegativaException.class, () -> serviceEspiritu.espiritusDemoniacos(Direccion.DESCENDENTE, -3, 3));
    }

    @Test
    void conectarMediumConEspDemon(){
        System.out.println("El medium:" + medium.getNombre());
        medium = serviceEspiritu.conectar(espDemon.getId(), medium.getId());

        Espiritu espDemonRecuperado = serviceEspiritu.recuperar(espDemon.getId());

        assertEquals(espDemonRecuperado.getMedium(), medium);
        assertEquals(1,medium.getEspiritus().size());
    }

    @Test
    void conectarExceptionMediumConEspDemon(){
        espDemon.setUbicacion(ubi2);
        espDemon.setMedium(medium);
        serviceEspiritu.actualizar(espDemon);

        assertThrows(ConectarException.class,() -> serviceEspiritu.conectar(espDemon.getId(), medium.getId()));
    }

    @Test
    void conectarExceptionMediumConEspDominado(){
        medium.setUbicacion(ubiADominar);
        serviceMedium.actualizar(medium);
        serviceEspiritu.dominar(espDominante.getId(), espADominar.getId());

        assertThrows(EspirituDominadoException.class,() -> serviceEspiritu.conectar(espADominar.getId(), medium.getId()));
    }

    @Test
    void conectarConMediumInexistente(){
        Long idInexistenteMedium = 123L;

        assertThrows(EspirituOMediumNoEncontradoException.class,() -> serviceEspiritu.conectar(espDemon.getId(), idInexistenteMedium));
    }

    @Test
    void conectarConEspirituInexistente(){
        Long idInexistenteEspiritu = 123L;

        assertThrows(EspirituOMediumNoEncontradoException.class,() -> serviceEspiritu.conectar(idInexistenteEspiritu, medium.getId()));
    }

    @Test
    void recuperarEspirituDemoniacoInexistente() {
        Long idInexistente = 123L;

        assertThrows(EspirituNoEncontradoException.class, () -> serviceEspiritu.recuperar(idInexistente));
    }

    @Test
    void recuperarEspiritusDemoniaciosConLaBaseDeDatosVacia() {
        serviceEspiritu.eliminar(espAngel.getId());
        serviceEspiritu.eliminar(espAngel2.getId());
        serviceEspiritu.eliminar(espDemon.getId());
        serviceEspiritu.eliminar(espDemon2.getId());
        serviceEspiritu.eliminar(espDominante.getId());
        serviceEspiritu.eliminar(espADominar.getId());

        List<Espiritu> espDemons = serviceEspiritu.espiritusDemoniacos(Direccion.ASCENDENTE, 1, 1);

        assertTrue(espDemons.isEmpty());
    }

    @Test
    void eliminarEspirituNoExistente() {
        Long idInexistente = 123L;

        assertDoesNotThrow(() -> serviceEspiritu.eliminar(idInexistente));
    }

    @Test
    void recuperarEspiritus() {
        List<Espiritu> esps = serviceEspiritu.recuperarTodos();
        assertEquals(6, esps.size());
    }

    @Test
    void dominarEspirituDeFormaExitosa(){
        serviceEspiritu.dominar(espDominante.getId(),espADominar.getId());

        espDominante = serviceEspiritu.recuperar(espDominante.getId());
        espADominar = serviceEspiritu.recuperar(espADominar.getId());

        assertTrue(espDominante.getEspiritusDominados().contains(espADominar));
        assertEquals(espDominante, espADominar.getDominante());
    }

    @Test
    void unEspirituPuedeDominarAVariosEspiritus(){
        Espiritu otroEspirituADominar = new EspirituAngelical(10, "Otto", 10, ubiADominar);
        Espiritu otroMasEspirituADominar = new EspirituAngelical(10, "Toto", 10, ubiADominar);
        serviceEspiritu.crear(otroEspirituADominar);
        serviceEspiritu.crear(otroMasEspirituADominar);

        serviceEspiritu.dominar(espDominante.getId(),espADominar.getId());
        serviceEspiritu.dominar(espDominante.getId(),otroEspirituADominar.getId());
        serviceEspiritu.dominar(espDominante.getId(),otroMasEspirituADominar.getId());

        espDominante = serviceEspiritu.recuperar(espDominante.getId());
        espADominar = serviceEspiritu.recuperar(espADominar.getId());
        otroEspirituADominar = serviceEspiritu.recuperar(otroEspirituADominar.getId());
        otroMasEspirituADominar = serviceEspiritu.recuperar(otroMasEspirituADominar.getId());

        assertTrue(espDominante.getEspiritusDominados().contains(espADominar));
        assertTrue(espDominante.getEspiritusDominados().contains(otroEspirituADominar));
        assertTrue(espDominante.getEspiritusDominados().contains(otroMasEspirituADominar));
        assertEquals(espDominante, espADominar.getDominante());
        assertEquals(espDominante, otroEspirituADominar.getDominante());
        assertEquals(espDominante, otroMasEspirituADominar.getDominante());

        serviceEspiritu.eliminar(otroEspirituADominar.getId());
        serviceEspiritu.eliminar(otroMasEspirituADominar.getId());
    }

    @Test
    void unEspirituDominadoPuedeDominarAOtrosEspiritus(){
        Poligono otroPoligono = new Poligono(List.of(
                new Point(81.090, 81.090),
                new Point(81.100, 81.100),
                new Point(81.100, 81.101),
                new Point(81.090, 81.101),
                new Point(81.090, 81.090)
        ));

        poligonoService.crear(otroPoligono);

        Ubicacion otraUbi = new Cementerio("Salta", 99);
        serviceUbicacion.crear(otraUbi, otroPoligono);

        Espiritu otroEspirituADominar = new EspirituAngelical(10, "Otto", 10, otraUbi);
        Espiritu otroEspirituMasADominar = new EspirituAngelical(10, "Pablo", 10, otraUbi);
        serviceEspiritu.crear(otroEspirituADominar);
        serviceEspiritu.crear(otroEspirituMasADominar);

        serviceEspiritu.dominar(espDominante.getId(),espADominar.getId());
        serviceEspiritu.dominar(espADominar.getId(),otroEspirituADominar.getId());
        serviceEspiritu.dominar(espADominar.getId(),otroEspirituMasADominar.getId());

        espDominante = serviceEspiritu.recuperar(espDominante.getId());
        espADominar = serviceEspiritu.recuperar(espADominar.getId());
        otroEspirituADominar = serviceEspiritu.recuperar(otroEspirituADominar.getId());
        otroEspirituMasADominar = serviceEspiritu.recuperar(otroEspirituMasADominar.getId());

        assertTrue(espDominante.getEspiritusDominados().contains(espADominar));
        assertTrue(espADominar.getEspiritusDominados().contains(otroEspirituADominar));
        assertTrue(espADominar.getEspiritusDominados().contains(otroEspirituMasADominar));
        assertEquals(espDominante, espADominar.getDominante());
        assertEquals(espADominar, otroEspirituADominar.getDominante());
        assertEquals(espADominar, otroEspirituMasADominar.getDominante());

        serviceEspiritu.eliminar(otroEspirituADominar.getId());
        serviceEspiritu.eliminar(otroEspirituMasADominar.getId());
        serviceUbicacion.eliminar(otraUbi.getId());
        poligonoService.eliminar(otroPoligono.getId());
    }

    @Test
    void dominarEspirituFallaPorEspiritusNoPersistidos(){
        assertThrows(EspirituOMediumNoEncontradoException.class, () -> serviceEspiritu.dominar(123L, espAngel.getId()));
        assertThrows(EspirituOMediumNoEncontradoException.class, () -> serviceEspiritu.dominar(espAngel.getId(), 123L));
    }

    @Test
    void dominarEspirituFallaPorDistanciaMenorADosKm(){
        poligonoService.eliminar(poligonoADominar.getId());

        Poligono poligonoMuyCercano = new Poligono(List.of(
                new Point(81.040, 81.040),
                new Point(81.050, 81.040),
                new Point(81.050, 81.060),
                new Point(81.040, 81.060),
                new Point(81.040, 81.040)
        ));

        poligonoService.crear(poligonoMuyCercano);

        Ubicacion ubiMuyCercana = new Cementerio("Ubicacion muy cercana", 99);

        serviceUbicacion.crear(ubiMuyCercana,poligonoMuyCercano);
        espADominar.setUbicacion(ubiMuyCercana);
        serviceEspiritu.actualizar(espADominar);

        assertThrows(EspiritusFueraDeRangoException.class, () -> serviceEspiritu.dominar(espDominante.getId(), espADominar.getId()));

        poligonoService.eliminar(poligonoMuyCercano.getId());
        serviceUbicacion.eliminar(ubiMuyCercana.getId());
    }

    @Test
    void dominarEspirituFallaPorDistanciaMayorACincoKm(){
        assertThrows(EspiritusFueraDeRangoException.class, () -> serviceEspiritu.dominar(espAngel.getId(), espAngel2.getId()));
    }

    @Test
    void dominarEspirituFallaPorEnergiaMayorA50(){
        espADominar.setEnergia(60);
        serviceEspiritu.actualizar(espADominar);

        assertThrows(EspirituADominarNoCumpleLosRequerimientosException.class, () -> serviceEspiritu.dominar(espDominante.getId(), espADominar.getId()));
    }

    @Test
    void dominarEspirituFallaPorqueElEspirituADominarNoEsLibre(){
        serviceMedium.mover(medium.getId(), 81.063, 81.063);
        serviceEspiritu.conectar(espADominar.getId(), medium.getId());

        assertThrows(EspirituADominarNoCumpleLosRequerimientosException.class, () -> serviceEspiritu.dominar(espDominante.getId(), espADominar.getId()));
    }

    @Test
    void dominarEspirituFallaPorqueElEspirituADominarYaEstaSiendoDominadoPorOtro(){
        Espiritu otroEspDominante = new EspirituDemoniaco(30, "Jorge", 20, ubiDominante);
        serviceEspiritu.crear(otroEspDominante);
        serviceEspiritu.dominar(espDominante.getId(), espADominar.getId());

        assertThrows(EspirituDominanteNoCumpleLosRequerimientosException.class, () -> serviceEspiritu.dominar(otroEspDominante.getId(), espADominar.getId()));
        serviceEspiritu.eliminar(otroEspDominante.getId());
    }

    @AfterEach
    void limpiarBase() {
        serviceMedium.eliminar(medium.getId());

        serviceEspiritu.eliminar(espAngel.getId());
        serviceEspiritu.eliminar(espAngel2.getId());
        serviceEspiritu.eliminar(espDemon.getId());
        serviceEspiritu.eliminar(espDemon2.getId());
        serviceEspiritu.eliminar(espDominante.getId());
        serviceEspiritu.eliminar(espADominar.getId());

        poligonoService.eliminar(poligono1.getId());
        poligonoService.eliminar(poligono2.getId());
        poligonoService.eliminar(poligonoDominante.getId());
        poligonoService.eliminar(poligonoADominar.getId());

        serviceUbicacion.eliminar(ubi1.getId());
        serviceUbicacion.eliminar(ubi2.getId());
        serviceUbicacion.eliminar(ubiDominante.getId());
        serviceUbicacion.eliminar(ubiADominar.getId());
    }
}
