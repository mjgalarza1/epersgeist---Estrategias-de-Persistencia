package ar.edu.unq.epersgeist.controller.rest;

import ar.edu.unq.epersgeist.modelo.PalabraRondaUltimate;
import ar.edu.unq.epersgeist.servicios.impl.PalabraRondaUltimateServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin
@RequestMapping("/palabraRondaUltimate")
public class PalabraRondaUltimateREST {

    private final PalabraRondaUltimateServiceImpl palabraRondaUltimateService;

    public PalabraRondaUltimateREST(PalabraRondaUltimateServiceImpl palabraRondaUltimateService) {
        this.palabraRondaUltimateService = palabraRondaUltimateService;
    }

    @GetMapping(value = "/{id}/palabraAdivinando", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> escucharJugadorP(@PathVariable String id) {
        return ResponseEntity.ok(palabraRondaUltimateService.palabraAdivinandoDe(id));
    }

    @GetMapping(value = "/{id}/letrasUsadas", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> escucharJugadorL(@PathVariable String id) {
        return ResponseEntity.ok(palabraRondaUltimateService.letrasUsadasDe(id));
    }

    @PostMapping()
    public ResponseEntity<PalabraRondaUltimate> crearPalabraAdivinando(@RequestBody String palabra){
        PalabraRondaUltimate palabraU = new PalabraRondaUltimate(palabra);
        palabraRondaUltimateService.crearPalabraAdivinando(palabraU).subscribe();
        return ResponseEntity.ok(palabraU);
    }

}
