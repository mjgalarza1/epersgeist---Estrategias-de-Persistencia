package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;

public interface MediumService {
    void crear(Medium medium);
    void eliminar(Long mediumId);
    Medium obtenerMedium(Long id);
    void actualizar(Medium medium);
    List<Medium> recuperarTodos();
    void descansar(Long mediumId);
    void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar);
    List<Espiritu> espiritus(Long mediumId);
    Espiritu invocar(Long mediumId, Long espirituId);
    void mover(Long mediumId, Double latitud, Double longitud);
    void clearAll();
}
