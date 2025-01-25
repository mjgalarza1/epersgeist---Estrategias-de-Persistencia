package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MediumTestIntegracion {

    private Ubicacion lujan;
    private Medium pepe;
    private Medium jose;
    private Espiritu angelTito;
    private Espiritu levi;
    private Espiritu bel;
    private RandomizerEspiritual random;


    @BeforeEach
    void setUp(){
        random = mock(RandomizerEspiritual.class);

        lujan = new Santuario("Lujan", 80);

        angelTito = new EspirituAngelical(100,"Tito", 100, lujan);
        levi = new EspirituDemoniaco(100,"Levi", 100, lujan);
        bel = new EspirituDemoniaco(100,"Bel", 1, lujan);

        pepe = new Medium("Pepe", 200, 100, lujan);
        jose = new Medium("Jose", 200, 100, lujan);
    }

    @Test
    void exorcizarSuma2ExorcismosHechosAlAngelY2EvitadosALeviY1ABel(){
        bel = new EspirituDemoniaco(100,"Bel", 100, lujan);

        //setteamos el randomizer al angelTito para que siempre haga daño
        when(random.getRandom()).thenReturn(10);
        angelTito.setRandomizerEspiritual(random);

        //conectamos a pepe con angelTito y jose con levi
        pepe.conectarseAEspiritu(angelTito);
        jose.conectarseAEspiritu(levi);
        //ahora el medium pepe exorcisa a jose, es decir que angelTito ataca a levi. Como levi tiene 100 de energia,
        // sobrevive al exorcismo y suma 1 en su cantExorcismosEvitados (que por defecto es 0)
        pepe.exorcizar(jose);

        assertEquals(1, angelTito.getCantExorcismosHechos());
        assertEquals(1, levi.getCantExorcismosEvitados());

        //ahora el medium jose se conecta con  bel
        jose.conectarseAEspiritu(bel);

        //jose es devuelta exorcisado, pero ahora tiene a dos demonios [levi,bel], entonces:
        //  - angelTito ya atacó dos veces, por ende su cantExorcismosHechos es 2.
        //  - levi ya sobrevivió a dos exorcismos seguidos, por ende su cantExorcismosEvitados es 2
        //  - bel no llega a ser atacado ya que solo hay un angel que ataca a levi y termino el exorcismo, por ende suma uno a su cantExorcismosEvitados
        pepe.exorcizar(jose);

        assertEquals(2, angelTito.getCantExorcismosHechos());
        assertEquals(2, levi.getCantExorcismosEvitados());
        assertEquals(1, bel.getCantExorcismosEvitados());
    }

    @Test
    void sumaSoloLosDemoniosQueSobrevivieron(){
        //setteamos el randomizer al angelTito para que siempre haga daño
        when(random.getRandom()).thenReturn(10);
        angelTito.setRandomizerEspiritual(random);

        //conectamos a pepe con angelTito y jose a levi y bel
        pepe.conectarseAEspiritu(angelTito);
        jose.conectarseAEspiritu(bel);
        jose.conectarseAEspiritu(levi);
        //ahora el medium pepe exorcisa a jose, es decir que angelTito ataca a bel, el cual a tener 1 de energia,
        // es desconectado y no suma 1 en su cantExorcismosEvitados
        pepe.exorcizar(jose);

        assertEquals(1, angelTito.getCantExorcismosHechos());
        assertEquals(0, bel.getCantExorcismosEvitados());
        assertEquals(1, levi.getCantExorcismosEvitados());
    }

}