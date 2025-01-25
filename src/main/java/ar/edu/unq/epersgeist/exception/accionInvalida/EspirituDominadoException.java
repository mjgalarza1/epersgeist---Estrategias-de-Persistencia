package ar.edu.unq.epersgeist.exception.accionInvalida;

public class EspirituDominadoException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "El espiritu esta siendo dominado";
    }
}