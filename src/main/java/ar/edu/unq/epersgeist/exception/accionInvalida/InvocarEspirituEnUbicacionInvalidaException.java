package ar.edu.unq.epersgeist.exception.accionInvalida;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public class InvocarEspirituEnUbicacionInvalidaException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    private Ubicacion ubi;
    private Espiritu espiritu;
    public InvocarEspirituEnUbicacionInvalidaException(Ubicacion ubicacion, Espiritu espiritu) {
        this.ubi = ubicacion;
        this.espiritu = espiritu;
    }

    @Override
    public String getMessage() {
        return "No se puede invocar al " + espiritu.getClass().getSimpleName() + " en el " + ubi.getClass().getSimpleName() + ".";
    }


}
