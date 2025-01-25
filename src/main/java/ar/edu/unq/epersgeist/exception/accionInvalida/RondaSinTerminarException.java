package ar.edu.unq.epersgeist.exception.accionInvalida;

public class RondaSinTerminarException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "La ronda actual está sin terminar";
    }
}
