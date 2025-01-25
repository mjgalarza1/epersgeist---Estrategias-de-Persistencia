package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Poligono;

import java.util.List;

public interface PoligonoService {
    void crear(Poligono poligono);
    void eliminar(String poligonoId);
    Poligono recuperar(String poligonoId);
    List<Poligono> recuperarTodos();
    void actualizar(Poligono poligono);
    boolean existeUbicacionEnRango(Long ubicacionIdPartida, Long ubicacionIdLlegada, Double kilometrosMin, Double kilometrosMax);
    void clearAll();
}
