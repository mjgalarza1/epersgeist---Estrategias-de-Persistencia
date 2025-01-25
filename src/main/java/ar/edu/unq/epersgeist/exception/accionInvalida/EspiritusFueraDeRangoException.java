package ar.edu.unq.epersgeist.exception.accionInvalida;

public class EspiritusFueraDeRangoException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "Los espiritus no se encuentran entre 2 y 5 kilometros de distancia";
    }
}