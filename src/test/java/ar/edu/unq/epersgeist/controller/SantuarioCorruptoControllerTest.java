package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCMediumController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCUbicacionController;
import ar.edu.unq.epersgeist.controller.utils.TipoEspiritu;
import ar.edu.unq.epersgeist.controller.utils.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.Poligono;
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
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = EpersgeistApplication.class)
public class SantuarioCorruptoControllerTest {
    @Autowired
    private EspirituService serviceEspiritu;

    @Autowired
    private UbicacionService serviceUbicacion;

    @Autowired
    private MediumService serviceMedium;

    @Autowired
    private PoligonoService poligonoService;


    @Autowired
    private MockMVCEspirituController mockMVCEspirituController;
    @Autowired
    private MockMVCUbicacionController mockMVCUbicacionController;
    @Autowired
    private MockMVCMediumController mockMVCMediumController;


    //Creates
    private CreateUbicacionDTO jujuyCreate;
    private CreateUbicacionDTO mendozaCreate;
    private CreateUbicacionDTO miamiCreate;

    private CreateMediumDTO mediumCorruptoCreate;
    private CreateMediumDTO mediumNormalCreate;

    private  CreateEspirituDTO pomberoCreate;
    private  CreateEspirituDTO lalloronaCreate;
    private  CreateEspirituDTO buCreate;
    private  CreateEspirituDTO luciferCreate;
    private  CreateEspirituDTO fidelCreate;
    private  CreateEspirituDTO charlyCreate;
    private CreateEspirituDTO jorgeCreate;


    //DTOs
    private UbicacionDTO jujuy;
    private UbicacionDTO mendoza;
    private UbicacionDTO miami;

    private MediumDTO mediumCorrupto;
    private MediumDTO mediumNormal;

    private EspirituDTO pombero;
    private EspirituDTO lallorona;
    private EspirituDTO bu;
    private EspirituDTO lucifer;
    private EspirituDTO fidel;
    private EspirituDTO charly;
    private EspirituDTO jorge;

    private Poligono jujuyPoligono;
    private Poligono mendozaPoligono;
    private Poligono miamiPoligono;




    @BeforeEach
    void setUp() throws Throwable {
        jujuyPoligono = new Poligono((List.of(
                new Point(0, 0),
                new Point(3, 6),
                new Point(6, 1),
                new Point(0, 0)
        )));

        mendozaPoligono = new Poligono((List.of(
                new Point(10, 10),
                new Point(13, 16),
                new Point(16, 11),
                new Point(10, 10)
        )));

        miamiPoligono = new Poligono((List.of(
                new Point(20, 20),
                new Point(20.3, 20.6),
                new Point(20.6, 20.1),
                new Point(20, 20)
        )));

        poligonoService.crear(jujuyPoligono);
        poligonoService.crear(mendozaPoligono);
        poligonoService.crear(miamiPoligono);

        //SANTUARIOS
        jujuyCreate   = new CreateUbicacionDTO("JUJUY", 20, TipoUbicacion.Santuario, jujuyPoligono.getId());
        mendozaCreate = new CreateUbicacionDTO("MENDOZA", 99, TipoUbicacion.Santuario, mendozaPoligono.getId());

        miamiCreate   = new CreateUbicacionDTO("Miami", 99, TipoUbicacion.Cementerio, miamiPoligono.getId());

        //MEDIUMS
        mediumCorruptoCreate = new CreateMediumDTO("Corrupto", 200, 100, jujuyCreate.nombre());
        mediumNormalCreate   = new CreateMediumDTO("Normal", 200, 100, mendozaCreate.nombre());

        //DEMONIOS
        //En "JUJUY"
        pomberoCreate   = new CreateEspirituDTO(100, 100, "Pombero",  jujuyCreate.nombre(), TipoEspiritu.EspirituDemoniaco);
        lalloronaCreate = new CreateEspirituDTO(100, 100,"LaLlorona", jujuyCreate.nombre(), TipoEspiritu.EspirituDemoniaco );
        buCreate        = new CreateEspirituDTO(100, 100,"Bu", jujuyCreate.nombre(), TipoEspiritu.EspirituDemoniaco);
        luciferCreate   = new CreateEspirituDTO(100, 100,"Lucifer", jujuyCreate.nombre(), TipoEspiritu.EspirituDemoniaco);
        //En "MENDOZA"
        fidelCreate  = new CreateEspirituDTO(100, 100,"Fidel", mendozaCreate.nombre(), TipoEspiritu.EspirituDemoniaco);
        charlyCreate = new CreateEspirituDTO(100, 100,"Charly", miamiCreate.nombre(), TipoEspiritu.EspirituDemoniaco);

        //ANGELES
        jorgeCreate = new CreateEspirituDTO(100, 100,"Jorge",  jujuyCreate.nombre(), TipoEspiritu.EspirituAngelical);

        //EN Jujuy hay 4 demonios (pombero,lallorona,bu, lucifer) y un angel (jorge)
        // En Mendoza hay 1 demonio (fidel) y 0 angeles
        //Por ende el que tiene mayor diferencia es Jujuy porque 3 ( 4 - 1 ) es mayor a 1 (1 - 0)

        //SE llaman a los endpoints para PERSISTIRLOS:
        mendoza   = mockMVCUbicacionController.crear(mendozaCreate, HttpStatus.OK, UbicacionDTO.class);
        jujuy     = mockMVCUbicacionController.crear(jujuyCreate, HttpStatus.OK, UbicacionDTO.class);
        miami     = mockMVCUbicacionController.crear(miamiCreate, HttpStatus.OK, UbicacionDTO.class);

        pombero   = mockMVCEspirituController.crear(pomberoCreate, HttpStatus.OK, EspirituDTO.class);
        lallorona = mockMVCEspirituController.crear(lalloronaCreate, HttpStatus.OK, EspirituDTO.class);
        bu        = mockMVCEspirituController.crear(buCreate,HttpStatus.OK, EspirituDTO.class);
        lucifer   = mockMVCEspirituController.crear(luciferCreate,HttpStatus.OK, EspirituDTO.class);
        fidel     = mockMVCEspirituController.crear(fidelCreate ,HttpStatus.OK, EspirituDTO.class);
        charly    = mockMVCEspirituController.crear(charlyCreate, HttpStatus.OK, EspirituDTO.class);
        jorge     = mockMVCEspirituController.crear(jorgeCreate, HttpStatus.OK, EspirituDTO.class);

        mediumCorrupto = mockMVCMediumController.crear(mediumCorruptoCreate,HttpStatus.OK, MediumDTO.class);
        mediumNormal   = mockMVCMediumController.crear(mediumNormalCreate,HttpStatus.OK, MediumDTO.class);
    }


    @Test
    void devuelveElSantuarioMasCorrupto() throws Throwable {
        //En Jujuy:
        //  Se conectan los demonios pombero y lucifer con mediumCorrupto por ende dejan de ser libres
        mockMVCEspirituController.conectar(pombero.id(), mediumCorrupto.id(), HttpStatus.OK, MediumDTO.class);
        mockMVCEspirituController.conectar(lucifer.id(), mediumCorrupto.id(), HttpStatus.OK, MediumDTO.class);
        // y el angel jorge tambien
        mockMVCEspirituController.conectar(jorge.id(), mediumCorrupto.id(), HttpStatus.OK, MediumDTO.class);

        //En Mendoza
        // fidel deja de ser libre ya que se conecta con mediumNormal
        mockMVCEspirituController.conectar(fidel.id(), mediumNormal.id(), HttpStatus.OK, MediumDTO.class);

        //Se pide el reporte
        ReporteSantuarioCorruptoDTO reporte  = mockMVCUbicacionController.santuarioCorrupto(HttpStatus.OK, ReporteSantuarioCorruptoDTO.class);

        assertEquals("JUJUY", reporte.nombreDelSantuario());
        assertEquals(mediumCorrupto.nombre(), reporte.mediumConMasDemonios().nombre());
        assertEquals(4, reporte.cantTotalDemonios());
        assertEquals(2, reporte.cantDemoniosLibres());
    }

    @Test
    public void santuarioMasCorruptoPeroNoHaySantuarios() throws  Throwable{
        serviceMedium.clearAll();
        serviceEspiritu.clearAll();
        serviceUbicacion.clearAll();
        ErrorDTO dtoRecibido = mockMVCUbicacionController.santuarioCorrupto(HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("No hay Santuarios en la base",404);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void santuarioMasCorruptoPeroNoHaySantuarioCorrupto () throws Throwable{
        serviceEspiritu.clearAll();
        serviceMedium.clearAll();

        ErrorDTO dtoRecibido = mockMVCUbicacionController.santuarioCorrupto(HttpStatus.OK, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("No existe ningun Santuario Corrupto.",200);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }


    @AfterEach
    public void cleanUp() {
        serviceMedium.clearAll();
        serviceEspiritu.clearAll();
        poligonoService.eliminar(jujuyPoligono.getId());
        poligonoService.eliminar(mendozaPoligono.getId());
        poligonoService.eliminar(miamiPoligono.getId());
        serviceUbicacion.clearAll();
    }


}
