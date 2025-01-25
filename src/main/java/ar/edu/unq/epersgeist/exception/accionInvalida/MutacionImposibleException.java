package ar.edu.unq.epersgeist.exception.accionInvalida;

public class MutacionImposibleException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  @Override
  public String getMessage() {
    return "La mutacion es imposible porque las habilidades no estan conectadas por las evaluaciones dadas";
  }
}
