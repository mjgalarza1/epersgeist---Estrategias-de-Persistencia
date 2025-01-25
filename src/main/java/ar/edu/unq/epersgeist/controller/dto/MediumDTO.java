package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record MediumDTO (Long id, String nombre, Integer manaMax, Integer mana, UbicacionDTO ubicacion, Set<EspirituDTO> espiritus) {

    public static MediumDTO desdeModelo(Medium medium) {
        return new MediumDTO(
                medium.getId(),
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getUbicacion() != null ? UbicacionDTO.desdeModelo(medium.getUbicacion()) : null,
                medium.getEspiritus().stream()
                        .map(EspirituDTO::desdeModelo)
                        .collect(Collectors.toCollection(HashSet::new)));
    }

    public Medium aModelo() {
        Medium medium = new Medium(this.nombre, this.manaMax, this.mana, this.ubicacion.aModelo());
        medium.setId(this.id);
        medium.setEspiritus(this.espiritus != null ?
                this.espiritus.stream()
                        .map(EspirituDTO::aModelo)
                        .collect(Collectors.toCollection(HashSet::new)) :
                new HashSet<>()
        );
        return medium;
    }
}
