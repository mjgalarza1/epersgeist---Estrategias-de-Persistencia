package ar.edu.unq.epersgeist.exception.notFound;

public class RondaNoEncontradaException extends NoEncontradoException {
    private static final long serialVersionUID = 1L;
    private String nombre;

    public RondaNoEncontradaException(String nombre){
        this.nombre = nombre;
    }
    @Override
    public String getMessage() {
        return "La palabra [" + nombre + "] no se ha encontrado";
    }
}

