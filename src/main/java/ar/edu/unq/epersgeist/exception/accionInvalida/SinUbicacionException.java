package ar.edu.unq.epersgeist.exception.accionInvalida;

public class SinUbicacionException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    private Object object;

    public SinUbicacionException(Object object) {
        this.object = object;
    }

    @Override
    public String getMessage(){
        return "El " + object.getClass().getSimpleName() + " no posee ubicacion." ;
    }
}
