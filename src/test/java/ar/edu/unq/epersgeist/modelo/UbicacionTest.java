package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.EnergiaInvalidaException;
import ar.edu.unq.epersgeist.exception.accionInvalida.InvocarEspirituEnUbicacionInvalidaException;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.geo.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UbicacionTest {

    Ubicacion santuario;
    Ubicacion cementerio;
    Ubicacion otraUbi;
    Poligono coordenadas;
    Espiritu demonio;
    Espiritu angel;

    @BeforeEach
    void setUp(){
        coordenadas =
                new Poligono(List.of(
                new Point(0, 0),
                new Point(3, 6),
                new Point(6, 1),
                new Point(0, 0)
        ));

        santuario = new Santuario("Iglesia", 100);
        cementerio = new Cementerio("Tumba", 100);
        otraUbi = new Cementerio("Olor", 100);
        demonio = new EspirituDemoniaco(40, "miedo", 50, otraUbi);
        angel = new EspirituAngelical(40, "bueno", 50, otraUbi);
    }

    @Test
    void crearInstanciaValida() {
        assertNotNull(new Cementerio("Uruguay",99));
        assertNotNull(new Cementerio("Uruguay",0));
    }

    @Test
    void crearInstanciaInvalida() {
        assertThrows(RuntimeException.class, () -> new Cementerio(null,99));
        assertThrows(RuntimeException.class, () -> new Cementerio("Chile", null));
        assertThrows(EnergiaInvalidaException.class, () -> new Cementerio("Chile", -20));
        assertThrows(EnergiaInvalidaException.class, () -> new Cementerio("Chile", 200));
    }

    @Test
    void invocarEspirituEnElLugarCorrecto() {
        cementerio.invocarDemonio(demonio);
        assertEquals(cementerio, demonio.getUbicacion());

        santuario.invocarAngel(angel);
        assertEquals(santuario, angel.getUbicacion());
    }

    @Test
    void invocarEspirituEnElLugarIncorrecto() {
        assertThrows(InvocarEspirituEnUbicacionInvalidaException.class, () -> cementerio.invocarAngel(angel));
        assertThrows(InvocarEspirituEnUbicacionInvalidaException.class, () -> santuario.invocarDemonio(demonio));
    }

    @Test
    void recuperarEnergiaAngelEnSantuario() {
        santuario.recuperarEnergiaAAngel(angel);
        assertEquals(100, angel.getEnergia());
    }

    @Test
    void recuperarEnergiaDemonioEnCementerio() {
        cementerio.recuperarEnergiaADemonio(demonio);
        assertEquals(100, demonio.getEnergia());
    }

    @Test
    void angelNoRecuperaEnergiaEnCementerio() {
        cementerio.recuperarEnergiaAAngel(angel);
        assertEquals(50, angel.getEnergia());
    }

    @Test
    void demonioNoRecuperaEnergiaEnSantuario() {
        santuario.recuperarEnergiaADemonio(demonio);
        assertEquals(50, demonio.getEnergia());
    }

    @Test
    void moverAngelASantuarioCambiaSuUbicacion() {
        santuario.moverAngel(angel);
        assertEquals(santuario, angel.getUbicacion());
    }

    @Test
    void moverDemonioACementerioCambiaSuUbicacion() {
        santuario.moverAngel(angel);
        assertEquals(santuario, angel.getUbicacion());
    }

    @Test
    void moverDemonioASantuarioCambiaSuUbicacionYLeSaca10DeEnergia() {
        santuario.moverDemonio(demonio);
        assertEquals(santuario, demonio.getUbicacion());
        assertEquals(40, demonio.getEnergia());
    }

    @Test
    void moverAngelACementerioCambiaSuUbicacionYLeSaca5DeEnergia() {
        cementerio.moverAngel(angel);
        assertEquals(cementerio, angel.getUbicacion());
        assertEquals(45, angel.getEnergia());
    }

    @Test
    void moverDemonioASantuarioCambiaSuUbicacionYLeSaca10DeEnergiaYMuere() {
        demonio = new EspirituDemoniaco(40, "miedo", 8, otraUbi);
        santuario.moverDemonio(demonio);
        assertEquals(santuario, demonio.getUbicacion());
        assertEquals(-2, demonio.getEnergia());
    }

    @Test
    void moverAngelACementerioCambiaSuUbicacionYLeSaca5DeEnergiaYQuedaEn0() {
        angel = new EspirituAngelical(40, "bueno", 4, otraUbi);
        cementerio.moverAngel(angel);
        assertEquals(cementerio, angel.getUbicacion());
        assertEquals(0, angel.getEnergia());
    }

    @Test
    void getEnergiaAAumentarEnSantuarioDevuelveEl150PorcientoDeSuEnergia(){
        assertEquals(150, santuario.getEnergiaAAumentar());
    }

    @Test
    void getEnergiaAAumentarEnCementerioDevuelveEl50PorcientoDeSuEnergia(){
        assertEquals(50, cementerio.getEnergiaAAumentar());
    }


}
