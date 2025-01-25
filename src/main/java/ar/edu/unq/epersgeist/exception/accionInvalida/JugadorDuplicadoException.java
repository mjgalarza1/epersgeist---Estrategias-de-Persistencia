package ar.edu.unq.epersgeist.exception.accionInvalida;

public class JugadorDuplicadoException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    private String nombre;

    public JugadorDuplicadoException(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getMessage() {
        return "El jugador " + this.nombre + " ya existe";
    }
}
