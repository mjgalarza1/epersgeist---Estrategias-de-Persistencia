package ar.edu.unq.epersgeist.exception.accionInvalida;

public class NivelConexionInvalidaException extends AccionInvalidaException {
        private static final long serialVersionUID = 1L;
        private Integer nivelDeConexion;

        public NivelConexionInvalidaException(Integer nivelDeConexion){
            this.nivelDeConexion = nivelDeConexion;
        }

        @Override
        public String getMessage() {
            return "El nivelDeConexion [" + nivelDeConexion+ "] debe estar entre 0 y 100";
        }
    }

