package ar.edu.unq.epersgeist.exception.notFound;

public class EspirituOMediumNoEncontradoException extends NoEncontradoException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "El espiritu o medium no se han encontrado";
    }
}