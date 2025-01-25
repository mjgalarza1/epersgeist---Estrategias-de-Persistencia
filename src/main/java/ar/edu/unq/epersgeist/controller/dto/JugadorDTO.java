package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Jugador;

public record JugadorDTO (String nombre, boolean esMiTurno, int puntaje, Long idJuego, String jugadorSiguiente, String idPalabraAdivinando){

    public static JugadorDTO desdeModelo(Jugador jugador) {
        return new JugadorDTO(
                jugador.getNombre(),
                jugador.isEsMiTurno(),
                jugador.getPuntuacion(),
                jugador.getIdJuego(),
                jugador.getJugadorSiguiente(),
                jugador.getIdPalabraRondaUltimate()
        );
    }

    public Jugador aModelo() {
        Jugador jugador = new Jugador(this.nombre);
        jugador.setEsMiTurno(this.esMiTurno);
        jugador.setPuntuacion(this.puntaje);
        jugador.setIdJuego(this.idJuego);
        jugador.setJugadorSiguiente(this.jugadorSiguiente);
        jugador.setIdPalabraRondaUltimate(this.idPalabraAdivinando);
        return jugador;
    }
}
