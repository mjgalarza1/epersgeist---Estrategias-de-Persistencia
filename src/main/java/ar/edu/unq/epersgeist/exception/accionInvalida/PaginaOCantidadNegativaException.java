package ar.edu.unq.epersgeist.exception.accionInvalida;

public class PaginaOCantidadNegativaException extends AccionInvalidaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage(){
        return  "La pagina o la cantidad por pagina es negativa";
    }
}