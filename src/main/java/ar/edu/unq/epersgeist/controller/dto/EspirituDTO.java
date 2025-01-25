package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.controller.utils.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;

import java.util.List;
import java.util.Set;

public record EspirituDTO(Long id, Integer energia, Integer nivelDeConexion, String nombre, UbicacionDTO ubicacion, TipoEspiritu tipo, MediumSimpleDTO medium, Set<String> habilidades){

    public static EspirituDTO desdeModelo(Espiritu espiritu) {
        return new EspirituDTO(
        espiritu.getId(),
        espiritu.getEnergia(),
        espiritu.getNivelDeConexion(),
        espiritu.getNombre(),
        espiritu.getUbicacion() != null ? UbicacionDTO.desdeModelo(espiritu.getUbicacion()) : null,
        TipoEspiritu.valueOf(espiritu.getClass().getSimpleName()),
        espiritu.getMedium() != null ? MediumSimpleDTO.desdeModelo(espiritu.getMedium())
                : new MediumSimpleDTO(null, "Este Espiritu no está conectado con ningún Medium"),
        espiritu.getHabilidades());
    }

    public Espiritu aModelo(){
        Espiritu esp;
        switch (tipo) {
            case EspirituDemoniaco -> {
                esp = new EspirituDemoniaco(this.nivelDeConexion(), this.nombre(), this.energia, this.ubicacion.aModelo());
                esp.setHabilidades(this.habilidades);
                return esp;
            }
            case EspirituAngelical -> {
                esp = new EspirituAngelical(this.nivelDeConexion(), this.nombre(), this.energia, this.ubicacion.aModelo());
                esp.setHabilidades(this.habilidades);
                return esp;
            }
            default -> throw new IllegalArgumentException("Tipo de espíritu no reconocido");
        }
    }
}
