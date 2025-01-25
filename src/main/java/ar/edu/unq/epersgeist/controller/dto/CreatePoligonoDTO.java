package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Poligono;
import org.springframework.data.geo.Point;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record CreatePoligonoDTO(double[][] coordenadas) {

    public static CreatePoligonoDTO desdeModelo(Poligono poligono){
        return new CreatePoligonoDTO(
            convertirAListaDeCoordenadas(poligono.getPosicion().getPoints())
        );
    }

    private static List<Point> convertirAPositions(double[][] coordenadas) {
        return Arrays.stream(coordenadas)
                .map(coordArray -> new Point(coordArray[0], coordArray[1]))
                .collect(Collectors.toList());
    }

    private static double[][] convertirAListaDeCoordenadas(List<Point> posiciones) {
        return posiciones.stream()
                .map(position -> new double[]{position.getX(), position.getY()})
                .toArray(double[][]::new);
    }

    public Poligono aModelo(){
        return new Poligono(convertirAPositions(this.coordenadas));
    }
}
