package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
@Document("Poligono")
public class Poligono  implements Serializable {
    @Id
    private String id;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPolygon posicion;

    private Long idUbicacion;

    public Poligono (List<Point> posicion){
        this.posicion = new GeoJsonPolygon(posicion);
    }

}
