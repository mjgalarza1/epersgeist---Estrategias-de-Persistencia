package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.controller.utils.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ubicaciones")
public record UbicacionPersistDTO(@Id String id, String nombre, int energiaUbicacion, TipoUbicacion tipo, String idPoligono) {

        public static UbicacionPersistDTO desdeModelo(Ubicacion ubicacion){
            return new UbicacionPersistDTO(
                    ubicacion.getId().toString(),
                    ubicacion.getNombre(),
                    ubicacion.getEnergiaUbicacion(),
                    TipoUbicacion.valueOf(ubicacion.getClass().getSimpleName()),
                    ubicacion.getIdPoligono());
        }
}
