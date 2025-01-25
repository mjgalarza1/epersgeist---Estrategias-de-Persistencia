package ar.edu.unq.epersgeist.controller.rest;

import ar.edu.unq.epersgeist.controller.dto.CreateEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.controller.utils.Validator;
import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("/espiritus")
final public class EspirituREST {

    private final EspirituService espirituService;
    private final UbicacionService ubicacionService;

    public EspirituREST(EspirituService espirituService, UbicacionService ubicacionService){
        this.espirituService = espirituService;
        this.ubicacionService = ubicacionService;
    }

    @PostMapping()
    public ResponseEntity<EspirituDTO> crear(@RequestBody CreateEspirituDTO dto) {
        Validator.getInstance().validarCreateEspirituDTO(dto);
        Ubicacion ubicacion = ubicacionService.findByName(dto.nombreUbicacion());
        Espiritu espiritu = dto.aModelo(ubicacion);
        espirituService.crear(espiritu);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(espiritu));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTO> recuperar(@PathVariable Long id) {
        Espiritu esp = espirituService.recuperar(id);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(esp));
    }

    @GetMapping()
    public ResponseEntity<List<EspirituDTO>> recuperarTodos() {
        List<Espiritu> espiritus = espirituService.recuperarTodos();
        List<EspirituDTO> espirituDTOS = espiritus.stream().map(EspirituDTO::desdeModelo).toList();
        return ResponseEntity.ok((espirituDTOS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        espirituService.eliminar(id);
        return ResponseEntity.ok("Tu espiritu ha sido eliminado correctamente.");
    }

    @PutMapping("/{id}/conectarA/{idMedium}")
    public ResponseEntity<MediumDTO> conectar(@PathVariable Long id, @PathVariable Long idMedium){
        Medium medium = espirituService.conectar(id,idMedium);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    // PROBAR
    @PutMapping("/{idDominante}/dominar/{idDominado}")
    public ResponseEntity<EspirituDTO> dominarA(@PathVariable Long idDominante, @PathVariable Long idDominado){
        espirituService.dominar(idDominante, idDominado);
        Espiritu esp = espirituService.recuperar(idDominado);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(esp));
    }

    @GetMapping("/demoniacos")
    public ResponseEntity<List<EspirituDTO>> espiritusDemoniacos(@RequestParam Direccion dir,  @RequestParam int pagina,  @RequestParam int cantidadPorPagina ){
        List<Espiritu> espiritus = espirituService.espiritusDemoniacos(dir,pagina,cantidadPorPagina);
        List<EspirituDTO> espirituDTOS = espiritus.stream().map(EspirituDTO::desdeModelo).toList();
        return ResponseEntity.ok((espirituDTOS));
    }

}