package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.exception.accionInvalida.*;
import ar.edu.unq.epersgeist.exception.notFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.exception.notFound.HabilidadNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EpersgeistApplication.class)
public class HabilidadServiceImplTest {

    private Habilidad habilidad;
    private Habilidad invisibilidad;
    private Habilidad rayosx;

    @Autowired
    private HabilidadService habilidadService;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private PoligonoService poligonoService;

    private Ubicacion quilmes;
    private Poligono poligonoQuilmes;

    @BeforeEach
    void setUp(){

        quilmes = new Santuario("Quilmes", 30);
        poligonoQuilmes = new Poligono((List.of(
                new Point(0, 0),
                new Point(3, 6),
                new Point(6, 1),
                new Point(0, 0)
        )));

        habilidad = new Habilidad("Una habilidad");
        invisibilidad = new Habilidad("Invisibilidad");
        rayosx = new Habilidad("RayosX");

        habilidadService.crear(habilidad);
        habilidadService.crear(invisibilidad);
        habilidadService.crear(rayosx);

        poligonoService.crear(poligonoQuilmes);
        ubicacionService.crear(quilmes,poligonoQuilmes);
        System.out.println("EL ID DEL POLIGONO:" + poligonoQuilmes.getId());
        quilmes = ubicacionService.recuperar(quilmes.getId());

    }

    @Test
    public void crear() {
        assertEquals("Una habilidad",habilidadService.recuperar("Una habilidad").getNombre());
    }

    @Test
    public void descubrirHabilidad() {
        habilidadService.clearAll();
        habilidad = new Habilidad("Una habilidad");
        Habilidad origen = new Habilidad("Origen");
        Habilidad destino = new Habilidad("Destino");

        habilidadService.crear(habilidad);
        habilidadService.crear(origen);
        habilidadService.crear(destino);

        Condicion condicion = new Condicion(5, Evaluacion.ENERGIA);

        habilidadService.descubrirHabilidad("Origen", "Destino", condicion);

        Habilidad habilidadRecuperada = habilidadService.recuperar("Origen");
        Habilidad habilidadDestinoRecuperada = habilidadService.recuperar("Destino");

        assertEquals("Origen",habilidadRecuperada.getNombre());
        assertEquals("Destino",habilidadDestinoRecuperada.getNombre());
        assertEquals(Evaluacion.ENERGIA, habilidadService.recuperarEvaluacionDeCondicionDe(habilidadRecuperada, habilidadDestinoRecuperada));
        assertEquals(5, habilidadService.recuperarCantidadDeCondicionDe(habilidadRecuperada, habilidadDestinoRecuperada));
    }

    @Test
    public void descubrirHabilidadConMismaHabilidad(){
        Habilidad origen = new Habilidad("Origen");

        habilidadService.crear(origen);

        Condicion condicion = new Condicion(5, Evaluacion.ENERGIA);

        //habilidadService.descubrirHabilidad("Origen", "Origen", condicion);
        assertThrows(MismaHabilidadException.class, () -> habilidadService.descubrirHabilidad("Origen", "Origen", condicion));
    }

    @Test
    public void descibrirHabilidadConHabilidadesYaConectadasAlReves(){
        habilidadService.clearAll();
        Habilidad origen = new Habilidad("Origen");
        Habilidad destino = new Habilidad("Destino");

        habilidadService.crear(origen);
        habilidadService.crear(destino);

        Condicion condicion = new Condicion(5, Evaluacion.ENERGIA);
        Condicion nivdecon = new Condicion(10, Evaluacion.NIVEL_DE_CONEXION);

        habilidadService.descubrirHabilidad("Origen", "Destino", condicion);

        assertThrows(HabilidadesYaConectadasException.class, () -> habilidadService.descubrirHabilidad("Destino", "Origen", condicion));
    }

    @Test
    public void descubrirHabilidadConUnaHabilidadQueNoExiste(){
        Habilidad origen = new Habilidad("Origen");

        habilidadService.crear(origen);

        Condicion condicion = new Condicion(5, Evaluacion.ENERGIA);

        assertThrows(HabilidadNoEncontradaException.class, () -> habilidadService.descubrirHabilidad("Origen", "Destino", condicion));
    }


    @Test
    public void recuperarHabilidadQueNoExiste() {
        assertThrows(HabilidadNoEncontradaException.class, () -> habilidadService.recuperar("pepe"));
    }

    @Test
    public void crearDuplicado() {
        Habilidad habilidadDuplicada =  new Habilidad("Una habilidad");
        assertThrows(HabilidadDuplicadaException.class, () -> habilidadService.crear(habilidadDuplicada));
    }

    @Test
    public void evolucionarEnUnEspirituSinHabilidadesLoHaceEvolucionarALasRaices(){
        habilidadService.clearAll();
        Habilidad habilidad = new Habilidad("Una habilidad");

        Espiritu angel = new EspirituAngelical(80, "Angel", 100, quilmes);

        habilidadService.crear(habilidad);
        espirituService.crear(angel);

        habilidadService.evolucionar(angel.getId());
        Espiritu espRecuperado = espirituService.recuperar(angel.getId());

        assertEquals(1, espRecuperado.getHabilidades().size());
    }

    @Test
    public void evolucionarEnUnEspirituConUnaHabilidadHaceQueEvolucioneALasHabilidadesQuePuedeMutar(){
        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);

        Espiritu angel = new EspirituAngelical(80, "Angel", 100, quilmes);

        espirituService.crear(angel);

        habilidadService.evolucionar(angel.getId());

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);

        habilidadService.evolucionar(angel.getId());

        Espiritu espRecuperado = espirituService.recuperar(angel.getId());

        assertEquals(3, espRecuperado.getHabilidades().size());
    }

    @Test
    public void espirituNoEvolucionaPorqueYaPoseeLaHabilidad(){
        habilidadService.clearAll();
        Habilidad habilidad = new Habilidad("Una habilidad");

        Espiritu angel = new EspirituAngelical(80, "Angel", 100, quilmes);

        habilidadService.crear(habilidad);
        espirituService.crear(angel);

        habilidadService.evolucionar(angel.getId());
        habilidadService.evolucionar(angel.getId());

        Espiritu espRecuperado = espirituService.recuperar(angel.getId());

        assertEquals(1, espRecuperado.getHabilidades().size());
    }

    @Test
    public void espirituNoEvolucionaPorqueNoHayHabilidades(){
        Espiritu angel = new EspirituAngelical(80, "Angel", 100, quilmes);

        espirituService.crear(angel);

        habilidadService.clearAll();

        habilidadService.evolucionar(angel.getId());

        Espiritu espRecuperado = espirituService.recuperar(angel.getId());

        assertEquals(0, espRecuperado.getHabilidades().size());
    }

    @Test
    public void evolucionarEnUnEspirituConUnaHabilidadHaceQueEvolucioneALasHabilidadesQuePuedeMutarSOLOAUnSalto(){
        Habilidad teletransportacion = new Habilidad("Teletransportacion");
        Habilidad transmutacion = new Habilidad("Transmutacion");

        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);

        Espiritu angel = new EspirituAngelical(80, "Angel", 100, quilmes);

        espirituService.crear(angel);

        habilidadService.crear(teletransportacion);
        habilidadService.crear(transmutacion);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);
        habilidadService.descubrirHabilidad("Invisibilidad", "Teletransportacion", condicion);
        habilidadService.descubrirHabilidad("RayosX", "Transmutacion", condicion);

        habilidadService.evolucionar(angel.getId());

        habilidadService.evolucionar(angel.getId());

        Espiritu espRecuperado = espirituService.recuperar(angel.getId());

        assertEquals(3, espRecuperado.getHabilidades().size());
        assertTrue(espRecuperado.getHabilidades().contains("Una habilidad"));
        assertTrue(espRecuperado.getHabilidades().contains("RayosX"));
        assertTrue(espRecuperado.getHabilidades().contains("Invisibilidad"));
    }

    @Test
    public void evolucionarEnUnEspirituQueNoExiste(){
        habilidadService.clearAll();
        Habilidad habilidad = new Habilidad("Una habilidad");

        habilidadService.crear(habilidad);

        assertThrows(EspirituNoEncontradoException.class, () -> habilidadService.evolucionar(123L));
    }

    @Test
    public void habilidadesConectadas(){
        Habilidad teletransportacion = new Habilidad("Teletransportacion");
        Habilidad transmutacion = new Habilidad("Transmutacion");

        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);

        habilidadService.crear(teletransportacion);
        habilidadService.crear(transmutacion);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);
        habilidadService.descubrirHabilidad("Invisibilidad", "Teletransportacion", condicion);
        habilidadService.descubrirHabilidad("RayosX", "Transmutacion", condicion);

        Set<Habilidad> habilidades = habilidadService.habilidadesConectadas("Una habilidad");
        assertEquals(2, habilidades.size());
    }

    @Test
    public void habilidadesConectadasSinHabilidadesConectadas(){
        Set<Habilidad> habilidades = habilidadService.habilidadesConectadas("Una habilidad");
        assertEquals(0, habilidades.size());
    }

    @Test
    public void habilidadesConectadasDeHabilidadNoPersistida(){
        Set<Habilidad> habilidades = habilidadService.habilidadesConectadas(" ");
        assertEquals(0, habilidades.size());
    }

    @Test
    public void habilidadesPosiblesDevuelveLasRaices(){
        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);

        Espiritu angel = new EspirituAngelical(80, "Angel", 100, quilmes);

        espirituService.crear(angel);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);

        Set<Habilidad> habilidadesPosibles = habilidadService.habilidadesPosibles(angel.getId());

        assertTrue(habilidadesPosibles.contains(habilidad));
        assertEquals(1, habilidadesPosibles.size());
    }

    @Test
    public void habilidadesPosiblesTraeTodasPorqueElEspirituCumpleLaCondicion(){
        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);

        Espiritu angel = new EspirituAngelical(80, "Angel", 100, quilmes);

        espirituService.crear(angel);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);

        habilidadService.evolucionar(angel.getId());

        Set<Habilidad> habilidadesPosibles = habilidadService.habilidadesPosibles(angel.getId());

        assertTrue(habilidadesPosibles.contains(invisibilidad));
        assertTrue(habilidadesPosibles.contains(rayosx));
        assertEquals(2, habilidadesPosibles.size());
    }

    @Test
    public void habilidadesPosiblesTraeSoloLasHabilidadesEnLasQueElEspirituCumpleLaCondicion(){
        Habilidad absorcion = new Habilidad("Absorcion");

        Condicion energia10 = new Condicion(10, Evaluacion.ENERGIA);
        Condicion nivdecon = new Condicion(10, Evaluacion.NIVEL_DE_CONEXION);
        Condicion energia90 = new Condicion(90, Evaluacion.ENERGIA);

        Espiritu angel = new EspirituAngelical(80, "Angel", 50, quilmes);

        espirituService.crear(angel);

        habilidadService.crear(absorcion);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", energia10);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", nivdecon);
        habilidadService.descubrirHabilidad("RayosX", "Absorcion", energia90);

        habilidadService.evolucionar(angel.getId());

        Set<Habilidad> habilidadesPosibles = habilidadService.habilidadesPosibles(angel.getId());

        assertTrue(habilidadesPosibles.contains(invisibilidad));
        assertTrue(habilidadesPosibles.contains(rayosx));
        assertEquals(2, habilidadesPosibles.size());
    }

    @Test
    public void habilidadesPosiblesNoDevuelveNadaPorqueElEspirituNoCumpleNingunaCondicion(){
        Habilidad absorcion = new Habilidad("Absorcion");

        Condicion energia10 = new Condicion(100, Evaluacion.ENERGIA);
        Condicion nivdecon = new Condicion(90, Evaluacion.NIVEL_DE_CONEXION);
        Condicion energia90 = new Condicion(90, Evaluacion.ENERGIA);

        Espiritu angel = new EspirituAngelical(80, "Angel", 50, quilmes);

        espirituService.crear(angel);

        habilidadService.crear(absorcion);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", energia10);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", nivdecon);
        habilidadService.descubrirHabilidad("RayosX", "Absorcion", energia90);

        habilidadService.evolucionar(angel.getId());

        Set<Habilidad> habilidadesPosibles = habilidadService.habilidadesPosibles(angel.getId());

        assertFalse(habilidadesPosibles.contains(invisibilidad));
        assertFalse(habilidadesPosibles.contains(rayosx));
        assertFalse(habilidadesPosibles.contains(absorcion));
        assertEquals(0, habilidadesPosibles.size());
    }

    @Test
    public void habilidadesPosiblesEnEspirituInexistente(){
        Condicion energia10 = new Condicion(100, Evaluacion.ENERGIA);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", energia10);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", energia10);

        assertThrows(EspirituNoEncontradoException.class, () -> habilidadService.evolucionar(123L));
    }

    @Test
    public void habilidadesPosiblesSinHabilidades(){
        habilidadService.clearAll();

        Espiritu angel = new EspirituAngelical(80, "Angel", 50, quilmes);

        espirituService.crear(angel);

        Set<Habilidad> habilidades= habilidadService.habilidadesPosibles(angel.getId());
        assertEquals(0, habilidades.size());
    }

    @Test
    public void caminoMasRentable(){
        Habilidad absorcion = new Habilidad("Absorcion");
        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);
        Condicion otraCondicion = new Condicion(10, Evaluacion.CANT_EXORCISMOS_EVITADOS);

        habilidadService.crear(absorcion);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);
        habilidadService.descubrirHabilidad("Invisibilidad", "Absorcion", condicion);
        habilidadService.descubrirHabilidad("RayosX", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("RayosX", "Absorcion", otraCondicion);

        List<Habilidad> camino = habilidadService.caminoMasRentable("Una habilidad", "Absorcion", Set.of(Evaluacion.ENERGIA));
        assertEquals(habilidad, camino.getFirst());
        assertEquals(invisibilidad, camino.get(1));
        assertEquals(absorcion, camino.get(2));
        assertEquals(3, camino.size());
    }

    @Test
    public void caminoMasRentableConDosCaminosIgualDeRentablesTraeCualquieraDeLosDos(){
        Habilidad absorcion = new Habilidad("Absorcion");
        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);

        habilidadService.crear(absorcion);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);
        habilidadService.descubrirHabilidad("Invisibilidad", "Absorcion", condicion);
        habilidadService.descubrirHabilidad("RayosX", "Absorcion", condicion);

        List<Habilidad> camino = habilidadService.caminoMasRentable("Una habilidad", "Absorcion", Set.of(Evaluacion.ENERGIA));
        assertEquals(3, camino.size());
    }

    @Test
    public void caminoMasRentableConListaDeEvaluacionesVacia(){
        Habilidad absorcion = new Habilidad("Absorcion");
        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);
        Condicion otraCondicion = new Condicion(10, Evaluacion.CANT_EXORCISMOS_EVITADOS);

        habilidadService.crear(absorcion);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);
        habilidadService.descubrirHabilidad("Invisibilidad", "Absorcion", condicion);
        habilidadService.descubrirHabilidad("RayosX", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("RayosX", "Absorcion", otraCondicion);

        assertThrows(ListaDeEvaluacionesVaciaException.class, () -> habilidadService.caminoMasRentable("Una habilidad", "Absorcion", Set.of()));
    }

    @Test
    public void caminoMasRentableConHabilidadesQueNoEstanConectadas(){
        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);

        assertThrows(HabilidadesNoConectadasException.class, () -> habilidadService.caminoMasRentable("Invisibilidad", "RayosX", Set.of(Evaluacion.ENERGIA)));
    }

    @Test
    public void caminoMasRentableConHabilidadesQueEstanConectadasPeroNoPorLasEvaluacionesDadas(){
        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);
        Habilidad absorcion = new Habilidad("Absorcion");

        habilidadService.crear(absorcion);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);
        habilidadService.descubrirHabilidad("Invisibilidad", "Absorcion", condicion);

        assertThrows(MutacionImposibleException.class, () -> habilidadService.caminoMasRentable("Una habilidad", "Absorcion", Set.of(Evaluacion.NIVEL_DE_CONEXION)));
    }

    @Test
    public void caminoMasRentableSinHabilidadesPersistidas(){
        assertThrows(HabilidadesNoConectadasException.class, () -> habilidadService.caminoMasRentable(" ", " ", Set.of(Evaluacion.NIVEL_DE_CONEXION)));
    }

    @Test
    public void caminoMasRentableConMuchasCondiciones(){
        Habilidad absorcion = new Habilidad("Absorcion");
        Condicion energia = new Condicion(10, Evaluacion.ENERGIA);
        Condicion exorev = new Condicion(10, Evaluacion.CANT_EXORCISMOS_EVITADOS);
        Condicion nivdecon = new Condicion(20, Evaluacion.NIVEL_DE_CONEXION);

        habilidadService.crear(absorcion);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", energia);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", exorev);
        habilidadService.descubrirHabilidad("Invisibilidad", "Absorcion", nivdecon);
        habilidadService.descubrirHabilidad("RayosX", "Invisibilidad", nivdecon);
        habilidadService.descubrirHabilidad("RayosX", "Absorcion", energia);

        List<Habilidad> camino = habilidadService.caminoMasRentable("Una habilidad", "Absorcion", Set.of(Evaluacion.CANT_EXORCISMOS_EVITADOS, Evaluacion.NIVEL_DE_CONEXION));
        assertEquals(habilidad, camino.getFirst());
        assertEquals(rayosx, camino.get(1));
        assertEquals(invisibilidad, camino.get(2));
        assertEquals(absorcion, camino.get(3));
        assertEquals(4, camino.size());
    }

    @Test
    public void CaminoMasRentableConMismaHabilidad(){
        Condicion energia = new Condicion(10, Evaluacion.ENERGIA);
        Condicion exorev = new Condicion(10, Evaluacion.CANT_EXORCISMOS_EVITADOS);

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", energia);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", exorev);

        assertThrows(HabilidadesNoConectadasException.class, () -> habilidadService.caminoMasRentable("Una habilidad", "Una habilidad", Set.of(Evaluacion.ENERGIA)));
    }

    @Test
    public void caminoMasMutable(){
        Habilidad absorcion = new Habilidad("Absorcion");
        Condicion condicion = new Condicion(10, Evaluacion.ENERGIA);
        Condicion otraCondicion = new Condicion(10, Evaluacion.CANT_EXORCISMOS_EVITADOS);

        Espiritu crack = new EspirituAngelical(30, "Franco", 20, quilmes);

        espirituService.crear(crack);
        habilidadService.crear(absorcion);

        habilidadService.evolucionar(crack.getId());

        habilidadService.descubrirHabilidad("Una habilidad", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("Una habilidad", "RayosX", condicion);
        habilidadService.descubrirHabilidad("Invisibilidad", "Absorcion", condicion);
        habilidadService.descubrirHabilidad("RayosX", "Invisibilidad", condicion);
        habilidadService.descubrirHabilidad("RayosX", "Absorcion", otraCondicion);

        List<Habilidad> camino = habilidadService.caminoMasMutable(crack.getId(), "Una habilidad");
        assertEquals(habilidad, camino.getFirst());
        assertEquals(rayosx, camino.get(1));
        assertEquals(invisibilidad, camino.get(2));
        assertEquals(absorcion, camino.get(3));
        assertEquals(4, camino.size());
    }

    @Test
    public void caminoMasMutablePeroElEspirituNoExiste(){
        assertThrows(EspirituNoEncontradoException.class, () -> habilidadService.caminoMasMutable(123L, "Una habilidad"));
    }


    @Test
    public void caminoMenosMutableConHabilidadesConectadasPorLaMismaCondicion(){
        habilidadService.clearAll();
        espirituService.clearAll();

        Habilidad a = new Habilidad("A");
        Habilidad b = new Habilidad("B");
        Habilidad c = new Habilidad("C");
        Habilidad d = new Habilidad("D");
        Habilidad e = new Habilidad("E");
        Habilidad f = new Habilidad("F");
        Habilidad g = new Habilidad("G");
        Habilidad h = new Habilidad("H");

        Espiritu crack = new EspirituAngelical(30, "Franco", 20, quilmes);

        espirituService.crear(crack);
        habilidadService.crear(a);

        Condicion energia10 = new Condicion(10, Evaluacion.ENERGIA);

        habilidadService.evolucionar(crack.getId());

        habilidadService.crear(b);
        habilidadService.crear(c);
        habilidadService.crear(d);
        habilidadService.crear(e);
        habilidadService.crear(f);
        habilidadService.crear(g);
        habilidadService.crear(h);

        habilidadService.descubrirHabilidad("A","B", energia10);
        habilidadService.descubrirHabilidad("B","C", energia10);
        habilidadService.descubrirHabilidad("A","D", energia10);
        habilidadService.descubrirHabilidad("D","E", energia10);
        habilidadService.descubrirHabilidad("E","F", energia10);

        // A -> B -> C          (cumplen TODAS las condiciones, y ES EL CAMINO MÁS CORTO)
        // A -> D -> E -> F     (cumplen TODAS las condiciones, pero es el camino más largo)

        List<Habilidad> camino = habilidadService.caminoMenosMutable( "A", crack.getId());

        assertEquals(3, camino.size());
        assertTrue(camino.contains(a));
        assertTrue(camino.contains(b));
        assertTrue(camino.contains(c));
    }

    @Test
    public void caminoMenosMutableConHabilidadesConectadasPorDiferentesCondiciones(){
        Condicion energia10 = new Condicion(10, Evaluacion.ENERGIA);
        Condicion energia200 = new Condicion(200,Evaluacion.ENERGIA);
        Condicion exorEvitados = new Condicion(500,Evaluacion.CANT_EXORCISMOS_EVITADOS);

        habilidadService.clearAll();
        espirituService.clearAll();

        Habilidad a = new Habilidad("A");
        Habilidad b = new Habilidad("B");
        Habilidad c = new Habilidad("C");
        Habilidad d = new Habilidad("D");
        Habilidad e = new Habilidad("E");
        Habilidad f = new Habilidad("F");
        Habilidad g = new Habilidad("G");
        Habilidad h = new Habilidad("H");

        Espiritu crack = new EspirituAngelical(30, "Franco", 20, quilmes);

        espirituService.crear(crack);
        habilidadService.crear(a);

        habilidadService.evolucionar(crack.getId());

        habilidadService.crear(b);
        habilidadService.crear(c);
        habilidadService.crear(d);
        habilidadService.crear(e);
        habilidadService.crear(f);
        habilidadService.crear(g);
        habilidadService.crear(h);

        habilidadService.descubrirHabilidad("A","B", energia200);
        habilidadService.descubrirHabilidad("A","C", energia10);
        habilidadService.descubrirHabilidad("C","E", exorEvitados);
        habilidadService.descubrirHabilidad("C","D", energia10);
        habilidadService.descubrirHabilidad("D","H", energia10);
        habilidadService.descubrirHabilidad("A","F", energia10);
        habilidadService.descubrirHabilidad("F","G", energia10);

        // A -> B               (no cumple con la condición entre A -> B, por lo tanto, no se considera)
        // A -> C -> E          (no cumple con la condición entre C -> E, por lo tanto, no se considera)
        // A -> C -> D -> H     (cumplen todas las condiciones, pero es el camino más largo)
        // A -> F -> G          (cumplen TODAS las condiciones, y ES EL CAMINO MÁS CORTO)

        List<Habilidad> camino = habilidadService.caminoMenosMutable( "A", crack.getId());

        assertEquals(3, camino.size());
        assertTrue(camino.contains(a));
        assertTrue(camino.contains(f));
        assertTrue(camino.contains(g));
    }

    @Test
    public void caminoMenosMutableTiraExceptionSiElEspirituNoTieneLaHabilidadDada(){
        Espiritu crack = new EspirituAngelical(30, "Franco", 20, quilmes);

        espirituService.crear(crack);

        assertThrows(HabilidadNoMutadaException.class, () -> habilidadService.caminoMenosMutable( "A", crack.getId()));
    }

    @Test
    public void caminoMenosMutableDevuelveUnaListaVaciaPorFaltaDeHabilidadesAMutar(){
        habilidadService.clearAll();
        espirituService.clearAll();

        Espiritu crack = new EspirituAngelical(30, "Franco", 20, quilmes);

        Habilidad a = new Habilidad("A");
        habilidadService.crear(a);

        espirituService.crear(crack);
        habilidadService.evolucionar(crack.getId());

        List<Habilidad> camino = habilidadService.caminoMenosMutable( "A", crack.getId());

        assertEquals(0, camino.size());
    }

    @Test
    public void caminoMenosMutablePeroElEspirituNoExiste(){
        assertThrows(EspirituNoEncontradoException.class, () -> habilidadService.caminoMasMutable(123L, "Una habilidad"));
    }

    @AfterEach()
    public void cleanUp(){
        habilidadService.clearAll();
        espirituService.clearAll();
        ubicacionService.clearAll();
        poligonoService.eliminar(poligonoQuilmes.getId());
    }
}
