package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import java.util.List;

public record SqlDataDTO(List<EspirituPersistDTO> espiritus, List<MediumPersistDTO> mediums ,List<UbicacionPersistDTO> ubicaciones)  {
    public static SqlDataDTO desdeModelo(List<Espiritu> espiritus, List<Medium> mediums , List<Ubicacion> ubicaciones) {
        return new SqlDataDTO(
                espiritus.stream().map(EspirituPersistDTO::desdeModelo).toList(),
                mediums.stream().map(MediumPersistDTO::desdeModelo).toList(),
                ubicaciones.stream().map(UbicacionPersistDTO::desdeModelo).toList()
                );
        }
}
