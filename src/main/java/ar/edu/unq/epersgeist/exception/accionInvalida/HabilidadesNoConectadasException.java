package ar.edu.unq.epersgeist.exception.accionInvalida;

public class HabilidadesNoConectadasException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private String nombreOrigen;
  private String nombreDestino;

  public HabilidadesNoConectadasException(String nombreOrigen, String nombreDestino) {
      this.nombreOrigen = nombreOrigen;
      this.nombreDestino = nombreDestino;
  }

  @Override
  public String getMessage() {
    return "La Habilidad ["+ this.nombreOrigen +"] no esta conectada con ["+ this.nombreDestino +"]";
  }

}
