package ar.edu.unq.epersgeist.exception.accionInvalida;

public class HabilidadNoMutadaException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String nombreHabilidad;

    public HabilidadNoMutadaException(String nombreHabilidad) {
        this.nombreHabilidad = nombreHabilidad;
    }

    @Override
    public String getMessage() {
        return "El espiritu todavia no muto a ["+ this.nombreHabilidad +"]";
    }

}
