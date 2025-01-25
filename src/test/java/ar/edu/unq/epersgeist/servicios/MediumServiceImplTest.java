package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.exception.accionInvalida.*;
import ar.edu.unq.epersgeist.exception.notFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.exception.notFound.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.exception.notFound.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.espiritu.*;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest(classes = EpersgeistApplication.class)
public class MediumServiceImplTest {

    @Autowired
    private MediumService serviceMedium;
    @Autowired
    private UbicacionService serviceUbicacion;
    @Autowired
    private EspirituService serviceEspiritu;

    @Autowired
    private HabilidadService habilidadService;
    @Autowired
    private PoligonoService servicePoligono;

    private Poligono poligonoArgentina;
    private Poligono poligonoChile;

    private Ubicacion argentina;
    private Ubicacion chile;

    private Medium unMedium;
    private Medium otroMedium;

    private Espiritu esp1;
    private Espiritu esp2;
    private Espiritu esp3;
    private Espiritu esp4;

    @BeforeEach
    void setUp() {
        // POLIGONOS
        poligonoArgentina = new Poligono(List.of(
                new Point(1.0, 1.0),
                new Point(1.0, 1.2),
                new Point(1.2, 1.2),
                new Point(1.2, 1.0),
                new Point(1.0, 1.0)
        ));

        poligonoChile = new Poligono(List.of(
                new Point(1.3, 1.3),
                new Point(1.3, 1.5),
                new Point(1.5, 1.5),
                new Point(1.5, 1.3),
                new Point(1.3, 1.3)
        ));

        // Crear los polígonos en el servicio
        servicePoligono.crear(poligonoArgentina);
        servicePoligono.crear(poligonoChile);


        // UBICACIONES
        argentina = new Santuario("Argentina",99);
        chile = new Cementerio("Chilee",99);
        serviceUbicacion.crear(argentina, poligonoArgentina);
        serviceUbicacion.crear(chile, poligonoChile);

        // MEDIUMS
        unMedium = new Medium("Pepe", 200, 100, argentina);
        otroMedium = new Medium("Violeta", 500, 500, chile);
        serviceMedium.crear(unMedium);
        serviceMedium.crear(otroMedium);

        // ESPIRITUS
        esp1 = new EspirituAngelical(100, "Casper", 100, argentina);
        esp2 = new EspirituAngelical(100, "Jaster", 100, argentina);
        esp3 = new EspirituDemoniaco(30, "Gaster", 50, chile);
        esp4 = new EspirituDemoniaco(30, "Wasper", 75, chile);
        serviceEspiritu.crear(esp1);
        serviceEspiritu.crear(esp2);
        serviceEspiritu.crear(esp3);
        serviceEspiritu.crear(esp4);
    }

    @Test
    void mediumDescansaEnCementerioYSusDemoniosRecuperanTodaLaEnergia(){
        Poligono poligonoEzpeleta = new Poligono((List.of(
                new Point(-7.0, -3.0),
                new Point(-7.5, -3.5),
                new Point(-8, -3.5),
                new Point(-7.0, -3.0)
        )));

        Ubicacion cementerioDeEzpeleta = new Cementerio("Cementerio de Ezpeleta", 30);
        servicePoligono.crear(poligonoEzpeleta);
        serviceUbicacion.crear(cementerioDeEzpeleta,poligonoEzpeleta);

        Medium ezpeletense = new Medium("Francisco", 500, 40, cementerioDeEzpeleta);
        serviceMedium.crear(ezpeletense);

        Espiritu voldemort = new EspirituDemoniaco(10,"Voldemort",10,cementerioDeEzpeleta);
        serviceEspiritu.crear(voldemort);

        serviceEspiritu.conectar(voldemort.getId(),ezpeletense.getId());
        serviceMedium.descansar(ezpeletense.getId());

        Espiritu espirituDespuesDelBreak = serviceEspiritu.recuperar(voldemort.getId());
        assertEquals(40, espirituDespuesDelBreak.getEnergia());

        servicePoligono.eliminar(poligonoEzpeleta.getId());
        serviceMedium.eliminar(ezpeletense.getId());
        serviceEspiritu.eliminar(voldemort.getId());
        serviceUbicacion.eliminar(cementerioDeEzpeleta.getId());
    }

    @Test
    void mediumDescansaEnCementerioYSuAngelNoRecuperaEnergia(){
        Poligono poligonoEzpeleta = new Poligono((List.of(
                new Point(-58.5, -34.5),  // Punto 1
                new Point(-58.4, -34.4),  // Punto 2
                new Point(-58.3, -34.5),  // Punto 3
                new Point(-58.5, -34.5)   // Cierra el polígono
        )));

        Ubicacion cementerioDeEzpeleta = new Cementerio("Cementerio de Ezpeleta", 30);
        servicePoligono.crear(poligonoEzpeleta);
        serviceUbicacion.crear(cementerioDeEzpeleta,poligonoEzpeleta);

        Medium ezpeletense = new Medium("Francisco", 500, 40, cementerioDeEzpeleta);
        serviceMedium.crear(ezpeletense);

        Espiritu fran = new EspirituAngelical(10,"Fran G.(aprobanos)",10,cementerioDeEzpeleta);
        serviceEspiritu.crear(fran);

        serviceEspiritu.conectar(fran.getId(),ezpeletense.getId());
        serviceMedium.descansar(ezpeletense.getId());

        Espiritu espirituDespuesDelBreak = serviceEspiritu.recuperar(fran.getId());
        assertEquals(10, espirituDespuesDelBreak.getEnergia());

        servicePoligono.eliminar(poligonoEzpeleta.getId());
        serviceMedium.eliminar(ezpeletense.getId());
        serviceEspiritu.eliminar(fran.getId());
        serviceUbicacion.eliminar(cementerioDeEzpeleta.getId());
    }


    @Test
    void mediumDescansaEnSantuarioYSuDemonioNoRecuperaEnergia(){
        Poligono poligonotreintayochoB = new Poligono((List.of(
                new Point(-70.0, -33.0),
                new Point(-70.5, -33.5),
                new Point(-80.5, -32.5),
                new Point(-70.0, -33.0)
        )));

        Ubicacion treintayochoB = new Santuario("38B", 60);
        servicePoligono.crear(poligonotreintayochoB);
        serviceUbicacion.crear(treintayochoB,poligonotreintayochoB);

        Medium ezpeletense = new Medium("Francisco", 500, 40, treintayochoB);
        serviceMedium.crear(ezpeletense);

        Espiritu voldemort = new EspirituDemoniaco(10,"Voldemort",10,treintayochoB);
        serviceEspiritu.crear(voldemort);

        serviceEspiritu.conectar(voldemort.getId(),ezpeletense.getId());
        serviceMedium.descansar(ezpeletense.getId());

        Espiritu espirituDespuesDelBreak = serviceEspiritu.recuperar(voldemort.getId());
        assertEquals(10, espirituDespuesDelBreak.getEnergia());

        serviceMedium.eliminar(ezpeletense.getId());
        serviceEspiritu.eliminar(voldemort.getId());
        serviceUbicacion.eliminar(treintayochoB.getId());
        servicePoligono.eliminar(poligonotreintayochoB.getId());
    }



    @Test
    void mediumDescansaEnCementerioYRecuperaSoloEl50PorCiento(){
        otroMedium.setMana(0);
        serviceMedium.actualizar(otroMedium);
        serviceMedium.descansar(otroMedium.getId());
        Medium mediumDescansado = serviceMedium.obtenerMedium(otroMedium.getId());
        assertEquals(49,mediumDescansado.getMana());
    }


    @Test
    void mediumDescansaEnSantuarioYSuAngelRecuperaTodaLaEnergiaDelLugar(){
        Poligono poligonotreintayochoB = new Poligono((List.of(
                new Point(70.0, 33.0),
                new Point(70.5, 33.5),
                new Point(80.5, 32.5),
                new Point(70.0, 33.0)
        )));

        Ubicacion treintayochoB = new Santuario("38B", 60);
        servicePoligono.crear(poligonotreintayochoB);
        serviceUbicacion.crear(treintayochoB,poligonotreintayochoB);

        Medium ezpeletense = new Medium("Francisco", 500, 40, treintayochoB);
        serviceMedium.crear(ezpeletense);

        Espiritu fran = new EspirituAngelical(10,"Fran G.",10,treintayochoB);
        serviceEspiritu.crear(fran);

        serviceEspiritu.conectar(fran.getId(),ezpeletense.getId());
        serviceMedium.descansar(ezpeletense.getId());

        Espiritu espirituDespuesDelBreak = serviceEspiritu.recuperar(fran.getId());
        assertEquals(70, espirituDespuesDelBreak.getEnergia());

        servicePoligono.eliminar(poligonotreintayochoB.getId());
        serviceMedium.eliminar(ezpeletense.getId());
        serviceEspiritu.eliminar(fran.getId());
        serviceUbicacion.eliminar(treintayochoB.getId());
    }


    @Test
    void mediumSeMueveASantuarioYMuereSuDemonioYSeDesconecta(){
        //el demonio queda en chile con uno de energia para poder conectarse a otroMedium que esta en chile tambien
        esp3.setUbicacion(chile); esp3.setEnergia(1);
        serviceEspiritu.actualizar(esp3);
        serviceEspiritu.conectar(esp3.getId(), otroMedium.getId());

        serviceMedium.mover(otroMedium.getId(), 1.0,1.0);
        otroMedium = serviceMedium.obtenerMedium(otroMedium.getId());

        Espiritu espirituDesconectado = serviceEspiritu.recuperar(esp3.getId());

        assertEquals(espirituDesconectado.getUbicacion(), argentina);
        assertEquals(0, espirituDesconectado.getEnergia());
        assertNull(espirituDesconectado.getMedium());
        assertTrue(otroMedium.getEspiritus().isEmpty());
    }


    @Test
    void mediumSeMueveACementerioYQuedaEn0PeroNoSeDesconecta(){
        //el demonio queda en argentina con uno de energia para poder conectarse a otroMedium que esta en argentina tambien
        esp1.setEnergia(1);
        serviceEspiritu.actualizar(esp1);

        serviceEspiritu.conectar(esp1.getId(), unMedium.getId());

        serviceMedium.mover(unMedium.getId(), 1.3, 1.3);

        unMedium = serviceMedium.obtenerMedium(unMedium.getId());
        Espiritu espirituAfterViaje = serviceEspiritu.recuperar(esp1.getId());

        assertEquals(espirituAfterViaje.getUbicacion(), chile);
        assertEquals(0, espirituAfterViaje.getEnergia());
        assertEquals(unMedium,espirituAfterViaje.getMedium());
    }

    @Test
    void mediumSeMueveACementerioYSusAngelesPierden5DeEnergia(){
        //movemos todos a argentina para hacer el conectar,  esp3 tiene 50 y esp4 tiene 75 de energia
        esp1.setUbicacion(argentina);
        esp2.setUbicacion(argentina);
        serviceEspiritu.actualizar(esp1);
        serviceEspiritu.actualizar(esp2);

        serviceEspiritu.conectar(esp1.getId(), unMedium.getId());
        serviceEspiritu.conectar(esp2.getId(), unMedium.getId());

        serviceMedium.mover(unMedium.getId(), 1.3, 1.3);
        unMedium = serviceMedium.obtenerMedium(unMedium.getId());

        List<Integer> energias = unMedium.getEspiritus().stream().map(Espiritu::getEnergia).toList();

        assertEquals(95, energias.get(1),energias.get(0));
    }

    @Test
    void mediumSeMueveACementerioYSusDemoniosNoPierdenEnergia(){
        //movemos todos a argentina para hacer el conectar,  esp3 tiene 50 y esp4 tiene 75 de energia
        esp3.setUbicacion(argentina);
        esp4.setUbicacion(argentina);
        serviceEspiritu.actualizar(esp3);
        serviceEspiritu.actualizar(esp4);

        serviceEspiritu.conectar(esp3.getId(), unMedium.getId());
        serviceEspiritu.conectar(esp4.getId(), unMedium.getId());

        serviceMedium.mover(unMedium.getId(), 1.3, 1.3);
        unMedium = serviceMedium.obtenerMedium(unMedium.getId());

        List<Integer> energias = unMedium.getEspiritus().stream().map(Espiritu::getEnergia).toList();

        assertEquals(50, energias.get(1));
        assertEquals(75, energias.get(0));
    }

    @Test
    void mediumSeMueveASantuarioYTodosSusDemoniosPierden10DeEnergia(){
        //movemos todos a chile para hacer el conectar,  esp3 tiene 50 y esp4 tiene 75 de energia
        esp3.setUbicacion(chile);
        esp4.setUbicacion(chile);
        serviceEspiritu.actualizar(esp3);
        serviceEspiritu.actualizar(esp4);

        serviceEspiritu.conectar(esp3.getId(), otroMedium.getId());
        serviceEspiritu.conectar(esp4.getId(), otroMedium.getId());

        serviceMedium.mover(otroMedium.getId(), 1.0,1.0);
        otroMedium = serviceMedium.obtenerMedium(otroMedium.getId());

        List<Integer> energias = otroMedium.getEspiritus().stream().map(Espiritu::getEnergia).toList();

        assertEquals(40, energias.get(1));
        assertEquals(65, energias.get(0));
    }

    @Test
    void mediumSeMueveASantuarioYSusAngelesNoPierdenEnergia(){
        //movemos todos a chile para hacer el conectar,  esp1 y esp2 tienen 100 de energia ambos
        esp1.setUbicacion(chile);
        esp2.setUbicacion(chile);
        serviceEspiritu.actualizar(esp1);
        serviceEspiritu.actualizar(esp2);

        serviceEspiritu.conectar(esp1.getId(), otroMedium.getId());
        serviceEspiritu.conectar(esp2.getId(), otroMedium.getId());

        serviceMedium.mover(otroMedium.getId(), 1.0,1.0);
        otroMedium = serviceMedium.obtenerMedium(otroMedium.getId());

        List<Integer> energias = otroMedium.getEspiritus().stream().map(Espiritu::getEnergia).toList();

        assertEquals(100, energias.get(1),energias.get(0));
    }

    @Test
    void mediumSeMueveAUnaUbicacionNoCreada(){
        unMedium = serviceMedium.obtenerMedium(unMedium.getId());
        assertThrows(UbicacionNoEncontradaException.class, () -> serviceMedium.mover(unMedium.getId(), -89.0, 100.0));
    }

    @Test
    void mediumSeMueveAUnaUbicacionInvalida(){
        unMedium = serviceMedium.obtenerMedium(unMedium.getId());
        assertThrows(CoordenadaFueraDeRangoException.class, () -> serviceMedium.mover(unMedium.getId(), -100.0, 200.0));
    }

    @Test
    void mediumsPersistidosCorrectamente() {
        assertNotNull(unMedium);
        assertNotNull(otroMedium);
    }

    @Test
    void crearMediumValido() {
        // Setup
        Medium nuevoMedium = new Medium("Carlos", 300, 150, argentina);

        // Exercise
        serviceMedium.crear(nuevoMedium);

        // Verify
        Medium mediumRecuperado = serviceMedium.obtenerMedium(nuevoMedium.getId());
        assertNotNull(mediumRecuperado);
        assertEquals(nuevoMedium.getNombre(), mediumRecuperado.getNombre());
        assertEquals(nuevoMedium.getMana(), mediumRecuperado.getMana());
        assertEquals(nuevoMedium.getUbicacion().getId(), mediumRecuperado.getUbicacion().getId());
        serviceMedium.eliminar(nuevoMedium.getId());
    }

    @Test
    void recuperarMedium() {
        // Setup
        Long idOriginal = unMedium.getId();
        String nombreOriginal = unMedium.getNombre();

        // Exercise
        Medium unMediumRecuperado = serviceMedium.obtenerMedium(idOriginal);

        // Verify
        assertEquals(unMediumRecuperado.getId(), idOriginal);
        assertEquals(unMediumRecuperado.getNombre(), nombreOriginal);
    }

    @Test
    void actualizarMedium() {
        // Setup
        String nombreOriginal = unMedium.getNombre(); // Pepe
        unMedium.setNombre("Travis");

        // Exercise
        serviceMedium.actualizar(unMedium);

        // Verify
        assertNotEquals(unMedium.getNombre(), nombreOriginal);
    }

    @Test
    void eliminarMedium() {
        // Exercise
        serviceMedium.eliminar(unMedium.getId());
        assertThrows(MediumNoEncontradoException.class, () -> serviceMedium.obtenerMedium(unMedium.getId()));
    }

    @Test
    void recuperarTodosLosMediums() {
        // Exercise
        List<Medium> mediums = serviceMedium.recuperarTodos();

        // Verify

        assertEquals(2, mediums.size());
    }

    @Test
    void espiritus() {
        // Setup
        serviceEspiritu.conectar(esp1.getId(), unMedium.getId());
        serviceEspiritu.conectar(esp2.getId(), unMedium.getId());
        serviceEspiritu.conectar(esp3.getId(), otroMedium.getId());

        // Exercise
        List<Espiritu> espiritusDeUnMedium = serviceMedium.espiritus(unMedium.getId());
        List<Espiritu> espiritusDeOtroMedium = serviceMedium.espiritus(otroMedium.getId());

        // Verify
        assertEquals(espiritusDeUnMedium.size(), 2);
        assertEquals(espiritusDeOtroMedium.size(), 1);
    }

    @Test
    void descansarEnSantuarioSuma148DeEnergiaAMediumYLosEspiritusQuedanEn100() {
        // Setup
        esp2.setearEnergiaValida(10);
        unMedium.setMana(0);
        serviceMedium.actualizar(unMedium);
        serviceEspiritu.actualizar(esp2);
        Integer energiaOriginalEsp1 = esp1.getEnergia(); // 100
        Integer energiaOriginalEsp2 = esp2.getEnergia(); // 100

        serviceEspiritu.conectar(esp1.getId(), unMedium.getId());
        serviceEspiritu.conectar(esp2.getId(), unMedium.getId());

        // Exercise
        serviceMedium.descansar(unMedium.getId());
        Medium mediumDescansado = serviceMedium.obtenerMedium(unMedium.getId());
        List<Espiritu> espiritusDescansados = serviceMedium.espiritus(unMedium.getId());


        // Verify   suma 148 porque el argentina tiene 99 de energia y el medium tenia 0 de mana
        assertEquals(148, mediumDescansado.getMana());
        assertEquals(100, espiritusDescansados.get(0).getEnergia());
        assertEquals(100, espiritusDescansados.get(1).getEnergia());
    }

    @Test
    void descansarEnSantuarioHaceQueRecupereHasta200DeManaYaQueEl150PorcientoDeLaEnergiaExcedeElManaMax(){
        Integer manaOriginalMedium = unMedium.getMana(); // 100
        serviceMedium.descansar(unMedium.getId());
        Medium mediumDescansado = serviceMedium.obtenerMedium(unMedium.getId());
        assertEquals(200, mediumDescansado.getMana());
    }

    @Test
    void descansarDejaElManaDelMediumIgualPorqueYaTieneManaMax() {
        // Setup
        esp2.setearEnergiaValida(10);
        serviceEspiritu.actualizar(esp2);
        unMedium.setMana(unMedium.getManaMax());
        serviceMedium.actualizar(unMedium);

        Integer energiaOriginalEsp1 = esp1.getEnergia(); // 100
        Integer energiaOriginalEsp2 = esp2.getEnergia(); // 10
        Integer manaOriginalMedium = unMedium.getMana(); // 200

        serviceEspiritu.conectar(esp1.getId(), unMedium.getId());
        serviceEspiritu.conectar(esp2.getId(), unMedium.getId());

        // Exercise
        serviceMedium.descansar(unMedium.getId());
        Medium mediumDescansado = serviceMedium.obtenerMedium(unMedium.getId());
        List<Espiritu> espiritusDescansados = serviceMedium.espiritus(unMedium.getId());

        // Verify
        assertEquals(manaOriginalMedium, mediumDescansado.getMana());
        assertEquals(100, espiritusDescansados.get(0).getEnergia());
        assertEquals(100, espiritusDescansados.get(1).getEnergia());
    }


    @Test
    void descansarMediumNoPersistido() {
        Long idInexistente = 121223L;

        assertThrows(MediumNoEncontradoException.class, () -> serviceMedium.descansar(idInexistente));
    }

    @Test
    void recuperarMediumInexistente() {
        Long idInexistente = 123L;

        assertThrows(MediumNoEncontradoException.class, () -> serviceMedium.obtenerMedium(idInexistente));
    }

    @Test
    void eliminarMediumInexistente() {
        Long idInexistente = 123L;
        assertDoesNotThrow(() -> serviceMedium.eliminar(idInexistente));
    }

    @Test
    void espiritusAngelesPierden10DeEnergiaDespuesDeExorcizarYRealizanAtaquesExitosos() {
        RandomizerEspiritual randomizerEspiritual = mock(RandomizerEspiritual.class);
        when(randomizerEspiritual.getRandom()).thenReturn(9);
        esp1.setRandomizer(randomizerEspiritual);
        esp2.setRandomizer(randomizerEspiritual);

        Medium mediumConectado = serviceEspiritu.conectar(esp2.getId(),serviceEspiritu.conectar(esp1.getId(),unMedium.getId()).getId());
        Medium mediumAExorcisar = serviceEspiritu.conectar(esp3.getId(),otroMedium.getId());


        serviceMedium.exorcizar(mediumConectado.getId(), mediumAExorcisar.getId());

        esp1 = serviceEspiritu.recuperar(esp1.getId());
        esp2 = serviceEspiritu.recuperar(esp2.getId());
        esp3 = serviceEspiritu.recuperar(esp3.getId());
        mediumAExorcisar = serviceMedium.obtenerMedium(mediumAExorcisar.getId());

        assertEquals(90, esp1.getEnergia(),esp2.getEnergia());
        assertEquals(0, (int) esp3.getEnergia());
        assertTrue(mediumAExorcisar.getEspiritus().isEmpty());
    }

    @Test
    void espiritusAngelesPierden10DeEnergiaDespuesDeExorcizarPeroSusAtaquesFallanYNoHacenDaño() {
        RandomizerEspiritual randomizerEspiritual = mock(RandomizerEspiritual.class);
        when(randomizerEspiritual.getRandom()).thenReturn(1);
        esp1.setRandomizer(randomizerEspiritual);
        esp2.setRandomizer(randomizerEspiritual);


        Medium mediumConectado = serviceEspiritu.conectar(esp2.getId(),serviceEspiritu.conectar(esp1.getId(),unMedium.getId()).getId());
        Medium mediumAExorcisar = serviceEspiritu.conectar(esp3.getId(),otroMedium.getId());

        serviceMedium.exorcizar(mediumConectado.getId(), mediumAExorcisar.getId());
        esp1 = serviceEspiritu.recuperar(esp1.getId());
        esp2 = serviceEspiritu.recuperar(esp2.getId());

        assertEquals(90, esp1.getEnergia(),esp2.getEnergia());
        assertEquals(50, esp3.getEnergia());
        assertEquals(75, esp4.getEnergia());
    }

    @Test
    void espiritusAngelesNoPierdenEnergiaPorqueNoHayDemoniosParaExorcizar() {
        Medium mediumConectado = serviceEspiritu.conectar(esp2.getId(),serviceEspiritu.conectar(esp1.getId(),unMedium.getId()).getId());

        serviceMedium.exorcizar(mediumConectado.getId(), otroMedium.getId());
        esp1 = serviceEspiritu.recuperar(esp1.getId());
        esp2 = serviceEspiritu.recuperar(esp2.getId());

        assertEquals(100, esp1.getEnergia(),esp2.getEnergia());
    }

    @Test
    void soloUnAngelPierde10DeEnergiaDespuesDeExorcizarPorqueYaNoHayDemoniosYElDemonioEsDesconectadoDeSuMedium() {
        RandomizerEspiritual randomizerEspiritual = mock(RandomizerEspiritual.class);
        when(randomizerEspiritual.getRandom()).thenReturn(9);
        esp1.setRandomizer(randomizerEspiritual);
        esp2.setRandomizer(randomizerEspiritual);

        Medium mediumConectado = serviceEspiritu.conectar(esp2.getId(),serviceEspiritu.conectar(esp1.getId(),unMedium.getId()).getId());
        esp3.setEnergia(2); serviceEspiritu.actualizar(esp3);
        Medium mediumAExorcisar = serviceEspiritu.conectar(esp3.getId(),otroMedium.getId());

        serviceMedium.exorcizar(mediumConectado.getId(), mediumAExorcisar.getId());
        esp1 = serviceEspiritu.recuperar(esp1.getId());
        esp2 = serviceEspiritu.recuperar(esp2.getId());
        mediumAExorcisar = serviceMedium.obtenerMedium(mediumAExorcisar.getId());

        //la energia de los angeles antes de exorcizar es 100, pero al exoricizar y al morirse el demonio,
        // solo a uno se le resta 10 de energia, por ende la suma de ambos daria 190
        assertEquals(190, esp1.getEnergia() + esp2.getEnergia());
        assertTrue(mediumAExorcisar.getEspiritus().isEmpty());
        assertNull(esp3.getMedium());
    }

    @Test
    void alMatarAUnDemonioSeAtacaAlSiguiente() {
        RandomizerEspiritual randomizerEspiritual = mock(RandomizerEspiritual.class);
        when(randomizerEspiritual.getRandom()).thenReturn(9);

        esp1.setRandomizer(randomizerEspiritual);
        esp2.setRandomizer(randomizerEspiritual);
        Medium mediumConectado = serviceEspiritu.conectar(esp2.getId(),serviceEspiritu.conectar(esp1.getId(),unMedium.getId()).getId());

        esp3.setEnergia(2); serviceEspiritu.actualizar(esp3);
        esp4.setEnergia(2); serviceEspiritu.actualizar(esp4);
        Medium mediumAExorcisar =  serviceEspiritu.conectar(esp4.getId(),serviceEspiritu.conectar(esp3.getId(),otroMedium.getId()).getId());

        serviceMedium.exorcizar(mediumConectado.getId(), mediumAExorcisar.getId());
        esp1 = serviceEspiritu.recuperar(esp1.getId());
        esp2 = serviceEspiritu.recuperar(esp2.getId());
        mediumAExorcisar = serviceMedium.obtenerMedium(mediumAExorcisar.getId());

        assertEquals(90, esp1.getEnergia());
        assertEquals(90, esp2.getEnergia());
        assertTrue(mediumAExorcisar.getEspiritus().isEmpty());
        assertNull(esp3.getMedium());
        assertNull(esp4.getMedium());
    }

    @Test
    void ningunAngelPierde10DeEnergiaDespuesDeExorcizar() {
        esp1.setEnergia(1);
        esp2.setEnergia(1);
        serviceEspiritu.actualizar(esp1);
        serviceEspiritu.actualizar(esp2);

        Medium mediumConectado = serviceEspiritu.conectar(esp2.getId(),serviceEspiritu.conectar(esp1.getId(),unMedium.getId()).getId());
        Medium mediumAExorcisar = serviceEspiritu.conectar(esp3.getId(),otroMedium.getId());

        serviceMedium.exorcizar(mediumConectado.getId(), mediumAExorcisar.getId());
        esp1 = serviceEspiritu.recuperar(esp1.getId());
        esp2 = serviceEspiritu.recuperar(esp2.getId());

        assertEquals(1, esp1.getEnergia(),esp2.getEnergia());
    }

    @Test
    void exorcizarInvalidoNoPuedeAtacarPorqueNoTieneAngeles() {
        assertThrows(ExorcistaSinAngelesException.class, () -> serviceMedium.exorcizar(unMedium.getId(), otroMedium.getId()));
    }

    @Test
    void invocarValido() {
        esp1.setUbicacion(chile);
        serviceEspiritu.actualizar(esp1);
        System.out.println("esp1 id: "+ esp1.getUbicacion().getId() + " unMedium id: " + unMedium.getUbicacion().getId());
        serviceMedium.invocar(unMedium.getId(), esp1.getId());

        Espiritu espirituInvocado = serviceEspiritu.recuperar(esp1.getId());
        Medium mediumNew = serviceMedium.obtenerMedium(unMedium.getId());

        assertEquals(espirituInvocado.getUbicacion().getId(), mediumNew.getUbicacion().getId());
        assertEquals(90, mediumNew.getMana()); //unMedium tenia 100 pero ahora tiene 90
    }

    @Test
    void invocarInvalidoPorqueElEspirituNoEsLibre() {
        esp1.setMedium(otroMedium);
        serviceEspiritu.actualizar(esp1);

        assertThrows(EspirituNoDisponibleException.class, () -> serviceMedium.invocar(unMedium.getId(), esp1.getId()));
    }

    @Test
    void invocarInvalidoPorqueElMediumOEspirituNoExisten() {
        Long idInexistente = 123L;

        assertThrows(MediumNoEncontradoException.class, () -> serviceMedium.invocar(idInexistente, esp1.getId()));
        assertThrows(EspirituNoEncontradoException.class, () -> serviceMedium.invocar(unMedium.getId(), idInexistente));
    }

    @Test
    void invocarNoHaceNadaPorFaltaDeMana() {
        Medium mediumSinMana = new Medium("toto", 100, 5, chile);
        Espiritu unEspiritu = new EspirituDemoniaco(100, "pepita" ,80,argentina);
        serviceMedium.crear(mediumSinMana);
        serviceEspiritu.crear(unEspiritu);
        serviceMedium.invocar(mediumSinMana.getId(),esp3.getId());
        Medium mediumRecuperado = serviceMedium.obtenerMedium(mediumSinMana.getId());

        assertEquals(5,mediumSinMana.getMana());
        assertEquals(argentina,unEspiritu.getUbicacion());

        serviceEspiritu.eliminar(unEspiritu.getId());
        serviceMedium.eliminar(mediumSinMana.getId());
    }

    @Test
    void mediumFallaAlInvocarDemonioLibreEnSantuarioYNoPierdeEnergia(){
        Poligono poligonotreintayochoB = new Poligono(List.of(
                new Point(1.3, 1.0),
                new Point(1.3, 1.2),
                new Point(1.7, 1.2),
                new Point(1.7, 1.0),
                new Point(1.3, 1.0)
        ));
        Ubicacion treintayochoB = new Santuario("38B", 60);
        servicePoligono.crear(poligonotreintayochoB);
        serviceUbicacion.crear(treintayochoB,poligonotreintayochoB);

        Medium epersista = new Medium("epersista", 500, 40, argentina);
        serviceMedium.crear(epersista);

        Espiritu voldemort = new EspirituDemoniaco(10,"Voldemort",10,treintayochoB);
        serviceEspiritu.crear(voldemort);
        Medium mediumRecuperado = serviceMedium.obtenerMedium(epersista.getId());
        assertThrows(InvocarEspirituEnUbicacionInvalidaException.class, () -> serviceMedium.invocar(epersista.getId(), voldemort.getId()));
        assertNotEquals(argentina, voldemort.getUbicacion());

        assertEquals(40,mediumRecuperado.getMana());

        servicePoligono.eliminar(poligonotreintayochoB.getId());
        serviceMedium.eliminar(epersista.getId());
        serviceEspiritu.eliminar(voldemort.getId());
        serviceUbicacion.eliminar(treintayochoB.getId());
    }

    @Test
    void mediumFallaAlInvocarAngelLibreEnCementerioYNoPierdeEnergia(){
        Poligono poligonotreintayochoB = new Poligono(List.of(
                new Point(1.51, 1.3),
                new Point(1.51, 1.5),
                new Point(1.5, 1.6),
                new Point(1.51, 1.51),
                new Point(1.51, 1.3)
        ));

        Ubicacion treintayochoB = new Santuario("38B", 60);
        servicePoligono.crear(poligonotreintayochoB);
        serviceUbicacion.crear(treintayochoB,poligonotreintayochoB);

        Medium epersista = new Medium("epersista", 500, 40, chile);
        serviceMedium.crear(epersista);

        Espiritu pepe = new EspirituAngelical(10,"Pepe",10,treintayochoB);
        serviceEspiritu.crear(pepe);
        Medium mediumRecuperado = serviceMedium.obtenerMedium(epersista.getId());
        assertThrows(InvocarEspirituEnUbicacionInvalidaException.class, () -> serviceMedium.invocar(epersista.getId(), pepe.getId()));
        assertNotEquals(chile, pepe.getUbicacion());

        assertEquals(40,mediumRecuperado.getMana());

        servicePoligono.eliminar(poligonotreintayochoB.getId());
        serviceMedium.eliminar(epersista.getId());
        serviceEspiritu.eliminar(pepe.getId());
        serviceUbicacion.eliminar(treintayochoB.getId());
    }

    @Test
    void mediumFallaAlInvocarAngelPorDistanciaAMasDe100Km(){
        Poligono poligonoMuyLejano = new Poligono(List.of(
                new Point(81.3, 81.3),
                new Point(81.3, 81.5),
                new Point(81.5, 81.5),
                new Point(81.5, 81.3),
                new Point(81.3, 81.3)
        ));

        Ubicacion ubicacionMuyLejana = new Santuario("38B", 60);
        servicePoligono.crear(poligonoMuyLejano);
        serviceUbicacion.crear(ubicacionMuyLejana, poligonoMuyLejano);

        Medium epersista = new Medium("epersista", 500, 40, chile);
        serviceMedium.crear(epersista);

        Espiritu pepe = new EspirituAngelical(10,"Pepe",10, ubicacionMuyLejana);
        serviceEspiritu.crear(pepe);

        assertThrows(EspirituMuyLejanoException.class, () -> serviceMedium.invocar(epersista.getId(), pepe.getId()));

        servicePoligono.eliminar(poligonoMuyLejano.getId());
        serviceMedium.eliminar(epersista.getId());
        serviceEspiritu.eliminar(pepe.getId());
        serviceUbicacion.eliminar(ubicacionMuyLejana.getId());
    }

    @Test
    void despuesDeExorcizarLosEspiritusQueSobrevivenMutan() {
        Habilidad absorcion = new Habilidad("Absorcion");

        habilidadService.crear(absorcion);

        RandomizerEspiritual randomizerEspiritual = mock(RandomizerEspiritual.class);
        when(randomizerEspiritual.getRandom()).thenReturn(9);
        esp1.setRandomizer(randomizerEspiritual);

        Medium mediumConectado = serviceEspiritu.conectar(esp1.getId(), unMedium.getId());

        esp3.setEnergia(20); serviceEspiritu.actualizar(esp3);
        esp4.setEnergia(100); serviceEspiritu.actualizar(esp4);
        serviceEspiritu.conectar(esp3.getId(),otroMedium.getId());
        Medium mediumAExorcisar = serviceEspiritu.conectar(esp4.getId(),otroMedium.getId());

        serviceMedium.exorcizar(mediumConectado.getId(), mediumAExorcisar.getId());

        esp3 = serviceEspiritu.recuperar(esp3.getId());
        esp4 = serviceEspiritu.recuperar(esp4.getId());

        assertTrue(esp4.getHabilidades().contains(absorcion.getNombre()));
        assertTrue(esp3.getHabilidades().contains(absorcion.getNombre()));
        assertEquals(1, esp3.getHabilidades().size());
        assertEquals(1, esp4.getHabilidades().size());
    }

    @AfterEach
    void teardown() {
        serviceMedium.eliminar(unMedium.getId());
        serviceMedium.eliminar(otroMedium.getId());

        serviceEspiritu.eliminar(esp1.getId());
        serviceEspiritu.eliminar(esp2.getId());
        serviceEspiritu.eliminar(esp3.getId());
        serviceEspiritu.eliminar(esp4.getId());

        servicePoligono.eliminar(poligonoArgentina.getId());
        servicePoligono.eliminar(poligonoChile.getId());

        serviceUbicacion.eliminar(argentina.getId());
        serviceUbicacion.eliminar(chile.getId());

        habilidadService.clearAll();
    }
}

