package ar.edu.unq.epersgeist.exception.accionInvalida;

public class ConectarException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "El espiritu no pudo establecer conexion con el medium porque no es libre o no comparten ubicacion";
    }
}
