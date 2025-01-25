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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EpersgeistApplication.class)
public class MediumControllerTest {

    @Autowired
    private EspirituService serviceEspiritu;
    @Autowired
    private UbicacionService serviceUbicacion;
    @Autowired
    private MediumService serviceMedium;
    @Autowired
    private PoligonoService poligonoService;

    @Autowired
    private MockMVCMediumController mockMVCMediumController;
    @Autowired
    private MockMVCEspirituController mockMVCEspirituController;
    @Autowired
    private MockMVCUbicacionController mockMVCUbicacionController;

    private Poligono oestePoligono;
    private Poligono estePoligono;

    private CreateUbicacionDTO santuario;
    private CreateUbicacionDTO otroSantuario;
    private CreateUbicacionDTO cementerio;

    private CreateMediumDTO unMedium;

    private CreateEspirituDTO espAngel1;
    private CreateEspirituDTO espAngel2;
    private CreateEspirituDTO espAngel3;

    private CreateEspirituDTO espDemon1;
    private CreateEspirituDTO espDemon2;

    private UbicacionDTO santuarioDTO;
    private UbicacionDTO otroSantuarioDTO;
    private MediumDTO mediumDTO;
    private EspirituDTO angelDTO1;
    private EspirituDTO angelDTO2;
    private EspirituDTO angelDTO3;
    private EspirituDTO demonioDTO1;
    private EspirituDTO demonioDTO2;

    @BeforeEach
    void setUp() throws Throwable {
        oestePoligono = new Poligono((List.of(
                new Point(0, 0),
                new Point(3, 6),
                new Point(6, 1),
                new Point(0, 0)
        )));


        estePoligono = new Poligono((List.of(
                new Point(10, 10),
                new Point(13, 16),
                new Point(16, 11),
                new Point(10, 10)
        )));

        poligonoService.crear(oestePoligono);
        poligonoService.crear(estePoligono);

        santuario = new CreateUbicacionDTO("Santuario del Oeste", 10, TipoUbicacion.Santuario, oestePoligono.getId());
        otroSantuario = new CreateUbicacionDTO("Santuario del Este", 10, TipoUbicacion.Santuario, estePoligono.getId());

        unMedium = new CreateMediumDTO("Trevor", 200, 200, santuario.nombre());
        otroSantuarioDTO = mockMVCUbicacionController.crear(otroSantuario, HttpStatus.OK, UbicacionDTO.class);
        espAngel1 = new CreateEspirituDTO(100,100,"Angel 1", santuario.nombre(), TipoEspiritu.EspirituAngelical);
        espAngel2 = new CreateEspirituDTO(100,100,"Angel 2", santuario.nombre(), TipoEspiritu.EspirituAngelical);

        espAngel3 = new CreateEspirituDTO(10,10,"Angel 3", otroSantuario.nombre(), TipoEspiritu.EspirituAngelical);

        espDemon1 = new CreateEspirituDTO(1,1,"Demonio 1", santuario.nombre(), TipoEspiritu.EspirituDemoniaco);
        espDemon2 = new CreateEspirituDTO(10,10,"Demonio 2", santuario.nombre(), TipoEspiritu.EspirituDemoniaco);

        santuarioDTO = mockMVCUbicacionController.crear(santuario, HttpStatus.OK, UbicacionDTO.class);

        mediumDTO = mockMVCMediumController.crear(unMedium, HttpStatus.OK, MediumDTO.class);

        angelDTO1 = mockMVCEspirituController.crear(espAngel1, HttpStatus.OK, EspirituDTO.class);
        angelDTO2 = mockMVCEspirituController.crear(espAngel2, HttpStatus.OK, EspirituDTO.class);
        angelDTO3 = mockMVCEspirituController.crear(espAngel3, HttpStatus.OK, EspirituDTO.class);

        demonioDTO1 = mockMVCEspirituController.crear(espDemon1, HttpStatus.OK, EspirituDTO.class);
        demonioDTO2 = mockMVCEspirituController.crear(espDemon2, HttpStatus.OK, EspirituDTO.class);
    }

    @Test
    public void recuperarTodos() throws Throwable {
        CreateMediumDTO otroMedium = new CreateMediumDTO("Bill", 50, 50, santuario.nombre());
        mockMVCMediumController.crear(otroMedium, HttpStatus.OK, MediumDTO.class);
        var mediums = mockMVCMediumController.recuperarTodos();

        assertEquals(2, mediums.size());
    }

    @Test
    public void recuperarTodosPeroNoHayMedium() throws Throwable {
        serviceMedium.clearAll();
        serviceEspiritu.clearAll();
        serviceUbicacion.clearAll();
        var mediums = mockMVCMediumController.recuperarTodos();

        assertEquals(0, mediums.size());
    }

    @Test
    public void eliminarMedium() throws Throwable {
        assertEquals("Tu Medium ha sido eliminado correctamente.", mockMVCMediumController.eliminar(mediumDTO.id()));
    }

    @Test
    public void eliminarMediumNoPersistido() throws Throwable {
        assertEquals("Tu Medium ha sido eliminado correctamente.", mockMVCMediumController.eliminar(129L));
    }

    @Test
    public void recuperarMedium() throws Throwable {
        MediumDTO mediumRecuperado = mockMVCMediumController.recuperar(mediumDTO.id(), HttpStatus.OK, MediumDTO.class);
        assertEquals(mediumDTO, mediumRecuperado);
    }

    @Test
    public void recuperarMediumNoPersistido() throws Throwable {
        ErrorDTO dtoRecibido = mockMVCMediumController.recuperar(129L, HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El medium no se ha encontrado", 404);
        assertEquals(dtoEsperado.error(), dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorNombreVacio() throws Throwable {
        CreateMediumDTO mediumInvalido = new CreateMediumDTO("", 50, 50, santuario.nombre());

        ErrorDTO dtoRecibido = mockMVCMediumController.crear(mediumInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El nombre no puede estar vacio o nulo.", 400);
        assertEquals(dtoEsperado.error(), dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorManaMaxVacio() throws Throwable {
        CreateMediumDTO mediumInvalido = new CreateMediumDTO("Walter", null, 150, santuario.nombre());

        ErrorDTO dtoRecibido = mockMVCMediumController.crear(mediumInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El mana maximo no puede estar vacio o ser menor a 0.", 400);
        assertEquals(dtoEsperado.error(), dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorManaMaxNegativo() throws Throwable {
        CreateMediumDTO mediumInvalido = new CreateMediumDTO("Walter", -50, 150, santuario.nombre());

        ErrorDTO dtoRecibido = mockMVCMediumController.crear(mediumInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El mana maximo no puede estar vacio o ser menor a 0.", 400);
        assertEquals(dtoEsperado.error(), dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorManaSuperiorAManaMax() throws Throwable {
        CreateMediumDTO mediumInvalido = new CreateMediumDTO("Walter", 100, 150, santuario.nombre());

        ErrorDTO dtoRecibido = mockMVCMediumController.crear(mediumInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El mana no puede superar el mana Max.", 400);
        assertEquals(dtoEsperado.error(), dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorManaNegativa() throws Throwable {
        CreateMediumDTO mediumInvalido = new CreateMediumDTO("Walter", 100, -10, santuario.nombre());

        ErrorDTO dtoRecibido = mockMVCMediumController.crear(mediumInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El mana no puede ser nulo o negativo.", 400);
        assertEquals(dtoEsperado.error(), dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorManaVacio() throws Throwable {
        CreateMediumDTO mediumInvalido = new CreateMediumDTO("Walter", 100, null, santuario.nombre());

        ErrorDTO dtoRecibido = mockMVCMediumController.crear(mediumInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El mana no puede ser nulo o negativo.", 400);
        assertEquals(dtoEsperado.error(), dtoRecibido.error());
    }

    @Test
    public void crearInvalidoPorUbicacionVacia() throws Throwable {
        CreateMediumDTO mediumInvalido = new CreateMediumDTO("Walter", 100, 100, "");
        ErrorDTO dtoRecibido = mockMVCMediumController.crear(mediumInvalido, HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El nombre no puede estar vacio o nulo.", 400);
        assertEquals(dtoEsperado.error(), dtoRecibido.error());
    }

    @Test
    public void espiritusDevuelveLosDosEspiritusConectadosAlMedium() throws Throwable {
        mockMVCEspirituController.conectar(angelDTO1.id(), mediumDTO.id(), HttpStatus.OK, MediumDTO.class);
        mockMVCEspirituController.conectar(angelDTO2.id(), mediumDTO.id(), HttpStatus.OK, MediumDTO.class);
        var espiritusDelMedium = mockMVCMediumController.espiritus(mediumDTO.id());

        assertEquals(2, espiritusDelMedium.size());
    }

    @Test
    public void espiritusNoDevuelveNadaPorqueNoHayEspiritusConectadosAlMedium() throws Throwable {
        var espiritusDelMedium = mockMVCMediumController.espiritus(mediumDTO.id());

        assertEquals(0, espiritusDelMedium.size());
    }

    @Test
    public void invocacionValida() throws Throwable {
        angelDTO3 = mockMVCMediumController.invocar(mediumDTO.id(), angelDTO2.id(), HttpStatus.OK, EspirituDTO.class);
        assertEquals(santuarioDTO.id(), angelDTO3.ubicacion().id());
    }

    @Test
    public void invocacionInvalida() throws Throwable {
        ErrorDTO errorARecibir = new ErrorDTO("El espiritu esta a mas de 100KM de distancia", 400);
        ErrorDTO errorRecibido = mockMVCMediumController.invocar(mediumDTO.id(), angelDTO3.id(), HttpStatus.BAD_REQUEST, ErrorDTO.class);
        assertEquals(errorARecibir, errorRecibido);
    }

    @Test
    public void invocacionInvalidaPorMediumNoPersistido() throws Throwable {
        ErrorDTO dtoRecibido = mockMVCMediumController.invocar(128L, angelDTO3.id(), HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El medium no se ha encontrado",404);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void invocacionInvalidaPorEspirituNoPersistido() throws Throwable {
        ErrorDTO dtoRecibido = mockMVCMediumController.invocar(mediumDTO.id(), 128L, HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("No se encontro al espiritu con el id: " + 128L,404);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void invocacionInvalidaPorEspirituNoLibre() throws Throwable {
        mockMVCEspirituController.conectar(angelDTO2.id(), mediumDTO.id(), HttpStatus.OK, MediumDTO.class);

        ErrorDTO dtoRecibido =mockMVCMediumController.invocar(mediumDTO.id(), angelDTO2.id(), HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("el Espiritu no es libre [tiene a Trevor conectado]",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void invocacionNoEsRealizadaPorManaDelMediumInsuficiente() throws Throwable {
        CreateMediumDTO cansadin = new CreateMediumDTO("Cansadin", 200, 1, santuarioDTO.nombre());
        MediumDTO cansadinDTO = mockMVCMediumController.crear(cansadin, HttpStatus.OK, MediumDTO.class);

        Integer manaAntesDelInvocar = cansadinDTO.mana();
        mockMVCMediumController.invocar(cansadinDTO.id(), angelDTO2.id(), HttpStatus.OK, EspirituDTO.class);

        assertEquals(1, manaAntesDelInvocar);
    }

    @Test
    public void mediumFallaAlInvocarAngelLibreEnCementerio() throws Throwable {
        Poligono c_oeste = new Poligono((List.of(
                new Point(-0, -0),
                new Point(-3, -6),
                new Point(-6, -1),
                new Point(-0, -0)
        )));

        poligonoService.crear(c_oeste);
        c_oeste = poligonoService.recuperar(c_oeste.getId());

        CreateUbicacionDTO cementerio = new CreateUbicacionDTO("Cementerio del Oeste", 10, TipoUbicacion.Cementerio, c_oeste.getId());
        CreateMediumDTO mediumEnCementerio = new CreateMediumDTO("Trevor", 200, 200, cementerio.nombre());

        UbicacionDTO cementerioDTO = mockMVCUbicacionController.crear(cementerio, HttpStatus.OK, UbicacionDTO.class);
        MediumDTO mediumEnCementerioDTO = mockMVCMediumController.crear(mediumEnCementerio, HttpStatus.OK, MediumDTO.class);

        ErrorDTO dtoRecibido = mockMVCMediumController.invocar(mediumEnCementerioDTO.id(), angelDTO1.id(), HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("No se puede invocar al EspirituAngelical en el Cementerio.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
        poligonoService.eliminar(c_oeste.getId());
    }

    @Test
    public void mediumFallaAlInvocarDemonioLibreEnSantuario() throws Throwable {
        Poligono freePolygon = new Poligono((List.of(
                new Point(-0, -0),
                new Point(-3, -6),
                new Point(-6, -1),
                new Point(-0, -0)
        )));
        poligonoService.crear(freePolygon);

        CreateUbicacionDTO cementerio = new CreateUbicacionDTO("Cementerio del Oeste", 10, TipoUbicacion.Cementerio, freePolygon.getId());
        CreateEspirituDTO espDemonio = new CreateEspirituDTO(10,10,"Demonio", cementerio.nombre(), TipoEspiritu.EspirituDemoniaco);

        UbicacionDTO cementerioDTO = mockMVCUbicacionController.crear(cementerio, HttpStatus.OK, UbicacionDTO.class);
        EspirituDTO espDemonioDTO = mockMVCEspirituController.crear(espDemonio, HttpStatus.OK, EspirituDTO.class);

        ErrorDTO dtoRecibido = mockMVCMediumController.invocar(mediumDTO.id(), espDemonioDTO.id(), HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("No se puede invocar al EspirituDemoniaco en el Santuario.",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
        poligonoService.eliminar(freePolygon.getId());
    }

    @Test
    public void moverMediumValido() throws Throwable {
        MediumDTO mediumMovidoDTO = mockMVCMediumController.mover(mediumDTO.id(), 10.0, 10.0, HttpStatus.OK, MediumDTO.class);
        assertEquals(mediumMovidoDTO.ubicacion().id(), otroSantuarioDTO.id());
    }

    @Test
    public void moverInvalidoPorMediumNoPersistido() throws Throwable {
        Long idInexistente = 128L;
        ErrorDTO dtoRecibido = mockMVCMediumController.mover(idInexistente, 60.0, 30.0, HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El medium no se ha encontrado",404);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void moverInvalidoPorPoligonoNoPersistido() throws Throwable {
        ErrorDTO dtoRecibido = mockMVCMediumController.mover(mediumDTO.id(), 90.0, 30.0, HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("La ubicacion no se ha encontrado",404);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void descansarValido() throws Throwable {
        CreateMediumDTO unMediumCansado = new CreateMediumDTO("Cansadin", 200, 50, santuario.nombre());
        MediumDTO unMediumCansadoDTO = mockMVCMediumController.crear(unMediumCansado, HttpStatus.OK, MediumDTO.class);

        MediumDTO mediumDescansadoDTO = mockMVCMediumController.descansar(unMediumCansadoDTO.id(), HttpStatus.OK, MediumDTO.class);

        assertEquals(mediumDescansadoDTO.mana(), 65);
        // El santuario tiene 10 de energia, y su cantidad a aumentar es 15 (50 * 1.50)
    }

    @Test
    public void descansarInvalidoPorMediumNoPersistido() throws Throwable {
        ErrorDTO dtoRecibido = mockMVCMediumController.descansar(128L, HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El medium no se ha encontrado",404);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @Test
    public void exorcizarValido() throws Throwable {
        CreateMediumDTO exorcista = new CreateMediumDTO("Exorcista", 200, 200, santuario.nombre());
        CreateMediumDTO endemoniado = new CreateMediumDTO("Endemoniado", 70, 70, santuario.nombre());

        MediumDTO exorcistaDTO = mockMVCMediumController.crear(exorcista, HttpStatus.OK, MediumDTO.class);
        MediumDTO endemoniadoDTO = mockMVCMediumController.crear(endemoniado, HttpStatus.OK, MediumDTO.class);

        exorcistaDTO = mockMVCEspirituController.conectar(angelDTO1.id(), exorcistaDTO.id(), HttpStatus.OK, MediumDTO.class);
        exorcistaDTO = mockMVCEspirituController.conectar(angelDTO2.id(), exorcistaDTO.id(), HttpStatus.OK, MediumDTO.class);
        endemoniadoDTO = mockMVCEspirituController.conectar(demonioDTO1.id(), endemoniadoDTO.id(), HttpStatus.OK, MediumDTO.class);

        mockMVCMediumController.exorcizar(exorcistaDTO.id(), endemoniadoDTO.id(), HttpStatus.OK, MediumDTO.class);

        var demoniosRecuperados = mockMVCMediumController.espiritus(endemoniadoDTO.id());

        assertEquals(0, demoniosRecuperados.size());
    }

    @Test
    public void exorcizarInvalidoPorMediumsNoPersistidos() throws Throwable {
        CreateMediumDTO exorcista = new CreateMediumDTO("Exorcista", 200, 200, santuario.nombre());
        MediumDTO exorcistaDTO = mockMVCMediumController.crear(exorcista, HttpStatus.OK, MediumDTO.class);
        exorcistaDTO = mockMVCEspirituController.conectar(angelDTO1.id(), exorcistaDTO.id(), HttpStatus.OK, MediumDTO.class);

        ErrorDTO dtoRecibido1 = mockMVCMediumController.exorcizar(exorcistaDTO.id(), 128L, HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoRecibido2 = mockMVCMediumController.exorcizar(128L, exorcistaDTO.id(), HttpStatus.NOT_FOUND, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El medium no se ha encontrado",404);

        assertEquals(dtoEsperado.error(),dtoRecibido1.error());
        assertEquals(dtoEsperado.error(),dtoRecibido2.error());
    }

    @Test
    public void exorcizarInvalidoPorMediumSinAngeles() throws Throwable {
        CreateMediumDTO exorcista = new CreateMediumDTO("Exorcista", 200, 200, santuario.nombre());
        CreateMediumDTO endemoniado = new CreateMediumDTO("Endemoniado", 70, 70, santuario.nombre());

        MediumDTO exorcistaDTO = mockMVCMediumController.crear(exorcista, HttpStatus.OK, MediumDTO.class);
        MediumDTO endemoniadoDTO = mockMVCMediumController.crear(endemoniado, HttpStatus.OK, MediumDTO.class);

        ErrorDTO dtoRecibido = mockMVCMediumController.exorcizar(exorcistaDTO.id(), endemoniadoDTO.id(), HttpStatus.BAD_REQUEST, ErrorDTO.class);
        ErrorDTO dtoEsperado = new ErrorDTO("El exorcista no tiene ningun angel",400);

        assertEquals(dtoEsperado.error(),dtoRecibido.error());
    }

    @AfterEach
    public void cleanUp() {
        serviceMedium.clearAll();
        serviceEspiritu.clearAll();
        poligonoService.eliminar(oestePoligono.getId());
        poligonoService.eliminar(estePoligono.getId());
        serviceUbicacion.clearAll();
    }

}