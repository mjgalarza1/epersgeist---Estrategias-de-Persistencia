package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.modelo.Poligono;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("poligonos")
public record PoligonoPersistDTO(@Id String id, GeoJsonPolygon poligono) {

    public static PoligonoPersistDTO desdeModelo(Poligono poligono) {
        return new PoligonoPersistDTO(
                poligono.getId(),
                poligono.getPosicion());
    }

}
