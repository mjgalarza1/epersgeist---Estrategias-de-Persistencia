package ar.edu.unq.epersgeist.controller.rest;

import ar.edu.unq.epersgeist.controller.dto.CreateMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.controller.utils.Validator;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/mediums")
final public class MediumREST {

    private final MediumService mediumService;
    private final UbicacionService ubicacionService;

    public MediumREST(MediumService mediumService,  UbicacionService ubicacionService) {
        this.mediumService = mediumService;
        this.ubicacionService = ubicacionService;
    }

    @PostMapping()
    public ResponseEntity<MediumDTO> crear(@RequestBody CreateMediumDTO createMediumDTO) {
        Validator.getInstance().validarCreateMediumDTO(createMediumDTO);
        Ubicacion ubicacionMedium = ubicacionService.findByName(createMediumDTO.ubicacion());
        Medium medium = createMediumDTO.aModelo(ubicacionMedium);
        mediumService.crear(medium);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediumDTO> recuperar(@PathVariable Long id) {
        Medium medium = mediumService.obtenerMedium(id);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @GetMapping()
    public ResponseEntity<List<MediumDTO>> recuperarTodos() {
        List<Medium> mediums = mediumService.recuperarTodos();
        List<MediumDTO> mediumDTOS = mediums.stream().map(MediumDTO::desdeModelo).toList();
        return ResponseEntity.ok((mediumDTOS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        mediumService.eliminar(id);
        return ResponseEntity.ok("Tu Medium ha sido eliminado correctamente.");
    }

    @GetMapping("/{id}/espiritus")
    public ResponseEntity<List<EspirituDTO>> espiritus(@PathVariable Long id) {
        List<Espiritu> espiritus = mediumService.espiritus(id);
        List<EspirituDTO> espirituDTOS = espiritus.stream().map(EspirituDTO::desdeModelo).toList();
        return ResponseEntity.ok((espirituDTOS));
    }


    @GetMapping("/{id}/invocarA/{idEspiritu}")
    public ResponseEntity<EspirituDTO> invocar(@PathVariable Long id, @PathVariable Long idEspiritu){
        Espiritu esp = mediumService.invocar(id,idEspiritu);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(esp));
    }

    @PutMapping("/{id}/moverA/{latitud}/{longitud}")
    public ResponseEntity<MediumDTO> mover(@PathVariable Long id, @PathVariable Double latitud, @PathVariable Double longitud){
        mediumService.mover(id,latitud,longitud);
        Medium medium = mediumService.obtenerMedium(id);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }


    @PutMapping("/{id}/descansar")
    public ResponseEntity<MediumDTO> descansar(@PathVariable Long id){
        mediumService.descansar(id);
        Medium medium = mediumService.obtenerMedium(id);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @PutMapping("/{id}/exorcizarA/{idAExorcizar}")
    public ResponseEntity<MediumDTO> exorcizar(@PathVariable Long id, @PathVariable Long idAExorcizar) {
        mediumService.exorcizar(id, idAExorcizar);
        Medium medium = mediumService.obtenerMedium(id);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

}