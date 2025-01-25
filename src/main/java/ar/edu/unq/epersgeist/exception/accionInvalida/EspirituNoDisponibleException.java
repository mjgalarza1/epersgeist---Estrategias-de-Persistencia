package ar.edu.unq.epersgeist.exception.accionInvalida;

public class EspirituNoDisponibleException extends AccionInvalidaException {
    public EspirituNoDisponibleException(String nombre) {this.nombre= nombre;}

    private static final long serialVersionUID = 1L;
    private String nombre ;

    @Override
    public String getMessage() {
      return "el Espiritu no es libre [tiene a "+ this.nombre + " conectado]";
    }

}
