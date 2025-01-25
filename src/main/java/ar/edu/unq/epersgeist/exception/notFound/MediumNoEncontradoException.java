package ar.edu.unq.epersgeist.exception.notFound;

public class MediumNoEncontradoException extends NoEncontradoException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "El medium no se ha encontrado";
    }
}
