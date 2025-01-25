package ar.edu.unq.epersgeist.exception.accionInvalida;

public class CantidadNegativaNulaException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "La cantidad de una condicion no puede ser Negativa o Null";
    }
}
