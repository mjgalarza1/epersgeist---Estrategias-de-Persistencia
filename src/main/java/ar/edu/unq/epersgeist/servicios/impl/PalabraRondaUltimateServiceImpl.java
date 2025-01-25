package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.PalabraRondaUltimate;
import ar.edu.unq.epersgeist.persistencia.dao.PalabraRondaUltimateDAO;
import ar.edu.unq.epersgeist.servicios.PalabraRondaUltimateService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class PalabraRondaUltimateServiceImpl implements PalabraRondaUltimateService {

    private final PalabraRondaUltimateDAO dao;

    public PalabraRondaUltimateServiceImpl(PalabraRondaUltimateDAO dao) {
        this.dao = dao;
    }

    @Override
    public Flux<String> palabraAdivinandoDe(String id) {
        return dao.palabraAdivinando(id);
    }

    @Override
    public Mono<Void> crearPalabraAdivinando(PalabraRondaUltimate palabra) {
        return dao.crearPalabraAdivinando(palabra);
    }

    @Override
    public Mono<PalabraRondaUltimate> actualizarPalabraAdivinando(PalabraRondaUltimate palabra) {
        return dao.actualizarPalabraAdivinando(palabra);
    }

    @Override
    public Flux<String> letrasUsadasDe(String id) {
        return dao.letrasUsadas(id);
    }

}
