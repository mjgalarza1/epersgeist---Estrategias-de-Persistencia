package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.accionInvalida.UbicacionDuplicadaException;
import ar.edu.unq.epersgeist.exception.notFound.NoHaySantuarioCorruptoException;
import ar.edu.unq.epersgeist.exception.notFound.NoHaySantuariosException;
import ar.edu.unq.epersgeist.exception.notFound.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.PoligonoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionDAO dao;
    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;
    private final PoligonoDAO poligonoDAO;

    public UbicacionServiceImpl(UbicacionDAO dao, MediumDAO mediumDAO, EspirituDAO espirituDAO, PoligonoDAO poligonoDAO) {
        this.dao = dao;
        this.mediumDAO = mediumDAO;
        this.espirituDAO = espirituDAO;
        this.poligonoDAO = poligonoDAO;
    }

    @Override
    public void crear(Ubicacion ubicacion, Poligono coordenadas) {
        try{
            ubicacion.setIdPoligono(coordenadas.getId());
            Ubicacion ubiPersistida = dao.save(ubicacion);
            coordenadas.setIdUbicacion(ubiPersistida.getId());
            poligonoDAO.save(coordenadas);
        }catch (DataIntegrityViolationException d){
            throw new UbicacionDuplicadaException(ubicacion.getNombre());
        }

    }

    @Override
    public Ubicacion recuperar(Long ubicacionId) {
        return dao.findById(ubicacionId).orElseThrow(UbicacionNoEncontradaException::new);
    }

    @Override
    public List<Ubicacion> recuperarTodos() { return dao.findAll(); }

    @Override
    public void actualizar(Ubicacion ubicacion) { dao.save(ubicacion); }

    @Override
    public void eliminar(Long ubicacionId) {
        mediumDAO.eliminarUbicacionDeMedium(ubicacionId);
        espirituDAO.eliminarUbicacionDeEspiritu(ubicacionId);
        dao.deleteById(ubicacionId);
    }


    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return dao.espiritusEn(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return dao.mediumsSinEspiritusEn(ubicacionId);
    }

    @Override
    public void delete(Ubicacion ubicacion) {
        mediumDAO.eliminarUbicacionDeMedium(ubicacion.getId());
        espirituDAO.eliminarUbicacionDeEspiritu(ubicacion.getId());
        dao.delete(ubicacion);
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<String> pageSantuario = dao.obtenerSantuarioMasCorrupto(pageable);
        if (pageSantuario.getContent().isEmpty()) {
            throw new NoHaySantuariosException();
        }

        String santuarioMasCorrupto = pageSantuario.getContent().getFirst();

        Integer cantDemonios =  dao.cantDemoniosEn(santuarioMasCorrupto);
        if (cantDemonios <= dao.cantAngelesEn(santuarioMasCorrupto)) throw new NoHaySantuarioCorruptoException();

        Page<Medium> pageMedium = dao.obtenerMediumMasCorrupto(santuarioMasCorrupto, pageable);
        Medium mediumMasCorrupto = pageMedium.getContent().isEmpty() ? null : pageMedium.getContent().getFirst();

        ReporteSantuarioMasCorrupto reporte = new ReporteSantuarioMasCorrupto(
                santuarioMasCorrupto,
                mediumMasCorrupto,
                cantDemonios,
                dao.cantDemoniosLibresEn(santuarioMasCorrupto));
        return reporte;
    }


    @Override
    public Ubicacion findByName(String name) {
        Ubicacion ubi = dao.findByName(name);
        if(null == ubi) throw new UbicacionNoEncontradaException();
        return ubi;
    }
    @Override
    public void clearAll() {
        dao.deleteAll();
    }



}



