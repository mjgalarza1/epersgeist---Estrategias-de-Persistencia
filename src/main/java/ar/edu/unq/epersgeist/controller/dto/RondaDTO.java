package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.ronda.Ronda;
import ar.edu.unq.epersgeist.modelo.ronda.RondaUno;

import java.util.List;

public record RondaDTO (String letrasEquivocadas, String palabraAdivinando, int intentos){

    public static RondaDTO desdeModelo(Ronda ronda) {
        return new RondaDTO(
                ronda.getLetrasEquivocadas(),
                ronda.getPalabraAdivinando(),
                ronda.getIntentos()
        );
    }

//    public Ronda aModelo() {
//        Ronda ronda = new RondaUno()
//        return jugador;
//    }
}
