package ar.edu.unq.epersgeist.exception.accionInvalida;

public class ListaDeEvaluacionesVaciaException extends AccionInvalidaException {

    @Override
    public String getMessage() {
        return "La lista de evaluaciones esta vacia";
    }
}
