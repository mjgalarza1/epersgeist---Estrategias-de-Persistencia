package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.PalabraRondaUltimate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PalabraRondaUltimateService {
    Flux<String> palabraAdivinandoDe(String nombre);
    Mono<Void> crearPalabraAdivinando(PalabraRondaUltimate palabra);
    Mono<PalabraRondaUltimate> actualizarPalabraAdivinando(PalabraRondaUltimate palabra);
    Flux<String> letrasUsadasDe(String id);
}
