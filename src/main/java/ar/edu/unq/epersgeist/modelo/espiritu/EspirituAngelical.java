package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Entity
@DiscriminatorValue("Angel")
@NoArgsConstructor
public class EspirituAngelical extends Espiritu  implements Serializable {

    public EspirituAngelical(@NonNull Integer nivelDeConexion, @NonNull String nombre,
                             @NonNull Integer energia, @NonNull Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, energia, ubicacion);
    }

    @Override
    public void atacar(Espiritu espAAtacar) {
        if (this.getEnergia() >= 10) {
            this.cantExorcismosHechos += 1;
            this.bajarEnergia(10);
            this.realizarAtaqueExitoso(espAAtacar);
            this.obtenerBonificacion();
        }
    }

    private void realizarAtaqueExitoso(Espiritu espAAtacar) {
        if (this.tienePorcentajeExitoso()) {
            espAAtacar.bajarEnergia(this.getNivelDeConexion() / 2);
        }
    }

    @Override
    public void bajarEnergia(Integer energiaABajar) {
        this.energia = Math.max(energia - energiaABajar, 0);
    }

    @Override
    public void obtenerBonificacion() {
        this.espiritusDominados.forEach(this::sumarBonificacion);
    }

    private void sumarBonificacion(Espiritu espirituDominado){
        if (espirituDominado.getHabilidades().size() > 3) this.puntosPorEspirituDominado += 2;
    }

    @Override
    public void serInvocadoEn(Ubicacion ubicacion) {
        ubicacion.invocarAngel(this);
    }

    @Override
    public void mover(Ubicacion ubicacion) {
        ubicacion.moverAngel(this);
    }

    @Override
    public void recuperarEnergiaEn(Ubicacion ubicacion) {
        ubicacion.recuperarEnergiaAAngel(this);
    }

    @Override
    public boolean puedeParticiparEnExorcismo() {
        return true;
    }


}
