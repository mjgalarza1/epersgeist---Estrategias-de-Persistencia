package ar.edu.unq.epersgeist.controller.rest;

import ar.edu.unq.epersgeist.controller.dto.SnapshotDTO;
import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.servicios.EstadisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin
@RequestMapping("/estadistica")
public class EstadisticaREST {
    private final EstadisticaService estadisticaService;

    public EstadisticaREST(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @PostMapping()
    public ResponseEntity<String> crearSnapshot() {
        estadisticaService.crearSnapshot();
        return ResponseEntity.ok("Snapshot creada correctamente.");
    }

    @GetMapping("/{fecha}")
    public ResponseEntity<SnapshotDTO> obtenerSnapshot(@PathVariable LocalDate fecha) {
        Snapshot ss = estadisticaService.obtenerSnapshot(fecha);
        SnapshotDTO dto = SnapshotDTO.desdeModelo(ss);
        return ResponseEntity.ok(dto);
    }


}
