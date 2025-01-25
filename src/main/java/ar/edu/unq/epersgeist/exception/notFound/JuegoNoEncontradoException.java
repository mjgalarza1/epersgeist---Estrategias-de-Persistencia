package ar.edu.unq.epersgeist.exception.notFound;

public class JuegoNoEncontradoException extends NoEncontradoException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "El juego no se ha encontrado";
    }
}
