package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Snapshot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface SnapshotDAO extends MongoRepository<Snapshot, String> {

    @Query(value = "{ 'fecha' : ?0 }")
    List<Snapshot> findByFecha(Date fecha);

    @Modifying
    @Transactional
    @Query(value = "{ 'fecha' : ?0 }", delete = true)
    void deleteByFecha(Date fechaConsulta);
}
