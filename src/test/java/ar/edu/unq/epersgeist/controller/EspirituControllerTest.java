package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCMediumController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCUbicacionController;
import ar.edu.unq.epersgeist.controller.utils.TipoEspiritu;
import ar.edu.unq.epersgeist.controller.utils.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.PoligonoService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.data.geo.Point;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EpersgeistApplication.class)
public class EspirituControllerTest {

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
        pepe = new CreateEspirituDTO(10,10,"Pepe",quilmes.nombre(),TipoEspiritu.EspirituAngelical);
        mago = new CreateMediumDTO("Mago", 200, 20, quilmes.nombre());

        ubicacionDTO = mockMVCUbicacionController.crear(quilmes, HttpStatus.OK, UbicacionDTO.class);
        espirituDTO = mockMVCEspirituController.crear(pepe, HttpStatus.OK, EspirituDTO.class);
    }

    @Test
    public void recuperarEspiritu() throws  Throwable{
        EspirituDTO espirituRecuperado = mockMVCEspirituController.recuperar(espirituDTO.id(), HttpStatus.OK, EspirituDTO.class);

        assertEquals(espirituDTO, espirituRecuperado);
    }

    @Test
    public void recuperarEspirituNoPersistido() throws  Throwable{
        ErrorDTO dtoRecibido = mockMVCEspirituController.recuperar(129L, HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("No se encontro al espiritu con el id: " + 129L,404);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void recuperarTodos() throws Throwable {
        CreateEspirituDTO pepeMalo = new CreateEspirituDTO(10,10,"pepeMalo",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);
        mockMVCEspirituController.crear(pepeMalo, HttpStatus.OK, EspirituDTO.class);
        var espiritus = mockMVCEspirituController.recuperarTodos();

        assertEquals(2, espiritus.size());
    }

    @Test
    public void recuperarTodosPeroNoHayEspiritu() throws Throwable {
        serviceMedium.clearAll();
        serviceEspiritu.clearAll();
        serviceUbicacion.clearAll();
        var espiritus = mockMVCEspirituController.recuperarTodos();

        assertEquals(0, espiritus.size());
    }

    @Test
    public void eliminarEspiritu() throws Throwable {
        assertEquals( "Tu espiritu ha sido eliminado correctamente.",mockMVCEspirituController.eliminar(espirituDTO.id()));
    }

    @Test
    public void eliminarEspirituNoPersistido() throws Throwable {
        assertEquals( "Tu espiritu ha sido eliminado correctamente.",mockMVCEspirituController.eliminar(129L));
    }

    @Test
    public void crearInvalidoPorEnergiaSuperiorA100() throws Throwable {
        CreateEspirituDTO espirituInvalido = new CreateEspirituDTO(1000,10,"pepeMalo",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);

        ErrorDTO dtoRecibido = mockMVCEspirituController.crear(espirituInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("La energia debe ser positiva y menor a 100",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }
    @Test
    public void crearInvalidoPorEnergiaNegativa() throws Throwable {
        CreateEspirituDTO espirituInvalido = new CreateEspirituDTO(-1000,10,"pepeMalo",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);

        ErrorDTO dtoRecibido = mockMVCEspirituController.crear(espirituInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("La energia debe ser positiva y menor a 100",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorNivelDeConexionNegativa() throws Throwable {
        CreateEspirituDTO espirituInvalido = new CreateEspirituDTO(10,-100,"pepeMalo",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);

        ErrorDTO dtoRecibido = mockMVCEspirituController.crear(espirituInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El nivel de conexion no puede ser nulo ni negativo.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }
    @Test
    public void crearInvalidoPorNivelDeConexionNula() throws Throwable {
        CreateEspirituDTO espirituInvalido = new CreateEspirituDTO(10,null,"pepeMalo",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);

        ErrorDTO dtoRecibido = mockMVCEspirituController.crear(espirituInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El nivel de conexion no puede ser nulo ni negativo.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorNombre() throws Throwable {
        CreateEspirituDTO espirituInvalido = new CreateEspirituDTO(10,100,"",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);

        ErrorDTO dtoRecibido = mockMVCEspirituController.crear(espirituInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El nombre no puede estar vacio.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorNombreUbicacionVacio() throws Throwable {
        CreateEspirituDTO espirituInvalido = new CreateEspirituDTO(10,100,"pepeMalo", "",TipoEspiritu.EspirituDemoniaco);

        ErrorDTO dtoRecibido = mockMVCEspirituController.crear(espirituInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El nombre no puede estar vacio o nulo.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorNombreUbicacionNull() throws Throwable {
        CreateEspirituDTO espirituInvalido = new CreateEspirituDTO(10,100,"pepeMalo", null,TipoEspiritu.EspirituDemoniaco);

        ErrorDTO dtoRecibido = mockMVCEspirituController.crear(espirituInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El nombre no puede estar vacio o nulo.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorTipo() throws Throwable {
        CreateEspirituDTO espirituInvalido = new CreateEspirituDTO(10,100,"pepeMalo", "asd", null);

        ErrorDTO dtoRecibido = mockMVCEspirituController.crear(espirituInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El tipo de espiritu es invalido.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void conectar() throws Throwable {
        MediumDTO mediumDTO = mockMVCMediumController.crear(mago, HttpStatus.OK, MediumDTO.class);
        MediumDTO mediumRecuperado = mockMVCEspirituController.conectar(espirituDTO.id(), mediumDTO.id(), HttpStatus.OK, MediumDTO.class);

        assertEquals(1, mediumRecuperado.espiritus().size());
    }

    @Test
    public void conectarConIdMediumInexistente() throws Throwable {
        ErrorDTO dtoRecibido = mockMVCEspirituController.conectar(espirituDTO.id(), 239L, HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El espiritu o medium no se han encontrado",404);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void conectarConIdEspirituInexistente() throws Throwable {
        MediumDTO mediumDTO = mockMVCMediumController.crear(mago, HttpStatus.OK, MediumDTO.class);
        ErrorDTO dtoRecibido = mockMVCEspirituController.conectar(239L, mediumDTO.id() , HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El espiritu o medium no se han encontrado",404);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void conectarConEspirituYMediumEnDistintaUbicacion() throws Throwable {
        Poligono bernalPoligono = new Poligono((List.of(
                new Point(10, 10),
                new Point(13, 16),
                new Point(16, 11),
                new Point(10, 10)
        )));
        poligonoService.crear(bernalPoligono);
        bernalPoligono = poligonoService.recuperar(bernalPoligono.getId());

        CreateUbicacionDTO bernal = new CreateUbicacionDTO("Bernal",10, TipoUbicacion.Santuario, bernalPoligono.getId());
        UbicacionDTO ubicacion = mockMVCUbicacionController.crear(bernal, HttpStatus.OK, UbicacionDTO.class);

        mago = new CreateMediumDTO("Mago", 200, 20, ubicacion.nombre());
        MediumDTO mediumDTO = mockMVCMediumController.crear(mago, HttpStatus.OK, MediumDTO.class);

        ErrorDTO dtoRecibido = mockMVCEspirituController.conectar(espirituDTO.id(), mediumDTO.id(), HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El espiritu no pudo establecer conexion con el medium porque no es libre o no comparten ubicacion",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
        poligonoService.eliminar(bernalPoligono.getId());
    }

    @Test
    public void conectarConEspirituNoLibre() throws Throwable {
        MediumDTO mediumDTO = mockMVCMediumController.crear(mago, HttpStatus.OK, MediumDTO.class);
        mockMVCEspirituController.conectar(espirituDTO.id(), mediumDTO.id(), HttpStatus.OK, MediumDTO.class);

        CreateMediumDTO perro = new CreateMediumDTO("Perro", 200, 20, quilmes.nombre());
        MediumDTO otroMediumDTO = mockMVCMediumController.crear(perro, HttpStatus.OK, MediumDTO.class);

        ErrorDTO dtoRecibido = mockMVCEspirituController.conectar(espirituDTO.id(), otroMediumDTO.id(), HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El espiritu no pudo establecer conexion con el medium porque no es libre o no comparten ubicacion",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void espiritusDemoniacos() throws Throwable {
        CreateEspirituDTO pepeMalo = new CreateEspirituDTO(10,10,"pepeMalo",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);
        CreateEspirituDTO pepeLoco = new CreateEspirituDTO(10,10,"pepeLoco",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);

        mockMVCEspirituController.crear(pepeMalo, HttpStatus.OK, EspirituDTO.class);
        mockMVCEspirituController.crear(pepeLoco, HttpStatus.OK, EspirituDTO.class);

        assertEquals(200, mockMVCEspirituController.espiritusDemoniacos(Direccion.ASCENDENTE, 1, 3, HttpStatus.OK));
    }

    @Test
    public void espiritusDemoniacosNoFuncionaPorPaginaNegativa() throws Throwable {
        CreateEspirituDTO pepeMalo = new CreateEspirituDTO(10,10,"pepeMalo",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);
        CreateEspirituDTO pepeLoco = new CreateEspirituDTO(10,10,"pepeLoco",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);

        mockMVCEspirituController.crear(pepeMalo, HttpStatus.OK, EspirituDTO.class);
        mockMVCEspirituController.crear(pepeLoco, HttpStatus.OK, EspirituDTO.class);

        assertEquals(400, mockMVCEspirituController.espiritusDemoniacos(Direccion.ASCENDENTE, -1, 3, HttpStatus.BAD_REQUEST));
    }

    @Test
    public void espiritusDemoniacosNoFuncionaPorCantidadNegativa() throws Throwable {
        CreateEspirituDTO pepeMalo = new CreateEspirituDTO(10,10,"pepeMalo",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);
        CreateEspirituDTO pepeLoco = new CreateEspirituDTO(10,10,"pepeLoco",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);
        mockMVCEspirituController.crear(pepeMalo, HttpStatus.OK, EspirituDTO.class);
        mockMVCEspirituController.crear(pepeLoco, HttpStatus.OK, EspirituDTO.class);

        assertEquals(400, mockMVCEspirituController.espiritusDemoniacos(Direccion.ASCENDENTE, 1, -3, HttpStatus.BAD_REQUEST));
    }

    @Test
    public void espiritusDemoniacosNoFuncionaPorDireccionNula() throws Throwable {
        CreateEspirituDTO pepeMalo = new CreateEspirituDTO(10,10,"pepeMalo",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);
        CreateEspirituDTO pepeLoco = new CreateEspirituDTO(10,10,"pepeLoco",quilmes.nombre(),TipoEspiritu.EspirituDemoniaco);
        mockMVCEspirituController.crear(pepeMalo, HttpStatus.OK, EspirituDTO.class);
        mockMVCEspirituController.crear(pepeLoco, HttpStatus.OK, EspirituDTO.class);

        assertEquals(400, mockMVCEspirituController.espiritusDemoniacos(null, 1, 3, HttpStatus.BAD_REQUEST));
    }

    @AfterEach
    public void cleanUp() {
        serviceMedium.clearAll();
        serviceEspiritu.clearAll();
        poligonoService.eliminar(quilmesPoligono.getId());
        serviceUbicacion.clearAll();
    }

}
