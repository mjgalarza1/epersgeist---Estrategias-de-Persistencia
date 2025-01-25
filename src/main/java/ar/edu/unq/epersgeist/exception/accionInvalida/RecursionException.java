package ar.edu.unq.epersgeist.exception.accionInvalida;

public class RecursionException  extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    private String jugadorOriginal;
    private String jugadorSiguiente;

    public RecursionException(String jugadorOriginal, String jugadorSiguiente) {
        this.jugadorOriginal = jugadorOriginal;
        this.jugadorSiguiente = jugadorSiguiente;
    }

    @Override
    public String getMessage() {
        return  "El jugador " + jugadorOriginal + " no puede ser su propio siguiente jugador: " + jugadorSiguiente;
    }
}

