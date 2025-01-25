package ar.edu.unq.epersgeist.modelo.estadoJuego;

import ar.edu.unq.epersgeist.modelo.Juego;
import ar.edu.unq.epersgeist.modelo.ronda.Ronda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "estado")
@NoArgsConstructor @Setter @Getter
@Entity
public abstract class EstadoJuego implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public abstract int cantPuntosObtenidos(Ronda ronda, Character letra);

    @OneToOne
    @JoinColumn(name = "juego_id")
    @Transient
    private Juego juego;
}
