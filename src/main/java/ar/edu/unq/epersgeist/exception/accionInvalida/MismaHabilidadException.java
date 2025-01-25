package ar.edu.unq.epersgeist.exception.accionInvalida;

public class MismaHabilidadException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    private String nombreHabilidad;

    public MismaHabilidadException(String nombreHabilidad) {
        this.nombreHabilidad = nombreHabilidad;
    }

        @Override
        public String getMessage () {
            return "el nombre " + this.nombreHabilidad + "de la habilidad es la misma";
        }
}

