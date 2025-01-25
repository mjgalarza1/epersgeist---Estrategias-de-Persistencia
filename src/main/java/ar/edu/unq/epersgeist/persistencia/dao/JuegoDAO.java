package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Juego;
import ar.edu.unq.epersgeist.modelo.ronda.Ronda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoDAO extends JpaRepository<Juego, Long> {

    @Query("SELECT r.intentos FROM Juego j JOIN j.rondaActual r WHERE j.id = ?1")
    int cantIntentosRestantes(Long id);

    @Query("SELECT r.palabraAdivinando FROM Juego j JOIN j.rondaActual r WHERE j.id = ?1")
    String palabraAdivinando(Long id);

    @Query("SELECT r.letrasEquivocadas FROM Juego j JOIN j.rondaActual r WHERE j.id = ?1")
    String letrasEquivocadas(Long id);

    @Query("SELECT r FROM Juego j JOIN j.rondaActual r WHERE j.id = ?1")
    Ronda rondaActualDe(Long id);

}
