package ar.edu.unq.epersgeist.exception.accionInvalida;

public class CoordenadaFueraDeRangoException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    @Override
    public String getMessage() {
        return "Las coordenadas estan fuera del rango permitido";
    }

}
