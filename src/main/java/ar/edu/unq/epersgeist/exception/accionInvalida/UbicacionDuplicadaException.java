package ar.edu.unq.epersgeist.exception.accionInvalida;

public class UbicacionDuplicadaException extends AccionInvalidaException {

    public UbicacionDuplicadaException(String nombre) {this.nombre= nombre;}

    private static final long serialVersionUID = 1L;
    private String nombre ;

    @Override
    public String getMessage() {
        return "La Ubicacion de nombre ["+ this.nombre +"] ya existe";
    }
}
