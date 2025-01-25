package ar.edu.unq.epersgeist.exception.accionInvalida;

public class LetraUsadaException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    private Character letra;

    public LetraUsadaException(Character letra) {
        this.letra = letra;
    }

    @Override
    public String getMessage() {
        return "La letra [" + this.letra + "] ya ha sido utilizada";
    }
}
