package ar.edu.unq.epersgeist.controller.rest;

import ar.edu.unq.epersgeist.controller.dto.JugadorDTO;
import ar.edu.unq.epersgeist.controller.dto.NombresDTO;
import ar.edu.unq.epersgeist.controller.utils.Validator;
import ar.edu.unq.epersgeist.modelo.Juego;
import ar.edu.unq.epersgeist.modelo.Jugador;
import ar.edu.unq.epersgeist.servicios.JuegoService;
import ar.edu.unq.epersgeist.servicios.JugadorService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/jugador")
public class JugadorREST {

    private final JugadorService jugadorService;
    private final JuegoService juegoService;

    public JugadorREST(JugadorService jugadorService, JuegoService juegoService) {
        this.jugadorService = jugadorService;
        this.juegoService = juegoService;
    }



    @PostMapping()
    public ResponseEntity<JugadorDTO> crearJugador(@RequestBody String nombre){
        Validator.getInstance().validarJugador(nombre);
        Jugador jugador = new Jugador(nombre);
        jugadorService.crearJugador(jugador, 123L).subscribe();
        return ResponseEntity.ok(JugadorDTO.desdeModelo(jugador));
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<Mono<JugadorDTO>> buscarJugador(@PathVariable String nombre) {
        Mono<JugadorDTO> jugador = jugadorService.buscarJugador(nombre).map(JugadorDTO::desdeModelo);
        return ResponseEntity.ok(jugador);
    }

    @GetMapping(value = "/ranking", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity <Flux<JugadorDTO>> streamRanking() {
        Flux<JugadorDTO> ranking = jugadorService.obtenerRanking()
                .map(JugadorDTO::desdeModelo);
        return ResponseEntity.ok(ranking);
    }

    @PutMapping(value = "/ranking")
    public ResponseEntity <String> detenerRanking() {
        try{
            jugadorService.detenerRanking();
            return ResponseEntity.ok("Ranking detenido con exito");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("No se pudo detener el ranking: " + e.getMessage());
        }
    }

    @PutMapping("/{nombre}/adivinarLetra/{letra}")
    public ResponseEntity<Mono<JugadorDTO>> adivinarLetra(@PathVariable String nombre, @PathVariable Character letra) {
        try{

            Validator.getInstance().validarLetra(letra);
            Validator.getInstance().validarJugador(nombre);

            Jugador jugador = jugadorService.buscarJugador(nombre).block();
            Long idJuego = jugador.getIdJuego();

            Validator.getInstance().validarIdDeJuego(idJuego);

            Juego juegoRecuperado = juegoService.recuperarJuego(idJuego);

            jugadorService.adivinarLetra(jugador, letra, juegoRecuperado).subscribe();

            return ResponseEntity.ok(jugadorService.buscarJugador(nombre).map(JugadorDTO::desdeModelo));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Mono.error(e));
        }

    }

    //este tenemos que eliminar
    @PutMapping("/{nombre}/{puntaje}")
    public ResponseEntity<Mono<JugadorDTO>> actualizar(@PathVariable String nombre, @PathVariable int puntaje) {
        Mono<JugadorDTO> jugadorActualizado = jugadorService.buscarJugador(nombre)
                .flatMap(jugador -> {
                    jugador.setPuntuacion(puntaje);
                    return jugadorService.actualizar(jugador);
                })
                .map(JugadorDTO::desdeModelo);

        return ResponseEntity.ok(jugadorActualizado);
    }

    @GetMapping("/{nombre}/puntaje")
    public ResponseEntity<Mono<Integer>> obtenerPuntaje(@PathVariable String nombre) {
        Mono<Integer> puntaje = jugadorService.obtenerPuntaje(nombre);
        return ResponseEntity.ok(puntaje);
    }

    @GetMapping("/top")
    public ResponseEntity<List<JugadorDTO>> obtenerTop(){
        List<Jugador> top = jugadorService.obtenerTop();
        return ResponseEntity.ok(top.stream().map(JugadorDTO::desdeModelo).toList());
    }

}
