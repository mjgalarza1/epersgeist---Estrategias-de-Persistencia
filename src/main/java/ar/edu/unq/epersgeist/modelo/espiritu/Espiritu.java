package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.exception.accionInvalida.*;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.RandomizerEspiritual;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.geo.Point;

import java.io.Serializable;
import java.util.*;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@Getter @Setter @NoArgsConstructor @ToString
@Entity
public abstract class Espiritu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Range(min = 0, max = 100)
    private Integer nivelDeConexion;
    private String nombre;

    @Range(min = -100, max = 100, message = "La energia debe ser menor o igual a 100")
    protected Integer energia;

    protected Integer cantExorcismosEvitados;
    protected Integer cantExorcismosHechos;

    @ManyToOne
    private Ubicacion ubicacion;
    @ManyToOne
    protected Medium medium;

    private Set<String> habilidades;
    private RandomizerEspiritual randomizer;
    @ManyToOne
    protected Espiritu dominante;

    @OneToMany (mappedBy = "dominante", cascade = CascadeType.ALL, fetch = FetchType.EAGER) @EqualsAndHashCode.Exclude
    protected Set<Espiritu> espiritusDominados;

    protected Integer puntosPorEspirituDominado;

    public Espiritu(@NonNull Integer nivelDeConexion, @NonNull String nombre,
                    @NonNull Integer energia, @NonNull Ubicacion ubicacion) {
        this.setearNivelConexionValida(nivelDeConexion);
        this.nombre = nombre;
        this.setearEnergiaValida(energia);
        this.ubicacion = ubicacion;
        this.randomizer = new RandomizerEspiritual();
        this.habilidades = new HashSet<>();
        this.espiritusDominados = new HashSet<>();
        this.cantExorcismosEvitados = 0;
        this.cantExorcismosHechos = 0;
        this.puntosPorEspirituDominado = 0;
        this.dominante = null;
    }

    public void setearEnergiaValida(Integer energia) {
        if (energia >= 0 && energia <= 100) {
            this.energia = energia;
        } else {
            throw new EnergiaInvalidaException(energia);
        }
    }

    public void setearNivelConexionValida(Integer nivelDeConexion) {
        if (nivelDeConexion >= 0 && nivelDeConexion <= 100) {
            this.nivelDeConexion = nivelDeConexion;
        } else {
            throw new NivelConexionInvalidaException(nivelDeConexion);
        }
    }

    public Medium aumentarConexion(Medium medium) {
        this.aumentarConexionHasta100(10);
        return medium;
    }

    public boolean tienePorcentajeExitoso() {
        int suma = randomizer.getRandom() + nivelDeConexion + puntosPorEspirituDominado ;
        return (suma * 100 / 110) > 66;
    }

    public void fortalecerNivelDeConexion(Integer mana) {
        double manaNuevo = (mana * 0.20);
        this.aumentarConexionHasta100((int) manaNuevo);
    }

    public void aumentarConexionHasta100(Integer valorAAumentar){
        this.nivelDeConexion = Math.min(100, this.nivelDeConexion + valorAAumentar );
    }


    public void conectarseAMedium(Medium medium) {
        this.validarConectar(medium);
        medium.conectarseAEspiritu(this);
        this.fortalecerNivelDeConexion(medium.getMana());
    }

    private void validarConectar(Medium medium) {
        if (this.getUbicacion().getNombre() != medium.getUbicacion().getNombre() || !this.esLibre()) throw new ConectarException();
        if (dominante != null) throw new EspirituDominadoException();
    }

    public boolean puedeAtacar(){
        return this.energia >= 10;
    }

    public void mutarA(List<String> habilidadesAMutar){
        this.habilidades.addAll(habilidadesAMutar);
    }


    public void sumarExorcismoEvitado() {
        this.cantExorcismosEvitados += 1;
    }

    public void sumarExorcismosHechos() {
        this.cantExorcismosHechos += 1;
    }


    public void dominar(Espiritu espirituADominar) {
        this.validarDominar(espirituADominar);
        this.espiritusDominados.add(espirituADominar);
        espirituADominar.setDominante(this);
    }

    private void validarDominar(Espiritu espirituADominar) {
        if(!espirituADominar.esLibre() || espirituADominar.getEnergia() > 50) throw new EspirituADominarNoCumpleLosRequerimientosException();
        if(espirituADominar.getDominante() != null) throw new EspirituDominanteNoCumpleLosRequerimientosException();
    }

    private boolean contieneA(Espiritu espiritu) {
        return this.espiritusDominados.contains(espiritu);
    }

    public void recuperarEnergia(Integer energiaExtra) {
        this.energia = Math.min(100, this.energia + energiaExtra);
    }

    public boolean esLibre() {
        return medium == null;
    }

    public abstract void recuperarEnergiaEn(Ubicacion ubicacion) ;

    public void setRandomizerEspiritual(RandomizerEspiritual randomizer) {
        this.randomizer = randomizer;
    }

    @Override // para comparar sin el @EqualsAndHashCode
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Espiritu espiritu = (Espiritu) o;
        return Objects.equals(nombre, espiritu.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    public abstract boolean puedeParticiparEnExorcismo();

    public abstract void atacar(Espiritu espAAtacar);

    public abstract void serInvocadoEn(Ubicacion ubicacion);

    public abstract void mover(Ubicacion ubicacion);

    public abstract void bajarEnergia(Integer energiaABajar);

    public abstract void obtenerBonificacion();
}
