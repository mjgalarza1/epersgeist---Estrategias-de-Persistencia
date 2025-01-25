package ar.edu.unq.epersgeist.exception.accionInvalida;

public class HabilidadDuplicadaException extends AccionInvalidaException {

    public HabilidadDuplicadaException(String nombre) {this.nombre= nombre;}

    private static final long serialVersionUID = 1L;
    private String nombre ;

    @Override
    public String getMessage() {
        return "La Habilidad de nombre ["+ this.nombre +"] ya existe";
    }

}
