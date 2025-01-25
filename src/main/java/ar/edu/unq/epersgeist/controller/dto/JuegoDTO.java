package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Juego;

public record JuegoDTO (RondaDTO rondaDTO){

    public static JuegoDTO desdeModelo(Juego juego) {
        return new JuegoDTO(
                RondaDTO.desdeModelo(juego.getRondaActual())
        );
    }

    public Juego aModelo() {
        return new Juego();
    }
}
