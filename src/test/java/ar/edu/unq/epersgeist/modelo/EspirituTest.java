package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.ConectarException;
import ar.edu.unq.epersgeist.exception.accionInvalida.InvocarEspirituEnUbicacionInvalidaException;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.RandomizerEspiritual;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.exception.accionInvalida.EnergiaInvalidaException;
import ar.edu.unq.epersgeist.exception.accionInvalida.NivelConexionInvalidaException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EspirituTest {

    private EspirituAngelical espAngelical;
    private EspirituDemoniaco espDemoniaco;
    private Ubicacion ubi;
    private RandomizerEspiritual random;
    private Medium medium;

    @BeforeEach
    public void setUp() {
        random = mock(RandomizerEspiritual.class);
        ubi = new Cementerio("EEUU", 12);
        espAngelical = new EspirituAngelical(65, "Bueno", 13, ubi);
        espDemoniaco = new EspirituDemoniaco(20, "Malo", 21, ubi);
        medium = new Medium("Maguito", 500, 200, ubi);
    }

    @Test
    public void puedeParticiparEnExorcismo() {
        assertTrue(espAngelical.puedeParticiparEnExorcismo());
    }

    @Test
    public void noPuedeParticiparEnExorcismo() {
        assertFalse(espDemoniaco.puedeParticiparEnExorcismo());
    }

    @Test
    public void setearEnergiaValida() {
        espAngelical.setearEnergiaValida(12);
        assertEquals(12, espAngelical.getEnergia());
    }

    @Test
    public void bajarEnergiaEnDemonioLoDesconectaDeSuMedium(){
        espDemoniaco.conectarseAMedium(medium);
        espDemoniaco.bajarEnergia(40);

        assertEquals(0, medium.getEspiritus().size());
    }

    @Test
    public void bajarEnergiaEnAngelConPocaEnergiaLaLimitaA0(){
        espAngelical.bajarEnergia(40);

        assertEquals(0, espAngelical.getEnergia());
    }

    @Test
    public void crearEspirituConEnergiaInvalida() {
        assertThrows(EnergiaInvalidaException.class, () -> {
            new EspirituAngelical(65, "Bueno", 120, ubi);
        });
    }

    @Test
    public void crearEspirituConParametrosNull() {
        assertThrows(RuntimeException.class, () -> {
            new EspirituAngelical(null, "Bueno", 120, ubi);
        });
        assertThrows(RuntimeException.class, () -> {
            new EspirituAngelical(65, "Bueno", null, ubi);
        });
        assertThrows(RuntimeException.class, () -> {
            new EspirituAngelical(65, "Bueno", 120, null);
        });
        assertThrows(RuntimeException.class, () -> {
            new EspirituAngelical(65, null, 120, ubi);
        });
    }


    @Test
    public void crearEspirituConNivelDeConexionInvalido() {
        assertThrows(NivelConexionInvalidaException.class, () -> {
            new EspirituAngelical(120, "Bueno", 12, ubi);
        });
    }

    @Test
    public void setearEnergiaException() {
        assertThrows(EnergiaInvalidaException.class, () -> {
            espAngelical.setearEnergiaValida(120);
        });
    }

    @Test
    public void setearNivelDeConexionValida() {
        espAngelical.setearNivelConexionValida(12);
        assertEquals(12, espAngelical.getNivelDeConexion());
    }

    @Test
    public void setearNivelDeConexionException() {
        assertThrows(NivelConexionInvalidaException.class, () -> {
            espAngelical.setearNivelConexionValida(120);
        });
    }

    @Test
    public void ataqueConPorcentajeExitoso() {
        when(random.getRandom()).thenReturn(10);
        espAngelical.setRandomizerEspiritual(random);

        assertTrue(espAngelical.tienePorcentajeExitoso());
    }

    @Test
    public void ataqueConPorcentajeNoExitoso() {
        when(random.getRandom()).thenReturn(1);
        espAngelical.setRandomizerEspiritual(random);

        assertFalse(espAngelical.tienePorcentajeExitoso());
    }

    @Test
    public void espirituDemoniacoAtacaExitosamenteYEspirituAngelicalPierde10DeEnergia() {
        when(random.getRandom()).thenReturn(10);
        espAngelical.setRandomizerEspiritual(random);

        espAngelical.atacar(espDemoniaco);

        assertEquals(21 - 32, espDemoniaco.getEnergia());
        assertEquals(3, espAngelical.getEnergia());
    }


    @Test
    public void espirituDemoniacoNoAtacaExitosamenteYEspirituAngelicalPierde10DeEnergia() {
        when(random.getRandom()).thenReturn(1);
        espAngelical.setRandomizerEspiritual(random);

        espAngelical.atacar(espDemoniaco);

        assertEquals(21, espDemoniaco.getEnergia());
        assertEquals(3, espAngelical.getEnergia());
    }

    @Test
    public void noAtacaPorqueNoTieneEnergiaInsuficiente() {
        espAngelical.setearEnergiaValida(9);

        when(random.getRandom()).thenReturn(1);
        espAngelical.setRandomizerEspiritual(random);

        espAngelical.atacar(espDemoniaco);

        assertEquals(21, espDemoniaco.getEnergia());
        assertEquals(9, espAngelical.getEnergia());
    }

    @Test
    public void ataqueConEnergiaExacta() {
        espAngelical.setEnergia(10);
        assertTrue(espAngelical.puedeAtacar());

        espAngelical.atacar(espDemoniaco);
        assertEquals(0, espAngelical.getEnergia());
    }

    @Test
    public void ataqueFallidoPorFaltaDeEnergia() {
        espAngelical.setEnergia(10);
        espAngelical.atacar(espDemoniaco);
        assertEquals(0, espAngelical.getEnergia());

        // Intentar atacar de nuevo sin energía suficiente
        assertFalse(espAngelical.puedeAtacar());
    }

    @Test
    public void espirituTieneMediumYElMediumTieneAlEspirituEnSuListaDespuesDelConectar(){
        espAngelical.setearNivelConexionValida(10);
        Integer nivelDeConexionNuevo = espAngelical.getNivelDeConexion() + (int) (medium.getMana() * 0.20);

        espAngelical.conectarseAMedium(medium);

        assertEquals(espAngelical.getMedium(), medium);
        assertEquals(1, medium.getEspiritus().size());
    }

    @Test
    public void intentaAumentarLaConexionHasta1065PeroQuedaEn100(){
        espAngelical.aumentarConexionHasta100(1000);
        assertEquals(100, espAngelical.getNivelDeConexion());
    }

    @Test
    public void aumentarLaConexionHasta80(){
        espAngelical.aumentarConexionHasta100(15);
        assertEquals(80, espAngelical.getNivelDeConexion());
    }

    @Test
    public void espirituTieneMediumYSuNivelDeConexionQuedaEn100YElMediumTieneAlEspirituEnSuListaDespuesDelConectar(){
        espAngelical.conectarseAMedium(medium);

        assertEquals(espAngelical.getMedium(), medium);
        assertEquals(1, medium.getEspiritus().size());
    }

    @Test
    public void conectarAMediumEnOtraUbicacion(){
            Ubicacion ubiDistinta = new Cementerio("Uruguay", 99);
            espAngelical.setUbicacion(ubiDistinta);

            assertThrows(ConectarException.class,() -> espAngelical.conectarseAMedium(medium));
    }

    @Test
    public void conectarEspirituNoLibreAMedium(){
        espAngelical.conectarseAMedium(medium);

        assertThrows(ConectarException.class,() -> espAngelical.conectarseAMedium(medium));
    }

    @Test
    public void demonioEsAtacadoDeFormaExitosaYSuEnergiaEsMenorA0(){
        RandomizerEspiritual randomizerEspiritual = mock(RandomizerEspiritual.class);
        when(randomizerEspiritual.getRandom()).thenReturn(9);
        espAngelical.setRandomizerEspiritual(randomizerEspiritual);

        espDemoniaco.setearEnergiaValida(1);
        espAngelical.atacar(espDemoniaco);
        assertTrue(espDemoniaco.getEnergia()<0);
    }

    @Test
    public void demonioMuereAlSerAtacadoDeFormaExitosaYSeDesconectaDeSuMedium(){
        RandomizerEspiritual randomizerEspiritual = mock(RandomizerEspiritual.class);
        when(randomizerEspiritual.getRandom()).thenReturn(9);
        espAngelical.setRandomizerEspiritual(randomizerEspiritual);

        espDemoniaco.conectarseAMedium(medium);
        espDemoniaco.setEnergia(1);
        espAngelical.atacar(espDemoniaco);

        assertEquals(0 , medium.getEspiritus().size());
        assertNull(espDemoniaco.getMedium());
    }

    @Test
    public void alDemonioNoLePasaNadaPorqueElAtaqueNoFueExitoso(){
        RandomizerEspiritual randomizerEspiritual = mock(RandomizerEspiritual.class);
        when(randomizerEspiritual.getRandom()).thenReturn(1);
        espAngelical.setRandomizerEspiritual(randomizerEspiritual);
        Espiritu espAntesDeSerAtacado = espDemoniaco;

        espDemoniaco.setEnergia(1);
        espAngelical.atacar(espDemoniaco);

        assertEquals(espAntesDeSerAtacado,espDemoniaco);

    }

    @Test
    public void invocarADemonioEnCementerioCambiaSuUbicacion(){
        Ubicacion cementerio = new Cementerio("Recoleta", 50);
        espDemoniaco.serInvocadoEn(cementerio);

        assertEquals(cementerio, espDemoniaco.getUbicacion());
    }

    @Test
    public void invocarAAngelEnSantuarioCambiaSuUbicacion(){
        Ubicacion santuario = new Santuario("Lujan", 50);
        espAngelical.serInvocadoEn(santuario);

        assertEquals(santuario, espAngelical.getUbicacion());
    }

    @Test
    public void invocarADemonioEnSantuarioFalla(){
        Ubicacion santuario = new Santuario("Lujan", 50);

        assertThrows(InvocarEspirituEnUbicacionInvalidaException.class, () -> espDemoniaco.serInvocadoEn(santuario));
    }

    @Test
    public void invocarAAngelEnCementerioFalla(){
        Ubicacion cementerio = new Cementerio("Recoleta", 50);

        assertThrows(InvocarEspirituEnUbicacionInvalidaException.class, () -> espAngelical.serInvocadoEn(cementerio));
    }

    @Test
    public void moverDemonioACementerioCambiaSuUbicacion(){
        Ubicacion cementerio = new Cementerio("Recoleta", 50);
        espDemoniaco.mover(cementerio);

        assertEquals(cementerio, espDemoniaco.getUbicacion());
    }

    @Test
    public void moverAngelASantuarioCambiaSuUbicacion(){
        Ubicacion santuario = new Santuario("Lujan", 50);
        espAngelical.serInvocadoEn(santuario);

        assertEquals(santuario, espAngelical.getUbicacion());
    }

    @Test
    public void moverDemonioASantuarioCambiaSuUbicacionYLeBaja10DeEnergia(){
        Ubicacion santuario = new Santuario("Lujan", 50);
        espDemoniaco.mover(santuario);

        assertEquals(santuario, espDemoniaco.getUbicacion());
        assertEquals(11, espDemoniaco.getEnergia());
    }

    @Test
    public void moverDemonioASantuarioCambiaSuUbicacionYLeBaja5DeEnergia(){
        Ubicacion cementerio = new Cementerio("Recoleta", 50);
        espAngelical.mover(cementerio);

        assertEquals(cementerio, espAngelical.getUbicacion());
        assertEquals(8, espAngelical.getEnergia());
    }

    @Test
    public void demonioRecuperaLaEnergiaTotalDelCementerio(){
        espDemoniaco.recuperarEnergiaEn(ubi);
        assertEquals(33, espDemoniaco.getEnergia());
    }

    @Test
    public void demonioNoRecuperaEnergiaEnSantuario(){
        Ubicacion santuario = new Santuario("Lujan", 50);
        espDemoniaco.recuperarEnergiaEn(santuario);
        assertEquals(21, espDemoniaco.getEnergia());
    }

    @Test
    public void angelRecuperaLaEnergiaTotalDelSantuario(){
        Ubicacion santuario = new Santuario("Lujan", 50);
        espAngelical.recuperarEnergiaEn(santuario);
        assertEquals(63, espAngelical.getEnergia());
    }

    @Test
    public void angelNoRecuperaEnergiaEnCementerio(){
        espAngelical.recuperarEnergiaEn(ubi);
        assertEquals(13, espAngelical.getEnergia());
    }


}
