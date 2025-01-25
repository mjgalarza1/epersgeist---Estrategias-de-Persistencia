package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Poligono;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PoligonoDAO extends MongoRepository<Poligono, String> {

        @Aggregation(pipeline = {
                "{ $match: { 'posicion': { $geoIntersects: { $geometry: { 'type': 'Point', 'coordinates': [ ?1, ?0 ] } } } } }",
                "{ $project: { 'idUbicacion': 1, '_id': 0 } }"
        })
        Long idDeUbicacionConLasCoords(Double latitud, Double longitud);

        @Query(
                "{idUbicacion: ?0, 'posicion': {$nearSphere: {$geometry: ?1, $minDistance: ?2, $maxDistance: ?3}}}"
        )
        Poligono existeUbicacionEnRango(Long idUbicacion, Point puntoDePartida, Double kilometrosMin, Double kilometrosMax);


        @Query("{idUbicacion: ?0}")
        Poligono findByIdUbicacion(Long ubicacionId);

        @Query(
                value = "{ 'posicion': { $geoIntersects: { $geometry: ?0 } } }"
        )
        List<Poligono> poligonosQueInterseccionanCon(GeoJsonPolygon coordenadas);
}


