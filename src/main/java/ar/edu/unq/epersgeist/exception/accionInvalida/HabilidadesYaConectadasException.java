package ar.edu.unq.epersgeist.exception.accionInvalida;

public class HabilidadesYaConectadasException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    private String nombreOrigen;
    private String nombreDestino;


    public HabilidadesYaConectadasException(String nombreOrigen, String nombreDestino) {
        this.nombreOrigen = nombreOrigen;
        this.nombreDestino = nombreDestino;
    }

    @Override
    public String getMessage() {
        return "las habilidades ya estan conectadas en sentido ["+ this.nombreOrigen + "] -> [" + this.nombreDestino +"]";
    }
}
