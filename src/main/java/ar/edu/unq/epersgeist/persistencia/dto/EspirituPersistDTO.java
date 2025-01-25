package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.controller.utils.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;

@Document("espiritus")
public record EspirituPersistDTO(@Id String id, Integer energia, Integer nivelDeConexion, String nombre, UbicacionPersistDTO ubicacion, TipoEspiritu tipo, MediumSimplePersistDTO medium, Set<String> habilidades){

    public static EspirituPersistDTO desdeModelo(Espiritu espiritu) {
        return new EspirituPersistDTO(
                espiritu.getId().toString(),
                espiritu.getEnergia(),
                espiritu.getNivelDeConexion(),
                espiritu.getNombre(),
                espiritu.getUbicacion() != null ? UbicacionPersistDTO.desdeModelo(espiritu.getUbicacion()) : null,
                TipoEspiritu.valueOf(espiritu.getClass().getSimpleName()),
                espiritu.getMedium() != null ? MediumSimplePersistDTO.desdeModelo(espiritu.getMedium())
                        : new MediumSimplePersistDTO(null, "Este Espiritu no está conectado con ningún Medium"),
                espiritu.getHabilidades());
    }

}
