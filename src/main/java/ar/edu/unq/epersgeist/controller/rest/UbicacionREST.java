package ar.edu.unq.epersgeist.controller.rest;

import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.controller.utils.Validator;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.PoligonoService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/ubicaciones")
final public class UbicacionREST {

    private final UbicacionService ubicacionService;
    private final PoligonoService poligonoService;

    public UbicacionREST(UbicacionService ubicacionService, PoligonoService poligonoService) {
        this.ubicacionService = ubicacionService;
        this.poligonoService = poligonoService;
    }

    @PostMapping()
    public ResponseEntity<UbicacionDTO> crear(@RequestBody CreateUbicacionDTO dto) {
        Validator.getInstance().validarUbicacionDTO(dto);
        Ubicacion ubi = dto.aModelo();
        Poligono poligono = poligonoService.recuperar(dto.idPoligono());
        ubicacionService.crear(ubi, poligono);
        return ResponseEntity.ok(UbicacionDTO.desdeModelo(ubi));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> recuperar(@PathVariable Long id) {
        Ubicacion ubicacion = ubicacionService.recuperar(id);
        return ResponseEntity.ok(UbicacionDTO.desdeModelo(ubicacion));
    }

    @GetMapping()
    public ResponseEntity<List<UbicacionDTO>> recuperarTodos() {
        List<Ubicacion> ubicaciones = ubicacionService.recuperarTodos();
        List<UbicacionDTO> ubicacionDTOS = ubicaciones.stream().map(UbicacionDTO::desdeModelo).toList();
        return ResponseEntity.ok((ubicacionDTOS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        ubicacionService.eliminar(id);
        return ResponseEntity.ok("Tu Ubicacion ha sido eliminada correctamente.");
    }

    @GetMapping("/{id}/espiritus")
    public ResponseEntity<List<EspirituDTO>> espiritusEn (@PathVariable Long id){
        List<Espiritu> espiritus = ubicacionService.espiritusEn(id);
        List<EspirituDTO> espirituDTOS = espiritus.stream().map(EspirituDTO::desdeModelo).toList();
        return ResponseEntity.ok(espirituDTOS);
    }

    @GetMapping("/{id}/mediumSinEspiritus")
    public ResponseEntity<List<MediumDTO>> mediumSinEspiritusEn (@PathVariable Long id){
        List<Medium> mediumsSinEspiritusEn = ubicacionService.mediumsSinEspiritusEn(id);
        List<MediumDTO> mediumDTOS = mediumsSinEspiritusEn.stream().map(MediumDTO::desdeModelo).toList();
        return ResponseEntity.ok(mediumDTOS);
    }

    @GetMapping("/reporteSantuarioMasCorrupto")
    public ResponseEntity<ReporteSantuarioCorruptoDTO> santuarioCorrupto () {
        ReporteSantuarioCorruptoDTO reporte = ReporteSantuarioCorruptoDTO.desdeModelo(ubicacionService.santuarioCorrupto());
        return ResponseEntity.ok(reporte);
    }

}