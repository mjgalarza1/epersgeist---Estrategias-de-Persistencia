package ar.edu.unq.epersgeist.servicios.ubicacion;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.exception.notFound.NoHaySantuarioCorruptoException;
import ar.edu.unq.epersgeist.exception.notFound.NoHaySantuariosException;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.PoligonoService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes =  EpersgeistApplication.class)
public class SantuarioMasCorruptoTest {

    @Autowired
    private EspirituService serviceEspiritu;

    @Autowired
    private UbicacionService serviceUbicacion;

    @Autowired
    private MediumService serviceMedium;

    @Autowired
    private PoligonoService poligonoService;

    private Poligono poligonoJujuy;
    private Poligono poligonoMendoza;
    private Poligono poligonoMiami;

    private  Ubicacion jujuy;
    private  Ubicacion mendoza;
    private  Ubicacion miami;

    private  Medium mediumCorrupto;
    private  Medium mediumNormal;

    private  EspirituDemoniaco pombero;
    private  EspirituDemoniaco lallorona;
    private  EspirituDemoniaco bu;
    private  EspirituDemoniaco lucifer;
    private  EspirituDemoniaco fidel;
    private  EspirituDemoniaco charly;

    private EspirituAngelical jorge;

    @BeforeEach
    void setUp(){
        //POLIGONOS
// Polígono Jujuy: Mantiene su forma pero se mueve a la izquierda y abajo
        poligonoJujuy = new Poligono((List.of(
                new Point(0, 0),
                new Point(3, 6),
                new Point(6, 1),
                new Point(0, 0)
        )));

// Polígono Mendoza: Se mueve más lejos, evitando cualquier intersección
        poligonoMendoza = new Poligono((List.of(
                new Point(20, 10),  // Cambiado de (10, 10) a (20, 10)
                new Point(23, 16),  // Cambiado de (13, 16) a (23, 16)
                new Point(26, 11),  // Cambiado de (16, 11) a (26, 11)
                new Point(20, 10)   // Cambiado de (10, 10) a (20, 10)
        )));

// Polígono Miami: Se mueve más lejos para evitar intersección con los otros
        poligonoMiami = new Poligono((List.of(
                new Point(-10, -10),
                new Point(-13, -16),
                new Point(-16, -11),
                new Point(-10, -10)
        )));


        //SANTUARIOS
        jujuy = new Santuario("JUJUY", 20);
        mendoza = new Santuario("MENDOZA", 99);

        miami = new Cementerio("Miami", 99);

        //MEDIUMS
        mediumCorrupto = new Medium("Corrupto", 200, 100, jujuy);
        mediumNormal = new Medium("Normal", 200, 100, mendoza);

        //DEMONIOS
        //En "JUJUY"
        pombero = new EspirituDemoniaco(100, "Pombero", 100, jujuy);
        lallorona = new EspirituDemoniaco(100, "LaLlorona", 100, jujuy);
        bu = new EspirituDemoniaco(100, "Bu", 100, jujuy);
        lucifer = new EspirituDemoniaco(100, "Lucifer", 100, jujuy);
        //En "MENDOZA"
        fidel = new EspirituDemoniaco(100, "Fidel", 100, mendoza);
        charly = new EspirituDemoniaco(100, "Charly", 100, miami);

        //ANGELES
        jorge = new EspirituAngelical(100, "Jorge", 100, jujuy);

        //EN Jujuy hay 4 demonios (pombero,lallorona,bu, lucifer) y un angel (jorge)
        // En Mendoza hay 1 demonio (fidel) y 0 angeles
        //Por ende el que tiene mayor diferencia es Jujuy porque 3 ( 4 - 1 ) es mayor a 1 (1 - 0)

        //SE PERSISTEN:
        poligonoService.crear(poligonoJujuy);
        poligonoService.crear(poligonoMendoza);
        poligonoService.crear(poligonoMiami);

        serviceUbicacion.crear(jujuy,poligonoJujuy);
        serviceUbicacion.crear(mendoza,poligonoMendoza);
        serviceUbicacion.crear(miami,poligonoMiami);

        serviceEspiritu.crear(pombero);
        serviceEspiritu.crear(lallorona);
        serviceEspiritu.crear(bu);
        serviceEspiritu.crear(lucifer);
        serviceEspiritu.crear(fidel);
        serviceEspiritu.crear(charly);
        serviceEspiritu.crear(jorge);


        serviceMedium.crear(mediumCorrupto);
        serviceMedium.crear(mediumNormal);
    }

    @Test
    void devuelveElSantuarioMasCorrupto(){
        //En Jujuy:
        //  Se conectan los demonios pombero y lucifer con mediumCorrupto por ende dejan de ser libres
        serviceEspiritu.conectar(pombero.getId(), mediumCorrupto.getId());
        serviceEspiritu.conectar(lucifer.getId(), mediumCorrupto.getId());
        // y el angel jorge tambien
        serviceEspiritu.conectar(jorge.getId(), mediumCorrupto.getId());

        //En Mendoza
        // fidel deja de ser libre ya que se conecta con mediumNormal
        serviceEspiritu.conectar(fidel.getId(), mediumNormal.getId());

        //Se pide el reporte
        ReporteSantuarioMasCorrupto reporte = serviceUbicacion.santuarioCorrupto();

        assertEquals("JUJUY", reporte.getNombreDelSantuario());
        assertEquals(mediumCorrupto, reporte.getMediumConMasDemonios());
        assertEquals(4, reporte.getCantTotalDemonios());
        assertEquals(2, reporte.getCantDemoniosLibres());
    }

    @Test
    void devuelveElMediumMasCorruptoHabiendoMasDeUnoEnLaMismaUbicacion(){
        Medium medium = new Medium("Jose", 200, 100, jujuy);
        serviceMedium.crear(medium);
        //En Jujuy:
        //  Se conectan los demonios pombero y lucifer con mediumCorrupto por ende dejan de ser libres
        serviceEspiritu.conectar(pombero.getId(), mediumCorrupto.getId());
        serviceEspiritu.conectar(lucifer.getId(), mediumCorrupto.getId());
        // y el angel jorge tambien
        serviceEspiritu.conectar(jorge.getId(), mediumCorrupto.getId());

        //En Mendoza
        // fidel deja de ser libre ya que se conecta con mediumNormal
        serviceEspiritu.conectar(fidel.getId(), mediumNormal.getId());

        //Se pide el reporte
        ReporteSantuarioMasCorrupto reporte = serviceUbicacion.santuarioCorrupto();

        assertEquals(mediumCorrupto, reporte.getMediumConMasDemonios());
        serviceMedium.eliminar(medium.getId());
    }

    @Test
    void santuarioMasCorruptoPeroNoHayEspiritus(){
        serviceEspiritu.eliminar(jorge.getId());
        serviceEspiritu.eliminar(pombero.getId());
        serviceEspiritu.eliminar(lallorona.getId());
        serviceEspiritu.eliminar(lucifer.getId());
        serviceEspiritu.eliminar(fidel.getId());
        serviceEspiritu.eliminar(charly.getId());
        serviceEspiritu.eliminar(bu.getId());

        serviceMedium.eliminar(mediumCorrupto.getId());
        serviceMedium.eliminar(mediumNormal.getId());

        assertThrows(NoHaySantuarioCorruptoException.class, () -> serviceUbicacion.santuarioCorrupto());

    }

    @Test
    void santuarioMasCorruptoPeroNoHaySantuarios(){

        serviceUbicacion.eliminar(jujuy.getId());
        serviceUbicacion.eliminar(mendoza.getId());
        serviceUbicacion.eliminar(miami.getId());

        assertThrows(NoHaySantuariosException.class, () -> serviceUbicacion.santuarioCorrupto());
    }

    @Test
    void noHaySantuarioCorrupto(){
        poligonoService.eliminar(poligonoJujuy.getId());
        poligonoService.eliminar(poligonoMendoza.getId());
        poligonoService.eliminar(poligonoMiami.getId());


        serviceUbicacion.eliminar(jujuy.getId());
        serviceUbicacion.eliminar(mendoza.getId());
        serviceUbicacion.eliminar(miami.getId());


        Poligono poligono = new Poligono((List.of(
                new Point(-30, -30),
                new Point(-30, -20),
                new Point(-20, -20),
                new Point(-30, -30)
        )));
        poligonoService.crear(poligono);

        Ubicacion santuarioSinDemonios = new Santuario("Santuario Don Satur", 21);
        serviceUbicacion.crear(santuarioSinDemonios,poligono);
        jorge.setUbicacion(santuarioSinDemonios);
        serviceEspiritu.actualizar(jorge);


        assertThrows(NoHaySantuarioCorruptoException.class, () -> serviceUbicacion.santuarioCorrupto());

        poligonoService.eliminar(poligono.getId());
        serviceUbicacion.eliminar(santuarioSinDemonios.getId());
    }


    @Test
    void santuarioCorruptoSinMediums(){
        serviceMedium.eliminar(mediumCorrupto.getId());
        serviceMedium.eliminar(mediumNormal.getId());

        ReporteSantuarioMasCorrupto reporte = serviceUbicacion.santuarioCorrupto();

        assertEquals("JUJUY", reporte.getNombreDelSantuario());
        assertNull(reporte.getMediumConMasDemonios());
        assertEquals(4, reporte.getCantTotalDemonios());
        assertEquals(4, reporte.getCantDemoniosLibres());
    }

    @Test
    void santuarioMasCorruptoConLaMismaDiferenciaSeEligeAlPrimeroPorOrdenAlfabetico(){
        serviceEspiritu.eliminar(lallorona.getId());
        serviceEspiritu.eliminar(pombero.getId());

        ReporteSantuarioMasCorrupto reporte = serviceUbicacion.santuarioCorrupto();

        assertEquals("JUJUY", reporte.getNombreDelSantuario());
        assertEquals(mediumCorrupto, reporte.getMediumConMasDemonios());
        assertEquals(2, reporte.getCantTotalDemonios());
        assertEquals(2, reporte.getCantDemoniosLibres());
    }



    @AfterEach
    void cleanUp(){
        serviceUbicacion.eliminar(jujuy.getId());
        serviceUbicacion.eliminar(mendoza.getId());
        serviceUbicacion.eliminar(miami.getId());

        poligonoService.eliminar(poligonoJujuy.getId());
        poligonoService.eliminar(poligonoMendoza.getId());
        poligonoService.eliminar(poligonoMiami.getId());

        serviceEspiritu.eliminar(jorge.getId());
        serviceEspiritu.eliminar(pombero.getId());
        serviceEspiritu.eliminar(lallorona.getId());
        serviceEspiritu.eliminar(lucifer.getId());
        serviceEspiritu.eliminar(fidel.getId());
        serviceEspiritu.eliminar(charly.getId());
        serviceEspiritu.eliminar(bu.getId());

        serviceMedium.eliminar(mediumCorrupto.getId());
        serviceMedium.eliminar(mediumNormal.getId());

        poligonoService.eliminar(poligonoJujuy.getId());
        poligonoService.eliminar(poligonoMendoza.getId());
        poligonoService.eliminar(poligonoMiami.getId());

    }


}
