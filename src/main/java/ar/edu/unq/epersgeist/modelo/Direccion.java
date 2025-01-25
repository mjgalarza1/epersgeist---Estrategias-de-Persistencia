package ar.edu.unq.epersgeist.modelo;

public enum Direccion {
    DESCENDENTE {
        @Override
        public String getNombre() { return "desc"; }
    }, ASCENDENTE {
        @Override
        public String getNombre() {
            return "asc";
        }
    };

    public abstract String getNombre();
}
