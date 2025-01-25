package ar.edu.unq.epersgeist.exception.notFound;

public class HabilidadNoEncontradaException  extends NoEncontradoException {
    private static final long serialVersionUID = 1L;
    private String nombre;

    public HabilidadNoEncontradaException(String nombre){
        this.nombre = nombre;
    }
    @Override
    public String getMessage() {
        return "La hablidad [" + nombre + "] no se ha encontrado";
    }
}
