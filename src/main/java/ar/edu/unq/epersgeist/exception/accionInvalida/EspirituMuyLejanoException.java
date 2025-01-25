package ar.edu.unq.epersgeist.exception.accionInvalida;

public class EspirituMuyLejanoException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "El espiritu esta a mas de 100KM de distancia";
    }
}