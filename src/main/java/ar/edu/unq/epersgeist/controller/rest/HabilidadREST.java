package ar.edu.unq.epersgeist.controller.rest;

import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.controller.utils.Validator;
import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.HabilidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/habilidades")
final public class HabilidadREST {

    private final HabilidadService habilidadService;
    private final EspirituService espirituService;

    public HabilidadREST(HabilidadService habilidadService, EspirituService espirituService) {
        this.habilidadService = habilidadService;
        this.espirituService = espirituService;
    }

    @GetMapping()
    public ResponseEntity<List<CreateHabilidadDTO>> recuperarTodos() {
        List<Habilidad> habilidades = habilidadService.recuperarTodos();
        List<CreateHabilidadDTO> habilidadesDTOs = habilidades.stream().map(CreateHabilidadDTO::desdeModelo).toList();
        return ResponseEntity.ok((habilidadesDTOs));
    }

    @PostMapping()
    public ResponseEntity<CreateHabilidadDTO> crear(@RequestBody CreateHabilidadDTO dto) {
        Validator.getInstance().validarCreateHabilidadDTO(dto);
        Habilidad habilidad = CreateHabilidadDTO.aModelo(dto);
        habilidadService.crear(habilidad);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{nombreHabilidadOrigen}/descubrirHabilidad/{nombreHabilidadDestino}")
    public ResponseEntity<CreateHabilidadDTO> descubrirHabilidad(@PathVariable String nombreHabilidadOrigen, @PathVariable String nombreHabilidadDestino,
                                                                 @RequestBody CondicionDTO condicionDTO) {
        Validator.getInstance().validarCondicionDTO(condicionDTO);
        Condicion condicion = CondicionDTO.aModelo(condicionDTO);
        habilidadService.descubrirHabilidad(nombreHabilidadOrigen, nombreHabilidadDestino, condicion);
        Habilidad habilidad = habilidadService.recuperar(nombreHabilidadOrigen);
        return ResponseEntity.ok(CreateHabilidadDTO.desdeModelo(habilidad));
    }

    @PutMapping("/evolucionar/{espirituId}")
    public ResponseEntity<EspirituDTO> evolucionar (@PathVariable Long espirituId){
        habilidadService.evolucionar(espirituId);
        Espiritu esp = espirituService.recuperar(espirituId);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(esp));
    }

    @GetMapping("/{nombreHabilidad}/habilidadesConectadas")
    public ResponseEntity<List<CreateHabilidadDTO>> habilidadesConectadas(@PathVariable String nombreHabilidad){
        List<CreateHabilidadDTO> habilidadDTOs = habilidadService.habilidadesConectadas(nombreHabilidad).stream().map(CreateHabilidadDTO::desdeModelo).toList();
        return ResponseEntity.ok((habilidadDTOs));
    }

    @GetMapping("/habilidadesPosibles/{espirituId}")
    public ResponseEntity<List<CreateHabilidadDTO>> habilidadesPosibles(@PathVariable Long espirituId){
        List<CreateHabilidadDTO> habilidadDTOs = habilidadService.habilidadesPosibles(espirituId).stream().map(CreateHabilidadDTO::desdeModelo).toList();
        return ResponseEntity.ok((habilidadDTOs));
    }

    @GetMapping("/{nombreHabilidadOrigen}/caminoMasRentable/{nombreHabilidadDestino}")
    public ResponseEntity<List<CreateHabilidadDTO>> caminoMasRentable(@PathVariable String nombreHabilidadOrigen,
                                                                      @PathVariable String nombreHabilidadDestino,
                                                                      @RequestParam Set<Evaluacion> evaluaciones){
        List<CreateHabilidadDTO> habilidadDTOs = habilidadService.caminoMasRentable(nombreHabilidadOrigen,nombreHabilidadDestino, evaluaciones).stream().map(CreateHabilidadDTO::desdeModelo).toList();
        return ResponseEntity.ok((habilidadDTOs));
    }

    @GetMapping("/recuperar/{nombreHabilidad}")
    public ResponseEntity<CreateHabilidadDTO> recuperar(@PathVariable String nombreHabilidad){
        Habilidad habilidad = habilidadService.recuperar(nombreHabilidad);
        return ResponseEntity.ok(CreateHabilidadDTO.desdeModelo(habilidad));
    }


}
