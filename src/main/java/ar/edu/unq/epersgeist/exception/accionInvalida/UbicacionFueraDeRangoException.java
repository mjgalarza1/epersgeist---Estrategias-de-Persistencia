package ar.edu.unq.epersgeist.exception.accionInvalida;

public class UbicacionFueraDeRangoException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "La ubicacion se encuentra a mas de 100km de distancia";
    }
}
