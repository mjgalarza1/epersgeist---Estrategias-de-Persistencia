package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionDAO extends JpaRepository<Ubicacion, Long> {

    @Query("select e from Espiritu e where e.ubicacion.id = :ubicacionId")
    List<Espiritu> espiritusEn(@Param("ubicacionId") Long ubicacionId);

    @Query("select m from Medium m left join m.espiritus e where e IS NULL and m.ubicacion.id = ?1")
    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);
    @Query("SELECT u.nombre " +
            "FROM Ubicacion u " +
            "LEFT JOIN Espiritu d ON TYPE(d) = EspirituDemoniaco AND d.ubicacion.id = u.id " +
            "WHERE TYPE(u) = Santuario " +
            "GROUP BY u.id, u.nombre " +
            "ORDER BY (COUNT(d.id) - (SELECT COUNT(a.id) FROM Espiritu a WHERE TYPE(a) = EspirituAngelical AND a.ubicacion.id = u.id)) DESC")
    Page<String> obtenerSantuarioMasCorrupto(Pageable pageable);



    @Query("SELECT m FROM Medium m JOIN Espiritu e ON e.ubicacion.id = m.ubicacion.id " +
            "WHERE m.ubicacion.nombre = ?1 " +
            "AND TYPE(e) = EspirituDemoniaco " +
            "GROUP BY m " +
            "ORDER BY COUNT(e) DESC")
    Page<Medium> obtenerMediumMasCorrupto(String nombreDelSantuario, Pageable pageable);

    @Query("SELECT COUNT (e) from EspirituDemoniaco e WHERE e.ubicacion.nombre = ?1 AND e.medium = null")
    Integer cantDemoniosLibresEn(String nombreDelSantuario);

    @Query("SELECT COUNT (e) FROM EspirituDemoniaco e WHERE e.ubicacion.nombre = ?1")
    Integer cantDemoniosEn(String nombre);

    @Query("SELECT COUNT (e) FROM EspirituAngelical e WHERE e.ubicacion.nombre = ?1")
    Integer cantAngelesEn(String nombre);

    @Query("SELECT u FROM Ubicacion u WHERE u.nombre = ?1")
    Ubicacion findByName(String nombre);

}

