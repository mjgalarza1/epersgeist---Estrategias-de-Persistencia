package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public record CreateMediumDTO (String nombre, Integer manaMax, Integer mana, String ubicacion){

    public static CreateMediumDTO desdeModelo(Medium medium) {
        return new CreateMediumDTO(
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getUbicacion().getNombre());
    }

    public Medium aModelo(Ubicacion ubicacion) {
        Medium medium = new Medium(this.nombre, this.manaMax, this.mana, ubicacion);
        return medium;
    }
}
