package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.accionInvalida.CoordenadaFueraDeRangoException;
import ar.edu.unq.epersgeist.exception.accionInvalida.EspirituMuyLejanoException;
import ar.edu.unq.epersgeist.exception.accionInvalida.SinUbicacionException;
import ar.edu.unq.epersgeist.exception.notFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.exception.notFound.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.exception.notFound.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.PoligonoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.HabilidadService;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.PoligonoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {
    private final HabilidadService habilidadService;
    private final PoligonoService poligonoService;
    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;
    private final UbicacionDAO ubicacionDAO;
    private final PoligonoDAO poligonoDAO;

    public MediumServiceImpl(HabilidadService habilidadService, PoligonoService poligonoService, MediumDAO mediumDAO, EspirituDAO espirituDAO, UbicacionDAO ubicacionDAO, PoligonoDAO poligonoDAO) {
        this.habilidadService = habilidadService;
        this.poligonoService = poligonoService;
        this.mediumDAO = mediumDAO;
        this.espirituDAO = espirituDAO;
        this.ubicacionDAO = ubicacionDAO;
        this.poligonoDAO = poligonoDAO;
    }

    @Override
    public void crear(Medium medium){
        mediumDAO.save(medium);
    }
    @Override
    public void eliminar(Long idMedium){
        mediumDAO.deleteById(idMedium);
    }

    @Override
    public void actualizar(Medium medium){
        mediumDAO.save(medium);
    }

    @Override
    public Medium obtenerMedium(Long idMedium){
        return mediumDAO.findById(idMedium).orElseThrow(MediumNoEncontradoException::new);
    }

    @Override
    public List<Medium> recuperarTodos(){
        return mediumDAO.findAll();
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId){
        return mediumDAO.espiritus(mediumId);
    }

    @Override
    public void descansar(Long mediumId){
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(MediumNoEncontradoException::new);
        if (medium.getUbicacion() == null) throw new SinUbicacionException(medium);
        medium.descansar();
    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar){
        Medium medium = mediumDAO.findById(idMediumExorcista).orElseThrow(MediumNoEncontradoException::new);
        Medium medium2 = mediumDAO.findById(idMediumAExorcizar).orElseThrow(MediumNoEncontradoException::new);

        medium.exorcizar(medium2);

        Set<Espiritu> espiritus1 = medium.getEspiritus();
        Set<Espiritu> espiritus2 = medium2.getEspiritus();
        espiritus1.forEach(e -> habilidadService.evolucionar(e.getId()));
        espiritus2.forEach(e -> habilidadService.evolucionar(e.getId()));
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(MediumNoEncontradoException::new);
        Espiritu espiritu = espirituDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(espirituId));
        this.validarInvocar(espiritu, medium);
        return medium.invocar(espiritu);
    }

    private void validarInvocar(Espiritu espiritu, Medium medium) {
        Ubicacion espirituUbi = espiritu.getUbicacion();
        Ubicacion mediumUbi = medium.getUbicacion();
        if (mediumUbi == null) throw new SinUbicacionException(medium);
        if (espirituUbi == null) throw new SinUbicacionException(espiritu);
        if(!poligonoService.existeUbicacionEnRango(mediumUbi.getId(), espirituUbi.getId(), 0.0, 100.0)) throw new EspirituMuyLejanoException();
    }

    @Override
    public void mover(Long mediumId, Double latitud, Double longitud) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(MediumNoEncontradoException::new);
        this.validarCoordenada(latitud,longitud);
        Long ubicacionId = poligonoDAO.idDeUbicacionConLasCoords(latitud, longitud);
        if (ubicacionId == null) throw new UbicacionNoEncontradaException();
        Ubicacion ubicacion = ubicacionDAO.findById(ubicacionId).orElseThrow(UbicacionNoEncontradaException::new);
        medium.mover(ubicacion);
    }

    private void validarCoordenada(Double latitud, Double longitud){
        if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180)
            throw new CoordenadaFueraDeRangoException();
    }
    @Override
    public void clearAll() {
        mediumDAO.deleteAll();
    }

}

