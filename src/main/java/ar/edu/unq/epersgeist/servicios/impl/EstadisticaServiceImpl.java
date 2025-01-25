package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.persistencia.dao.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dto.HabilidadPersistDTO;
import ar.edu.unq.epersgeist.persistencia.dto.PoligonoPersistDTO;
import ar.edu.unq.epersgeist.persistencia.dto.SqlDataDTO;
import ar.edu.unq.epersgeist.servicios.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service @Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;
    private final UbicacionDAO ubicacionDAO;
    private final HabilidadDAO habilidadDAO;
    private final PoligonoDAO poligonoDAO;
    private final SnapshotDAO snapshotDAO;

    public EstadisticaServiceImpl(EspirituDAO espirituDAO, MediumDAO mediumDAO, UbicacionDAO ubicacionDAO, HabilidadDAO habilidadDAO, PoligonoDAO poligonoDAO, SnapshotDAO snapshotDAO) {
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
        this.ubicacionDAO = ubicacionDAO;
        this.habilidadDAO = habilidadDAO;
        this.poligonoDAO = poligonoDAO;
        this.snapshotDAO = snapshotDAO;
    }


    @Override
    public void crearSnapshot() {
        Date fechaConsulta = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        snapshotDAO.deleteByFecha(fechaConsulta);
        //SQL
        SqlDataDTO sqlDataDTO = SqlDataDTO.desdeModelo(espirituDAO.findAll(),mediumDAO.findAll() ,ubicacionDAO.findAll() );
        //NEO4J
        List<HabilidadPersistDTO> datosNeo4JDB =habilidadDAO.findAll().stream().map(HabilidadPersistDTO::desdeModelo).toList();
        //MONGO
        List<PoligonoPersistDTO> datosMongoDB = poligonoDAO.findAll().stream().map(PoligonoPersistDTO::desdeModelo).toList();

        Snapshot snapshot = new Snapshot(sqlDataDTO, datosNeo4JDB, datosMongoDB, LocalDate.now());

        snapshotDAO.save(snapshot);
    }

    @Override
    public Snapshot obtenerSnapshot(LocalDate fecha) {
        Date fechaConsulta = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Snapshot> snapshotsRecuperados = snapshotDAO.findByFecha(fechaConsulta);

        if (!snapshotsRecuperados.isEmpty()) return snapshotsRecuperados.getFirst();
        return null;
    }

    @Override
    public void eliminar(Snapshot snapshot) {
        snapshotDAO.delete(snapshot);
    }
}
