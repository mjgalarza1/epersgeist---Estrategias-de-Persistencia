package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import java.util.List;

public interface UbicacionService {
        void crear(Ubicacion ubicacion, Poligono coordenadas);
        Ubicacion recuperar(Long idDeLaUbicacion);
        List<Ubicacion> recuperarTodos();
        void actualizar(Ubicacion ubicacion);
        void eliminar(Long idDeLaUbicacion);
        List<Espiritu> espiritusEn(Long ubicacionId);
        List<Medium> mediumsSinEspiritusEn(Long ubicacionId);
        void delete(Ubicacion ubicacion);
        ReporteSantuarioMasCorrupto santuarioCorrupto();
        Ubicacion findByName(String name);
        void clearAll();
}

