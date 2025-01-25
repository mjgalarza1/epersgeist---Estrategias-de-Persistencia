package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.HabilidadInvalidaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HabilidadTest {

    @Test
    void crearInstanciaInvalidaPorNombreVacioONulo() {
        assertThrows(HabilidadInvalidaException.class, () -> new Habilidad(""));
        assertThrows(HabilidadInvalidaException.class, () -> new Habilidad(null));
    }
    @Test
    void crearInstanciaValida() {
        assertDoesNotThrow(() -> new Habilidad("Velocidad"));
    }
}
