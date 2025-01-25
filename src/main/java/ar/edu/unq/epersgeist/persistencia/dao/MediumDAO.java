package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediumDAO extends JpaRepository<Medium, Long> {

    @Query(
            "from Espiritu e where e.medium.id = ?1"
    )
    List<Espiritu> espiritus(Long mediumId);

    @Modifying
    @Query(
            "update Medium m set m.ubicacion = null where m.ubicacion.id = ?1"
    )
    void eliminarUbicacionDeMedium(Long ubicacionId);
}
