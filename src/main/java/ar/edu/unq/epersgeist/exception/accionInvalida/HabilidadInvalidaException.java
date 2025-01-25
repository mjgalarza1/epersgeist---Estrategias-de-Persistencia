package ar.edu.unq.epersgeist.exception.accionInvalida;

public class HabilidadInvalidaException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "El nombre de la habilidad no puede estar vacia";
    }
}

