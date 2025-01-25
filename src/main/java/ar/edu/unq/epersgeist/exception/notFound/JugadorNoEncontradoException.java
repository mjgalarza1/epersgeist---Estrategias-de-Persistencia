package ar.edu.unq.epersgeist.exception.notFound;

public class JugadorNoEncontradoException  extends NoEncontradoException {
    private static final long serialVersionUID = 1L;
    private String nombre;

    public JugadorNoEncontradoException(String nombre){
        this.nombre = nombre;
    }
    @Override
    public String getMessage() {
        return "No se encontro al jugador con el nombre: " + nombre;
    }
}
