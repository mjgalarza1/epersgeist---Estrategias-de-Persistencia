package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.accionInvalida.InerseccionEntrePoligonosException;
import ar.edu.unq.epersgeist.exception.accionInvalida.PoligonoInvalidoException;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.persistencia.dao.PoligonoDAO;
import ar.edu.unq.epersgeist.servicios.PoligonoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.stereotype.Service;
import ar.edu.unq.epersgeist.exception.notFound.PoligonoNoEncontradoException;
import java.util.List;

@Service
public class PoligonoServiceImpl implements PoligonoService {

    private final PoligonoDAO poligonoDAO;

    public PoligonoServiceImpl(PoligonoDAO poligonoDAO) {
        this.poligonoDAO = poligonoDAO;
    }

    @Override
    public void crear(Poligono poligono) {
        try{
            if (!poligonoDAO.poligonosQueInterseccionanCon(poligono.getPosicion()).isEmpty()) throw new InerseccionEntrePoligonosException();
            poligonoDAO.save(poligono);
        }catch (DataIntegrityViolationException | UncategorizedMongoDbException a) {
            throw new PoligonoInvalidoException(a.getMessage());
        }
    }



    @Override
    public void eliminar(String poligonoId) {
        poligonoDAO.deleteById(poligonoId);
    }

    @Override
    public Poligono recuperar(String poligonoId) {
        return poligonoDAO.findById(poligonoId).orElseThrow(PoligonoNoEncontradoException::new);
    }

    @Override
    public List<Poligono> recuperarTodos() {
        return poligonoDAO.findAll();
    }

    @Override
    public void actualizar(Poligono poligono) {
        poligonoDAO.save(poligono);
    }

    @Override
    public boolean existeUbicacionEnRango(Long ubicacionIdPartida, Long ubicacionIdLlegada, Double kilometrosMin, Double kilometrosMax){
        Poligono poligonoPartida = poligonoDAO.findByIdUbicacion(ubicacionIdPartida);
        Poligono poligonoLlegada = poligonoDAO.findByIdUbicacion(ubicacionIdLlegada);

        this.validarKilometros(kilometrosMin, kilometrosMax, poligonoPartida, poligonoLlegada);

        poligonoLlegada = poligonoDAO.existeUbicacionEnRango(
                    ubicacionIdLlegada,
                    poligonoPartida.getPosicion().getPoints().getFirst(),
                    kilometrosMin*1000,
                    kilometrosMax*1000
        );

        return (poligonoLlegada != null);
    }

    private void validarKilometros(Double kilometrosMin, Double kilometrosMax, Poligono poligonoPartida, Poligono poligonoLlegada) {
        if (poligonoPartida == null || poligonoLlegada == null) throw new PoligonoNoEncontradoException();
        if (kilometrosMin < 0 || kilometrosMax < 0) throw new IllegalArgumentException("Las cantidades de kilometros no pueden ser negativas");
        if (kilometrosMin > kilometrosMax) throw new IllegalArgumentException("El valor de kilometrosMin (" + kilometrosMin + ") no puede ser mayor que kilometrosMax (" + kilometrosMax + ").");
    }

    @Override
    public void clearAll() {
        poligonoDAO.deleteAll();
    }


}
