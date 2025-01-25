package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Poligono;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;


public record PoligonoDTO(String id, GeoJsonPolygon poligono) {

    public static PoligonoDTO desdeModelo(Poligono poligono) {
        return new PoligonoDTO(
                poligono.getId(),
                poligono.getPosicion());
    }

}
