package ar.edu.unq.epersgeist.exception.accionInvalida;

public class PoligonoInvalidoException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    private String error;

    public PoligonoInvalidoException(String error) {this.error = error;}

    @Override
    public String getMessage() {
        return "El poligono dado es invalido: " + error;
    }
}
