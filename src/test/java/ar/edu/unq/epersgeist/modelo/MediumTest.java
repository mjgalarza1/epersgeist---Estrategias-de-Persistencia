package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.EspirituNoDisponibleException;
import ar.edu.unq.epersgeist.exception.accionInvalida.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.exception.accionInvalida.ManaInvalidaException;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MediumTest {

    private Ubicacion unaUbicacion;
    private Medium unMedium;
    private Espiritu esp1;
    private Espiritu esp2;

    @BeforeEach
    void setUp(){
        // DOC
        unaUbicacion = mock(Ubicacion.class);
        esp1 = mock(Espiritu.class);
        esp2 = mock(Espiritu.class);

        // SUT
        unMedium = new Medium("Pepe", 200, 100, unaUbicacion);
    }

    @Test
    void moverMediumASantuarioYSuDemonioPierde10DeEnergia(){
        Ubicacion santuario = new Santuario("Santuario", 90);
        Espiritu demonio = new EspirituDemoniaco(90, "Demon", 90, unaUbicacion);

        demonio.conectarseAMedium(unMedium);
        unMedium.mover(santuario);

        assertEquals(80, demonio.getEnergia());
    }

    @Test
    void crearMediumConManaInvalido() {
        // Verify
        assertThrows(ManaInvalidaException.class,() -> {
            new Medium("Medium con Mana Invalido", 100, 120, unaUbicacion);
        });
        assertThrows(ManaInvalidaException.class,() -> {
            new Medium("Medium con Mana Invalido", -5, 120, unaUbicacion);
        });
    }

    @Test
    public void crearMediumConParametrosNull() {
        assertThrows(RuntimeException.class, () -> {
            new Medium(null, 1, 120, unaUbicacion);
        });
        assertThrows(RuntimeException.class, () -> {
            new Medium("A", 1, null, unaUbicacion);
        });
        assertThrows(RuntimeException.class, () -> {
            new Medium("A", 1, 120, null);
        });
        assertThrows(RuntimeException.class, () -> {
            new Medium("A", null, 120, unaUbicacion);
        });
    }

    @Test
    void crearMediumConManaSuperiorAMaximo() {
        // Verify
        assertThrows(ManaInvalidaException.class, () -> {
            new Medium("Medium", 100, 110, unaUbicacion);
        });
    }

    @Test
    void conectarseAUnEspirituAgregaLosEspiritusAlMedium() {
        // Exercise
        unMedium.conectarseAEspiritu(esp1);
        unMedium.conectarseAEspiritu(esp2);

        // Verify
        assertEquals(2, unMedium.getEspiritus().size());
        assertTrue(unMedium.getEspiritus().contains(esp1));
        assertTrue(unMedium.getEspiritus().contains(esp2));
    }

    @Test
    void conectarseAUnEspirituLoDejaConectadoConSuMedium() {
        // Exercise
        unMedium.conectarseAEspiritu(esp1);

        // Verify
        verify(esp1).setMedium(unMedium);
    }

    @Test
    void aumentarManaValido() {
        // Setup
        int valorOriginal = unMedium.getMana();

        // Exercise
        unMedium.aumentarMana(10);

        // Verify
        assertEquals(100, valorOriginal);
        assertEquals(110, unMedium.getMana());
        assertNotEquals(valorOriginal, unMedium.getMana());
    }

    @Test
    void aumentarManaLanzaExcepcionConNumeroNegativo() {
        // Verify
        assertThrows(ManaInvalidaException.class,() -> {
            unMedium.aumentarMana(-10);
        });
    }

    @Test
    void desconectarEspiritu() {
        // Setup
        unMedium.conectarseAEspiritu(esp1);

        // Exercise
        unMedium.desconectarEspiritu(esp1);

        // Verify
        assertFalse(unMedium.getEspiritus().contains(esp1));
    }

    @Test
    void exorcizarSinAngelesLanzaExcepcion() {
        // Verify
        assertThrows(ExorcistaSinAngelesException.class, () -> {
            unMedium.exorcizar(unMedium);
        });
    }

    @Test
    void invocarEspirituNoLibreLanzaExcepcion() {
        // Setup
        Medium otroMedium = new Medium("Roberto", 200, 100, unaUbicacion);

        when(esp1.esLibre()).thenReturn(false);
        when(esp1.getMedium()).thenReturn(otroMedium);

        // Verify
        assertThrows(EspirituNoDisponibleException.class, () -> {
            unMedium.invocar(esp1);
        });
    }

    @Test
    void invocarEspirituLibreValido() {
        // Setup
        when(esp1.esLibre()).thenReturn(true);

        // Exercise
        unMedium.invocar(esp1);

        // Verify
        verify(esp1).serInvocadoEn(unMedium.getUbicacion());
        assertEquals(90, unMedium.getMana());
    }

    @Test
    void invocarEspirituNoOcurrePorqueNoHayManaSuficiente() {
        // Setup
        Medium mediumConManaInsuficiente = new Medium("Coral", 200, 2, unaUbicacion);
        when(esp1.esLibre()).thenReturn(true);

        // Exercise
        mediumConManaInsuficiente.invocar(esp1);

        // Verify
        verify(esp1).esLibre();
        verifyNoMoreInteractions(esp1);
        assertEquals(2, mediumConManaInsuficiente.getMana());
    }

    @Test
    void moverCambiaLaUbicacionDelMediumYMueveAlEspiritu() {
        // Setup
        Ubicacion otraUbicacion = mock(Ubicacion.class);

        // Exercise
        unMedium.conectarseAEspiritu(esp1);
        unMedium.mover(otraUbicacion);

        // Verify
        verify(esp1).mover(otraUbicacion);
        assertEquals(otraUbicacion, unMedium.getUbicacion());
    }

    @Test
    void descansarRecuperaManaAlMediumYEnergiaAlEspiritu() {
        // Setup
        when(unaUbicacion.getEnergiaAAumentar()).thenReturn(50);
        // Exercise
        unMedium.conectarseAEspiritu(esp1);
        unMedium.descansar();

        // Verify
        verify(esp1).recuperarEnergiaEn(unaUbicacion);
        assertEquals(150, unMedium.getMana());
    }

    @Test
    void descansarRecuperaManaHastaElManaMax() {
        // Setup
        when(unaUbicacion.getEnergiaAAumentar()).thenReturn(120);
        // Exercise
        unMedium.conectarseAEspiritu(esp1);
        unMedium.descansar();

        // Verify
        verify(esp1).recuperarEnergiaEn(unaUbicacion);
        assertEquals(200, unMedium.getMana());
    }

    @Test
    void descansarNoRecuperaManaPorqueYaLlegoAlManaMax() {
        // Setup
        Medium otroMedium = new Medium("Coral", 200, 200, unaUbicacion);
        when(unaUbicacion.getEnergiaAAumentar()).thenReturn(120);
        // Exercise
        otroMedium.conectarseAEspiritu(esp1);
        otroMedium.descansar();

        // Verify
        verify(esp1).recuperarEnergiaEn(unaUbicacion);
        assertEquals(200, otroMedium.getMana());
    }
}
