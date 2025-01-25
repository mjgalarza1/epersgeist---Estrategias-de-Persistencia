package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.persistencia.dto.HabilidadPersistDTO;
import ar.edu.unq.epersgeist.persistencia.dto.PoligonoPersistDTO;
import ar.edu.unq.epersgeist.persistencia.dto.SqlDataDTO;

import java.time.LocalDate;
import java.util.List;

public record SnapshotDTO(LocalDate fecha, SqlDataDTO sql, List<HabilidadPersistDTO> neo4j, List<PoligonoPersistDTO> mongo) {

    public static SnapshotDTO desdeModelo(Snapshot snapshot){
        return new SnapshotDTO(
                snapshot.getFecha(),
                snapshot.getSql(),
                snapshot.getNeo4j(),
                snapshot.getMongo()
        );
    }
}
