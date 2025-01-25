package ar.edu.unq.epersgeist.exception.notFound;

public class NoHaySantuariosException extends NoEncontradoException {

    private static final long serialVersionUID = 1L;
    @Override
    public String getMessage() {
        return "No hay Santuarios en la base";
    }

}
