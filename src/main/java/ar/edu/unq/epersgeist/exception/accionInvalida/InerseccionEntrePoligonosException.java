package ar.edu.unq.epersgeist.exception.accionInvalida;

public class InerseccionEntrePoligonosException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    public InerseccionEntrePoligonosException() {}

    @Override
    public String getMessage() {
        return "El poligono que intentas crear intersecta a un poligono existente. Intentalo de nuevo con otras coordenadas.";
    }

}
