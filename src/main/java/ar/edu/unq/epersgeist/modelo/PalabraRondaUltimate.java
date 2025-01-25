package ar.edu.unq.epersgeist.modelo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PalabraRondaUltimate {
    private String id;
    private String palabraAdivinando;
    private String letrasUsadas;

    public PalabraRondaUltimate(String palabraAdivinando) {
        this.palabraAdivinando = palabraAdivinando;
        this.letrasUsadas = "";
    }

    public PalabraRondaUltimate(String palabraAdivinando, String letrasUsadas) {
        this.palabraAdivinando = palabraAdivinando;
        this.letrasUsadas = letrasUsadas;
    }

}
