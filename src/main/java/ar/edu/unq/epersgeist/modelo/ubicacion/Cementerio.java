package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.exception.accionInvalida.InvocarEspirituEnUbicacionInvalidaException;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@DiscriminatorValue("Cementerio") @NoArgsConstructor
@Entity
public class Cementerio extends Ubicacion {

    public Cementerio(@NonNull String nombre,@NonNull Integer energiaUbicacion) {
        super(nombre, energiaUbicacion);
    }

    @Override
    public void invocarAngel(Espiritu espiritu){
       throw new InvocarEspirituEnUbicacionInvalidaException(this, espiritu);
    }

    @Override
    public void invocarDemonio(Espiritu espiritu) {
        this.moverDemonio(espiritu);
    }

    @Override
    public Integer getEnergiaAAumentar() {
        double  cantAAumentar =  this.energiaUbicacion * 0.50;
        return (int) cantAAumentar;
    }

    @Override
    public void recuperarEnergiaAAngel(Espiritu espiritu) {
    }

    @Override
    public void recuperarEnergiaADemonio(Espiritu espiritu) {
        espiritu.recuperarEnergia(this.energiaUbicacion);
    }

    @Override
    public void moverAngel(Espiritu espiritu) {
        espiritu.setUbicacion(this);
        espiritu.bajarEnergia(5);
    }

    @Override
    public void moverDemonio(Espiritu espiritu) {
        espiritu.setUbicacion(this);
    }


}
