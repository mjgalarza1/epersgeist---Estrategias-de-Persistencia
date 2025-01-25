package ar.edu.unq.epersgeist.exception.notFound;

public class PoligonoNoEncontradoException extends NoEncontradoException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "El poligono no se ha encontrado";
    }
}