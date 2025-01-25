package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Document("mediums")
public record MediumPersistDTO(@Id String id, String nombre, Integer manaMax, Integer mana, UbicacionPersistDTO ubicacion, Set<Long> espiritus) {
    public static MediumPersistDTO desdeModelo(Medium medium) {
        return new MediumPersistDTO(
                medium.getId().toString(),
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getUbicacion() != null ? UbicacionPersistDTO.desdeModelo(medium.getUbicacion()) : null,
                medium.getEspiritus().stream()
                        .map(Espiritu::getId)
                        .collect(Collectors.toCollection(HashSet::new)));
    }

}
