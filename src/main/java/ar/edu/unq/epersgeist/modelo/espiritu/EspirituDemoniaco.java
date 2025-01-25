package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Demonio")
public class EspirituDemoniaco extends Espiritu  implements Serializable {

    public EspirituDemoniaco(@NonNull Integer nivelDeConexion, @NonNull String nombre,
                             @NonNull Integer energia, @NonNull Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, energia, ubicacion);
    }

    @Override
    public void bajarEnergia(Integer energia) {
        this.energia -= energia;
        if (this.medium != null && this.getEnergia() <= 0) {
            this.medium.desconectarEspiritu(this);
            this.medium = null;
            this.energia = 0;
        }
    }

    @Override
    public void recuperarEnergiaEn(Ubicacion ubicacion) {
        ubicacion.recuperarEnergiaADemonio(this);
    }

    @Override
    public void serInvocadoEn(Ubicacion ubicacion) {
        ubicacion.invocarDemonio(this);
    }

    @Override
    public void mover(Ubicacion ubicacion) {
        ubicacion.moverDemonio(this);
    }

    @Override
    public boolean puedeParticiparEnExorcismo() {
        return false;
    }

    @Override
    public void atacar(Espiritu espAAtacar) {
    }

    @Override
    public void obtenerBonificacion() {}

}
