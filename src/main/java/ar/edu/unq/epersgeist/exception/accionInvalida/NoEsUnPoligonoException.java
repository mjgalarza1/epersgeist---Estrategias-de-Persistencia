package ar.edu.unq.epersgeist.exception.accionInvalida;

public class NoEsUnPoligonoException extends AccionInvalidaException {
  private static final long serialVersionUID = 1L;

  @Override
  public String getMessage() {
    return "Las coordenadas no forman un poligono";
  }
}
