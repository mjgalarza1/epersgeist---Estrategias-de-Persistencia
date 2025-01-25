package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Habilidad;
import ar.edu.unq.epersgeist.modelo.Medium;

public record CreateHabilidadDTO(String nombre) {

    public static CreateHabilidadDTO desdeModelo(Habilidad habilidad) {
        return new CreateHabilidadDTO(habilidad.getNombre());
    }

    public static Habilidad aModelo(CreateHabilidadDTO dto) {
        return new Habilidad(dto.nombre());
    }

}
