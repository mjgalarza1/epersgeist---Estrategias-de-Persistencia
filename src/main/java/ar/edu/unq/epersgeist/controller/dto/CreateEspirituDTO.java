package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.controller.utils.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public record CreateEspirituDTO(Integer energia, Integer nivelDeConexion, String nombre, String nombreUbicacion, TipoEspiritu tipo) {

    public static CreateEspirituDTO desdeModelo(Espiritu espiritu){
        return new CreateEspirituDTO(
                espiritu.getEnergia(),
                espiritu.getNivelDeConexion(),
                espiritu.getNombre(),
                espiritu.getUbicacion().getNombre(),
                TipoEspiritu.valueOf(espiritu.getClass().getSimpleName()));
    }

    public Espiritu aModelo(Ubicacion ubicacion){
        return switch (tipo) {
            case EspirituDemoniaco ->
                    new EspirituDemoniaco(this.nivelDeConexion(), this.nombre(), this.energia, ubicacion);
            case EspirituAngelical ->
                    new EspirituAngelical(this.nivelDeConexion(), this.nombre(), this.energia, ubicacion);
            default -> throw new IllegalArgumentException("Tipo de espíritu no reconocido");
        };
    }
}
