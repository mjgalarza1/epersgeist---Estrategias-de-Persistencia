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

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = EpersgeistApplication.class)

public class UbicacionControllerTest {
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

    private CreateUbicacionDTO quilmes;
    private CreateEspirituDTO pepe;
    private CreateMediumDTO mago;

    private Poligono quilmesPoligono;

    private UbicacionDTO ubicacionDTO;
    private EspirituDTO espirituDTO;

    @BeforeEach
    void setUp() throws Throwable {
        quilmesPoligono = new Poligono((List.of(
                new Point(0, 0),
                new Point(3, 6),
                new Point(6, 1),
                new Point(0, 0)
        )));

        poligonoService.crear(quilmesPoligono);
        quilmesPoligono = poligonoService.recuperar(quilmesPoligono.getId());

        quilmes = new CreateUbicacionDTO("Quilmes",10, TipoUbicacion.Santuario, quilmesPoligono.getId());
        pepe = new CreateEspirituDTO(10,10,"Pepe",quilmes.nombre(), TipoEspiritu.EspirituAngelical);
        mago = new CreateMediumDTO("Mago", 200, 20, quilmes.nombre());

        ubicacionDTO = mockMVCUbicacionController.crear(quilmes, HttpStatus.OK, UbicacionDTO.class);
        espirituDTO = mockMVCEspirituController.crear(pepe, HttpStatus.OK, EspirituDTO.class);
    }

    @Test
    public void recuperarUbicacion() throws  Throwable{
        UbicacionDTO ubicacionRecuperada = mockMVCUbicacionController.recuperar(ubicacionDTO.id(), HttpStatus.OK, UbicacionDTO.class);

        assertEquals(ubicacionDTO, ubicacionRecuperada);
    }

    @Test
    public void recuperarEspirituNoPersistido() throws  Throwable{
        ErrorDTO dtoRecibido = mockMVCUbicacionController.recuperar(129L, HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("La ubicacion no se ha encontrado",404);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void recuperarTodos() throws Throwable {
        Poligono pastoPoligono = new Poligono((List.of(
                new Point(10, 10),
                new Point(13, 16),
                new Point(16, 11),
                new Point(10, 10)
        )));

        poligonoService.crear(pastoPoligono);
        pastoPoligono = poligonoService.recuperar(pastoPoligono.getId());

        CreateUbicacionDTO santuario = new CreateUbicacionDTO("Pasto",10,TipoUbicacion.Santuario, pastoPoligono.getId());
        mockMVCUbicacionController.crear(santuario, HttpStatus.OK, UbicacionDTO.class);
        var ubicaciones = mockMVCUbicacionController.recuperarTodos();

        assertEquals(2, ubicaciones.size());
        poligonoService.eliminar(pastoPoligono.getId());
    }

    @Test
    public void recuperarTodosPeroNoHayEspiritu() throws Throwable {
        serviceMedium.clearAll();
        serviceEspiritu.clearAll();
        serviceUbicacion.clearAll();
        var ubicaciones = mockMVCUbicacionController.recuperarTodos();

        assertEquals(0, ubicaciones.size());
    }

    @Test
    public void eliminarUbicacion() throws Throwable {
        assertEquals( "Tu Ubicacion ha sido eliminada correctamente.",mockMVCUbicacionController.eliminar(ubicacionDTO.id()));
    }

    @Test
    public void eliminarEspirituNoPersistido() throws Throwable {
        assertEquals( "Tu Ubicacion ha sido eliminada correctamente.",mockMVCUbicacionController.eliminar(129L));
    }

    @Test
    public void crearInvalidoPorNombre() throws Throwable {
        Poligono unPoligono = new Poligono((List.of(
                new Point(10, 10),
                new Point(13, 16),
                new Point(16, 11),
                new Point(10, 10)
        )));

        poligonoService.crear(unPoligono);
        unPoligono = poligonoService.recuperar(unPoligono.getId());

        CreateUbicacionDTO ubicacionInvalida = new CreateUbicacionDTO("",10,TipoUbicacion.Santuario, unPoligono.getId());

        ErrorDTO dtoRecibido = mockMVCUbicacionController.crear(ubicacionInvalida, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El nombre no puede estar vacio o nulo.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
        poligonoService.eliminar(unPoligono.getId());
    }

    @Test
    public void crearInvalidoPorNombreNulo() throws Throwable {
        Poligono unPoligono = new Poligono((List.of(
                new Point(10, 10),
                new Point(13, 16),
                new Point(16, 11),
                new Point(10, 10)
        )));

        poligonoService.crear(unPoligono);
        unPoligono = poligonoService.recuperar(unPoligono.getId());

        CreateUbicacionDTO ubicacionInvalida = new CreateUbicacionDTO(null,10,TipoUbicacion.Santuario, unPoligono.getId());

        ErrorDTO dtoRecibido = mockMVCUbicacionController.crear(ubicacionInvalida, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El nombre no puede estar vacio o nulo.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
        poligonoService.eliminar(unPoligono.getId());
    }

    @Test
    public void crearInvalidoPorEnergiaNegativa() throws Throwable {
        Poligono pastoPoligono = new Poligono((List.of(
                new Point(10, 10),
                new Point(13, 16),
                new Point(16, 11),
                new Point(10, 10)
        )));

        poligonoService.crear(pastoPoligono);
        pastoPoligono = poligonoService.recuperar(pastoPoligono.getId());

        CreateUbicacionDTO ubicacionInvalida = new CreateUbicacionDTO("Pasto",-10,TipoUbicacion.Santuario, pastoPoligono.getId());

        ErrorDTO dtoRecibido = mockMVCUbicacionController.crear(ubicacionInvalida, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("La energia debe ser positiva y menor a 100",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
        poligonoService.eliminar(pastoPoligono.getId());
    }

    @Test
    public void crearInvalidoPorEnergiaMayorA100() throws Throwable {
        Poligono pastoPoligono = new Poligono((List.of(
                new Point(10, 10),
                new Point(13, 16),
                new Point(16, 11),
                new Point(10, 10)
        )));

        poligonoService.crear(pastoPoligono);
        pastoPoligono = poligonoService.recuperar(pastoPoligono.getId());

        CreateUbicacionDTO ubicacionInvalida = new CreateUbicacionDTO("Pasto",1000,TipoUbicacion.Santuario, pastoPoligono.getId());

        ErrorDTO dtoRecibido = mockMVCUbicacionController.crear(ubicacionInvalida, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("La energia debe ser positiva y menor a 100",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
        poligonoService.eliminar(pastoPoligono.getId());
    }

    @Test
    public void crearInvalidoPorTipoNulo() throws Throwable {
        Poligono pastoPoligono = new Poligono((List.of(
                new Point(10, 10),
                new Point(13, 16),
                new Point(16, 11),
                new Point(10, 10)
        )));

        poligonoService.crear(pastoPoligono);
        pastoPoligono = poligonoService.recuperar(pastoPoligono.getId());

        CreateUbicacionDTO ubicacionInvalida = new CreateUbicacionDTO("Pasto",10,null, pastoPoligono.getId());

        ErrorDTO dtoRecibido = mockMVCUbicacionController.crear(ubicacionInvalida, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El tipo de ubicacion es invalido.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
        poligonoService.eliminar(pastoPoligono.getId());
    }

    @Test
    public void crearInvalidoPorPoligonoVacio() throws Throwable {
        CreateUbicacionDTO ubicacionInvalida = new CreateUbicacionDTO("Pasto",10,TipoUbicacion.Santuario, null);

        ErrorDTO dtoRecibido = mockMVCUbicacionController.crear(ubicacionInvalida, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El id no puede ser nulo.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void espiritusEnUbicacion() throws Throwable {
        CreateEspirituDTO pepa = new CreateEspirituDTO(10,10,"Pepa",quilmes.nombre(), TipoEspiritu.EspirituAngelical);
        mockMVCEspirituController.crear(pepa, HttpStatus.OK, EspirituDTO.class);

        Collection<EspirituDTO> espiritus = mockMVCUbicacionController.espiritusEn(ubicacionDTO.id(), HttpStatus.OK);
        assertEquals(2, espiritus.size());
    }

    @Test
    public void espiritusEnUbicacionConIdInvalida() throws Throwable {
        Collection<EspirituDTO> espiritus = mockMVCUbicacionController.espiritusEn(123L, HttpStatus.OK);
        assertEquals(0, espiritus.size());
    }

    @Test
    public void mediumsSinEspiritusEnUbicacion() throws Throwable {
        mockMVCMediumController.crear(mago, HttpStatus.OK, MediumDTO.class);
        Collection<MediumDTO> mediums = mockMVCUbicacionController.mediumSinEspiritusEn(ubicacionDTO.id(), HttpStatus.OK);
        assertEquals(1, mediums.size());
    }

    @Test
    public void mediumsSinEspiritusEnUbicacionInvalida() throws Throwable {
        Collection<MediumDTO> mediums = mockMVCUbicacionController.mediumSinEspiritusEn(123L, HttpStatus.OK);
        assertEquals(0, mediums.size());
    }

    @AfterEach
    public void cleanUp() {
        serviceMedium.clearAll();
        serviceEspiritu.clearAll();
        poligonoService.eliminar(quilmesPoligono.getId());
        serviceUbicacion.clearAll();
    }
}
