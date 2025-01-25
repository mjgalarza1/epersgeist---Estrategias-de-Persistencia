package ar.edu.unq.epersgeist.exception.accionInvalida;

public class EspirituDominanteNoCumpleLosRequerimientosException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    @Override
    public String getMessage() {
        return "El Espiritu dominante NO puede intentar dominar un espíritu por el cual está siendo dominado";

    }
}
