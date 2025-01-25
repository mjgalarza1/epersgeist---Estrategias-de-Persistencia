package ar.edu.unq.epersgeist.exception.notFound;

public class UbicacionNoEncontradaException extends NoEncontradoException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "La ubicacion no se ha encontrado";
    }
}