package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.accionInvalida.EspirituNoDisponibleException;
import ar.edu.unq.epersgeist.exception.accionInvalida.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.exception.accionInvalida.ManaInvalidaException;
import ar.edu.unq.epersgeist.exception.notFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.geo.Point;
import org.springframework.data.util.Pair;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
@Entity
public class Medium implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Range(min = 0)
    private Integer manaMax;
    @Range(min = 0)
    private Integer mana;

    @OneToMany (mappedBy = "medium", cascade = CascadeType.ALL, fetch = FetchType.EAGER) @EqualsAndHashCode.Exclude

    protected Set<Espiritu> espiritus;
    @ManyToOne
    private Ubicacion ubicacion;

    public Medium(@NonNull String nombre, @NonNull Integer manaMax, @NonNull Integer mana,
                  @NonNull Ubicacion ubicacion) {
        this.nombre = nombre;
        this.manaMax = setearManaMaxValido(mana, manaMax);
        this.mana = setearManaValido(mana, manaMax);
        this.ubicacion = ubicacion;
        espiritus = new HashSet<>();
    }

    private Integer setearManaMaxValido(@NonNull Integer mana, @NonNull Integer manaMax) {
        if(manaMax >= 0) {
            return manaMax;
        } else {
            throw new ManaInvalidaException(mana, manaMax);
        }
    }

    private Integer setearManaValido(Integer mana, Integer manaMax){
        if(mana >= 0 && mana <= this.manaMax) {
            return mana;
        } else {
            throw new ManaInvalidaException(mana, manaMax);
        }
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        this.espiritus.add(espiritu);
        espiritu.setMedium(this);
    }

    public void aumentarMana(Integer cantAAumentar){
        if (cantAAumentar > 0){
            this.mana = Math.min(this.manaMax, this.mana + cantAAumentar);
        }else{
            throw new ManaInvalidaException(cantAAumentar, manaMax);
        }
    }

    public void desconectarEspiritu(Espiritu espiritu) {
        this.espiritus.remove(espiritu);
    }

    public void descansar() {
        this.aumentarMana(ubicacion.getEnergiaAAumentar());
        espiritus.forEach(e -> e.recuperarEnergiaEn(this.ubicacion));
    }

    public void mover(Ubicacion ubicacion){
        this.ubicacion = ubicacion;
        espiritus.forEach(e -> e.mover(ubicacion));
    }

    public void exorcizar(Medium mediumAExorcizar) {
        validarAngelesVacios();
        List<Espiritu> demonios = mediumAExorcizar.getEspiritus().stream().filter(e -> !e.puedeParticiparEnExorcismo()).collect(Collectors.toList());
        if (!demonios.isEmpty()) {
            this.getEspiritus().forEach(a -> {
                if (demonios.isEmpty()) return;
                this.exorcizarA(demonios, a);
            });
            if(!demonios.isEmpty()) demonios.forEach(Espiritu::sumarExorcismoEvitado);
        }
    }

    private void exorcizarA(List<Espiritu> demonios, Espiritu a){
        Espiritu demonioAAtacar = demonios.getFirst();
        if(!demonios.isEmpty()) {
            demonioAAtacar = demonios.getFirst();
            a.atacar(demonioAAtacar);
        }
        if(demonioAAtacar.getEnergia() <= 0) {
            demonios.remove(demonioAAtacar);
        }
    }

    private void validarAngelesVacios() {
        if(this.getEspiritus().stream().noneMatch(Espiritu::puedeParticiparEnExorcismo)) {
            throw new ExorcistaSinAngelesException();
        }
    }

    public Espiritu invocar(Espiritu e) {
        this.validarInvocar(e);
        if (this.getMana() >= 10) {
            e.serInvocadoEn(this.ubicacion);
            this.mana -= 10;
        }
        return e;
    }

    public void validarInvocar(Espiritu e){
        if (!e.esLibre())  throw new EspirituNoDisponibleException(e.getMedium().getNombre());
    }

}