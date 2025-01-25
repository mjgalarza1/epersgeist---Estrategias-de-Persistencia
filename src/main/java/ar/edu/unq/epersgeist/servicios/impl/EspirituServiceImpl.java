package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.accionInvalida.*;
import ar.edu.unq.epersgeist.exception.notFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.exception.notFound.EspirituOMediumNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;
    private final PoligonoServiceImpl poligonoService;

    public EspirituServiceImpl(EspirituDAO dao, MediumDAO mediumDAO, PoligonoServiceImpl poligonoService) {
        this.mediumDAO = mediumDAO;
        this.espirituDAO = dao;
        this.poligonoService = poligonoService;
    }

    @Override
    public List<Espiritu> recuperarTodos() { return espirituDAO.findAll(); }

    @Override
    public void actualizar(Espiritu espiritu) { espirituDAO.save(espiritu);}

    @Override
    public void eliminar(Long espirituId) {
        espirituDAO.deleteById(espirituId);
    }

    @Override
    public void crear(Espiritu espiritu) {
       espirituDAO.save(espiritu);
    }

    @Override
    public Espiritu recuperar(Long espirituId) {
        return espirituDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(espirituId));
    }

    @Override
    public void clearAll() {
        espirituDAO.deleteAll();
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        Espiritu espiritu = espirituDAO.findById(espirituId).orElseThrow(EspirituOMediumNoEncontradoException::new);
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(EspirituOMediumNoEncontradoException::new);
        if (medium.getUbicacion() == null) throw new SinUbicacionException(medium);
        if (espiritu.getUbicacion() == null) throw new SinUbicacionException(espiritu);
        espiritu.conectarseAMedium(medium);
        return medium;
    }


    @Override
    public List<Espiritu> espiritusDemoniacos(Direccion dir, int pagina, int cantidad) {
        if (pagina < 1 || cantidad < 1) throw new PaginaOCantidadNegativaException();
        if (dir == null) throw new IllegalArgumentException("La direccion es null");
        Sort sort = Sort.by("energia");

        Pageable pageable = PageRequest.of(pagina - 1, cantidad, sortDeDireccion(dir,sort));

        return espirituDAO.espiritusDemoniacos(pageable);
    }

    @Override
    public void dominar(Long espirituDominanteId, Long espirituADominarId){
        Espiritu espirituDominante = espirituDAO.findById(espirituDominanteId).orElseThrow(EspirituOMediumNoEncontradoException::new);
        Espiritu espirituADominar = espirituDAO.findById(espirituADominarId).orElseThrow(EspirituOMediumNoEncontradoException::new);
        this.validarDistancia(espirituDominante.getUbicacion().getId(), espirituADominar.getUbicacion().getId());
        espirituDominante.dominar(espirituADominar);
    }

    private void validarDistancia(Long idEspirituDominante, Long idEspirituADominar) {
        if (!poligonoService.existeUbicacionEnRango(idEspirituDominante, idEspirituADominar,  2.0,5.0)){
            throw new EspiritusFueraDeRangoException();
        }
    }


    private Sort sortDeDireccion(Direccion dir, Sort sort) {
        return switch (dir) {
            case ASCENDENTE ->
                    sort.ascending();
            case DESCENDENTE ->
                    sort.descending();
            default -> throw new IllegalArgumentException("Tipo de Direccion no reconocida");
        };
    }




}
