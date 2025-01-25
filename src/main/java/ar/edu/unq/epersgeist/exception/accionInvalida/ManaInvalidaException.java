package ar.edu.unq.epersgeist.exception.accionInvalida;

public class ManaInvalidaException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    private Integer mana;
    private Integer manaMax;

    public ManaInvalidaException(Integer mana, Integer manaMax){
        this.mana = mana;
        this.manaMax = manaMax;
    }

    @Override
    public String getMessage() {
        return "El mana [" + mana + "] debe estar entre 0 y [" + manaMax + "]";
    }
}
