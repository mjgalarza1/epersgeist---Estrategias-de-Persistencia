package ar.edu.unq.epersgeist.exception.accionInvalida;

public class NoHaySuficienteJugadoresException  extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "No hay suficientes jugadores para cambiar el turno";
    }
}
