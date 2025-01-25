package ar.edu.unq.epersgeist.controller.rest;

import ar.edu.unq.epersgeist.controller.dto.CreatePoligonoDTO;
import ar.edu.unq.epersgeist.controller.dto.PoligonoDTO;
import ar.edu.unq.epersgeist.controller.utils.Validator;
import ar.edu.unq.epersgeist.modelo.Poligono;
import ar.edu.unq.epersgeist.servicios.PoligonoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/poligonos")
final public class PoligonoREST {

    private final PoligonoService poligonoService;

    public PoligonoREST(PoligonoService poligonoService) {
        this.poligonoService = poligonoService;
    }

    @PostMapping()
    public ResponseEntity<PoligonoDTO> crear(@RequestBody CreatePoligonoDTO dto) {
        Validator.getInstance().validarPoligonoDTO(dto);
        Poligono poligono = dto.aModelo();
        poligonoService.crear(poligono);
        return ResponseEntity.ok(PoligonoDTO.desdeModelo(poligono));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoligonoDTO> recuperar(@PathVariable String id) {
        Poligono poligono = poligonoService.recuperar(id);
        return ResponseEntity.ok(PoligonoDTO.desdeModelo(poligono));
    }

    @GetMapping()
    public ResponseEntity<List<PoligonoDTO>> recuperarTodos() {
        List<Poligono> poligonos = poligonoService.recuperarTodos();
        List<PoligonoDTO> poligonoDTOS = poligonos.stream().map(PoligonoDTO::desdeModelo).toList();
        return ResponseEntity.ok((poligonoDTOS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable String id){
        poligonoService.eliminar(id);
        return ResponseEntity.ok("Tu Poligono ha sido eliminado correctamente.");
    }
}
