package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EpersgeistApplication.class)
public class EstadisticaServiceImplTest {

    @Autowired
    private EstadisticaService estadisticaService;
    @Autowired
    private MediumService serviceMedium;
    @Autowired
    private HabilidadService habilidadService;
    @Autowired
    private UbicacionService serviceUbicacion;
    @Autowired
    private PoligonoService servicePoligono;

    @Test
    void crearSnapshotYRecuperar(){
        LocalDate fechaHoy = LocalDate.now();
        estadisticaService.crearSnapshot();
        Snapshot ssCreadaHoy = estadisticaService.obtenerSnapshot(fechaHoy);
        assertEquals(fechaHoy, ssCreadaHoy.getFecha());
        estadisticaService.eliminar(ssCreadaHoy);
    }

    @Test
    void crearSnapshotSiemprePisaElDeLaFechaActual(){
        LocalDate fechaHoy = LocalDate.now();

        // se crea un snapshot sin nada persistido
        estadisticaService.crearSnapshot();
        Snapshot primerSnapshotCreada = estadisticaService.obtenerSnapshot(fechaHoy);

        // se setean cosas para crear un segundo snapshot con cosas persistidas
        Poligono poligonoCABA = new Poligono(List.of(
                new Point(1.0, 1.0),
                new Point(1.0, 1.2),
                new Point(1.2, 1.2),
                new Point(1.2, 1.0),
                new Point(1.0, 1.0)
        ));
        servicePoligono.crear(poligonoCABA);

        Santuario argentina = new Santuario("Argentina",99);
        serviceUbicacion.crear(argentina,poligonoCABA);

        Medium unMedium = new Medium("Pepe", 200, 100, argentina);
        serviceMedium.crear(unMedium);

        Habilidad unaHabilidad = new Habilidad("Cura++");
        habilidadService.crear(unaHabilidad);

        // se crea el segundo snapshot
        estadisticaService.crearSnapshot();
        Snapshot snapshotMasReciente = estadisticaService.obtenerSnapshot(fechaHoy);

        assertNotEquals(primerSnapshotCreada.getId(), snapshotMasReciente.getId());
        assertEquals(fechaHoy, snapshotMasReciente.getFecha());
        assertEquals(poligonoCABA.getId(), snapshotMasReciente.getMongo().getFirst().id());
        assertEquals("Argentina", snapshotMasReciente.getSql().ubicaciones().getFirst().nombre());
        assertEquals("Pepe", snapshotMasReciente.getSql().mediums().getFirst().nombre());
        assertEquals("Cura++", snapshotMasReciente.getNeo4j().getFirst().nombre());

        serviceMedium.eliminar(unMedium.getId());
        habilidadService.eliminar(unaHabilidad.getNombre());
        serviceUbicacion.eliminar(argentina.getId());
        servicePoligono.eliminar(poligonoCABA.getId());
        estadisticaService.eliminar(snapshotMasReciente);
    }

    @Test
    void recuperarSnapshotVacio(){
        Snapshot snapshotNull = estadisticaService.obtenerSnapshot(LocalDate.now());

        assertNull(snapshotNull);
    }

    @Test
    void eliminarExitoso(){
        estadisticaService.crearSnapshot();
        Snapshot snapshotRecuperada = estadisticaService.obtenerSnapshot(LocalDate.now());
        estadisticaService.eliminar(snapshotRecuperada);

        assertNull(estadisticaService.obtenerSnapshot(LocalDate.now()));
    }

}
