package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAO extends JpaRepository<Espiritu, Long> {


    @Query("SELECT e FROM EspirituDemoniaco e")
    List<Espiritu> espiritusDemoniacos(Pageable pageable);

    @Modifying
    @Query(
            "update Espiritu e set e.ubicacion = null where e.ubicacion.id = ?1"
    )
    void eliminarUbicacionDeEspiritu(Long ubicacionId);
//
@Query(value = "SELECT * FROM ESPIRITU", nativeQuery = true)
List<Espiritu> recuperarTodos();

}