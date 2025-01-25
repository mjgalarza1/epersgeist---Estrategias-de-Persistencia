package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.PalabraRondaUltimate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PalabraRondaUltimateDAO {
    Mono<Void> crearPalabraAdivinando(PalabraRondaUltimate palabra);
    Flux<String> palabraAdivinando(String id);
    Mono<PalabraRondaUltimate> actualizarPalabraAdivinando(PalabraRondaUltimate palabra);
    Mono<PalabraRondaUltimate> recuperar(String idPalabraRondaUltimate);
    Flux<String> letrasUsadas(String id);
}
