package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.CantidadNegativaNulaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CondicionTest {
    private Condicion condicion;

    @Test
    void crearInstanciaValida(){
        assertDoesNotThrow(() -> new Condicion(10, Evaluacion.ENERGIA));
    }
    @Test
    void crearInstanciaInvalidaPorCantidadNegativaONula(){
        assertThrows(CantidadNegativaNulaException.class , () -> new Condicion(-10, Evaluacion.ENERGIA));
        assertThrows(CantidadNegativaNulaException.class , () -> new Condicion(null, Evaluacion.ENERGIA));
    }
}
