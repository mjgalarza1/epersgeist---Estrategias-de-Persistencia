package ar.edu.unq.epersgeist.exception.accionInvalida;

public class ExorcistaSinAngelesException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;
    public ExorcistaSinAngelesException() {
        super();
    }

  @Override
  public String getMessage() {
    return "El exorcista no tiene ningun angel";
  }

}
