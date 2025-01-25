package ar.edu.unq.epersgeist.exception.accionInvalida;

public class EnergiaInvalidaException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    private Integer energia;

    public EnergiaInvalidaException(Integer energia){
        this.energia = energia;
    }

    @Override
    public String getMessage() {
        return "La energia [" + energia + "] debe estar entre 0 y 100";
    }
}
