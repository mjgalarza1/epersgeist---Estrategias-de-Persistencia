package ar.edu.unq.epersgeist.modelo;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Random;

@EqualsAndHashCode
public class RandomizerEspiritual implements Serializable {

    public Random random;

    public RandomizerEspiritual() {
        random = new Random();
    }

    public int getRandom() {
        return random.nextInt(1, 10);
    }

    public int getRandomHasta(int max) {
        return random.nextInt(1, max);
    }

}
