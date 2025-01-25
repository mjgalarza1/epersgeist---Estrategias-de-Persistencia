package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Snapshot;

import java.time.LocalDate;


public interface EstadisticaService {
    void crearSnapshot();
    Snapshot obtenerSnapshot(LocalDate fecha);
    void eliminar(Snapshot snapshot);
}
