package ar.edu.unq.epersgeist.exception.accionInvalida;

public class EspirituADominarNoCumpleLosRequerimientosException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    @Override
    public String getMessage() {
        return "El Espiritu a dominar no es libre o tiene mas de 50 de energia";

    }
}
