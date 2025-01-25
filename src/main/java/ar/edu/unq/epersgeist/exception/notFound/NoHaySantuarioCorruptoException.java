package ar.edu.unq.epersgeist.exception.notFound;

public class NoHaySantuarioCorruptoException extends NoEncontradoException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage(){
        return  "No existe ningun Santuario Corrupto.";
    }
}