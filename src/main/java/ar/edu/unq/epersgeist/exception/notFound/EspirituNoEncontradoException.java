package ar.edu.unq.epersgeist.exception.notFound;

public class EspirituNoEncontradoException extends NoEncontradoException {

    private static final long serialVersionUID = 1L;
    private Long id;

    public EspirituNoEncontradoException(Long id){
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "No se encontro al espiritu con el id: " + id;
    }
}

