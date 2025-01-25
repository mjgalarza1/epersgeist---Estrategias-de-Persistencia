package ar.edu.unq.epersgeist.modelo.ronda;

import ar.edu.unq.epersgeist.exception.accionInvalida.LetraUsadaException;
import ar.edu.unq.epersgeist.exception.accionInvalida.SinIntentosException;
import ar.edu.unq.epersgeist.modelo.Juego;
import ar.edu.unq.epersgeist.modelo.Jugador;
import ar.edu.unq.epersgeist.modelo.RandomizerEspiritual;
import ar.edu.unq.epersgeist.modelo.estadoJuego.Adivinado;
import ar.edu.unq.epersgeist.modelo.estadoJuego.Equivocado;
import ar.edu.unq.epersgeist.modelo.estadoJuego.EstadoJuego;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "numero")
@Data
@Entity
public abstract class Ronda implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    protected List<String> palabrasPosibles;
    private RandomizerEspiritual randomizer;
    private String letrasUsadas; // Cambiado a String
    private String palabraAAdivinar; // Cambiado a String
    private String letrasEquivocadas; // Cambiado a String
    private String palabraAdivinando; // Cambiado a String
    private int intentos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estado_id")
    private EstadoJuego estado;

    @OneToOne
    @JoinColumn(name = "juego_id")
    @Transient
    private Juego juego;

    public Ronda() {
        this.randomizer = new RandomizerEspiritual();
        this.letrasEquivocadas = "";
        this.letrasUsadas = "";
        this.intentos = 6;
        this.estado = new Equivocado();
        this.setComienzoDeRonda();
    }

    public String getPalabraRandom() {
        return palabrasPosibles.get(randomizer.getRandomHasta(palabrasPosibles.size()));
    }

    public void settearPalabraAAdivinar(String palabraAAdivinar){
        this.palabraAAdivinar = palabraAAdivinar;
        this.convertirPalabraAdivinando();
    }

    public int evaluarLetra(Character letra, Jugador jugador) {
        if (intentos <= 0) throw new SinIntentosException();
        this.validarLetra(letra);
        this.estado = new Equivocado();
        List<Character> palabraAAdivinarLista = convertirStringALista(palabraAAdivinar);
        List<Character> palabraAdivinandoLista = convertirStringALista(palabraAdivinando);

        for (int i = 0; i < palabraAAdivinarLista.size(); i++) {
            if (palabraAAdivinarLista.get(i).equals(letra)) {
                palabraAdivinandoLista.set(i, letra);
                estado = new Adivinado();
            }
        }
        palabraAdivinando = convertirListaAString(palabraAdivinandoLista);
        letrasUsadas += letra; // Agregamos la letra directamente al String
        return estado.cantPuntosObtenidos(this, letra);
    }

    private void validarLetra(Character letra) {
        if (letrasUsadas.contains(letra.toString())) throw new LetraUsadaException(letra);
    }

    public int letraEquivocada(Character letra) {
        intentos--;
        letrasEquivocadas += letra; // Agregamos la letra directamente al String
        if (this.getIntentos() == 0) {
            return -5;
        }
        return -1;
    }

    public int letraAcertada(Character letra) {
        if (this.esPalabraAcertada()) return 5;
        return 1;
    }

    private void convertirPalabraAdivinando() {
        List<Character> inicial = new ArrayList<>();
        List<Character> palabraAAdivinarLista = convertirStringALista(palabraAAdivinar);
        for (int i = 0; i < palabraAAdivinar.length(); i++) {
               this.convertirPalabra(inicial, palabraAAdivinarLista.get(i));
        }
        palabraAdivinando = convertirListaAString(inicial);
    }

    private void convertirPalabra(List<Character> inicial, Character letra){
        if (letra != ' '){
            inicial.add('_');
        }else{
            inicial.add(' ');
        }
    }

    protected abstract void setComienzoDeRonda();

    protected void elegirPalabraRandom(){
        palabraAAdivinar = palabrasPosibles.get(randomizer.getRandomHasta(palabrasPosibles.size()));
        this.convertirPalabraAdivinando();
    }

    public boolean esPalabraAcertada() {
        return palabraAdivinando.equals(palabraAAdivinar);
    }

    // Métodos utilitarios para conversión
    private List<Character> convertirStringALista(String str) {
        List<Character> lista = new ArrayList<>();
        for (char c : str.toCharArray()) {
            lista.add(c);
        }
        return lista;
    }

    private String convertirListaAString(List<Character> lista) {
        StringBuilder sb = new StringBuilder();
        for (Character c : lista) {
            sb.append(c);
        }
        return sb.toString();
    }

    public void cambiarTurnoA(Jugador jugador, Jugador jugadorSiguiente) {
    }

    public abstract Ronda proximaRonda();

}
