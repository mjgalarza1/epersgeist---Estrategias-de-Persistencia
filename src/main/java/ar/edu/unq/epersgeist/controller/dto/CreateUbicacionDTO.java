package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.controller.utils.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;


public record CreateUbicacionDTO( String nombre, int energiaUbicacion, TipoUbicacion tipo, String idPoligono) {

    public static CreateUbicacionDTO desdeModelo(Ubicacion ubicacion){
        return new CreateUbicacionDTO(
                ubicacion.getNombre(),
                ubicacion.getEnergiaUbicacion(),
                TipoUbicacion.valueOf(ubicacion.getClass().getSimpleName()),
                ubicacion.getIdPoligono());
    }

    public Ubicacion aModelo(){
        return switch (tipo) {
            case Santuario ->
                    new Santuario(this.nombre, this.energiaUbicacion);
            case Cementerio ->
                    new Cementerio(this.nombre, this.energiaUbicacion);
            default -> throw new IllegalArgumentException("Tipo de ubicacion no reconocido");
        };
    }
}