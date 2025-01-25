package ar.edu.unq.epersgeist.exception.accionInvalida;

public class SinIntentosException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "No es posible evaluar más letras debido a que no quedan intentos en la ronda actual";
    }
}
