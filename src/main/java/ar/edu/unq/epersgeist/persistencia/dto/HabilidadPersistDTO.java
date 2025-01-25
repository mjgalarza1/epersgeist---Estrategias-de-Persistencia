package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.modelo.Habilidad;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("habilidades")
public record HabilidadPersistDTO(@Id String id, String nombre) {

    public static HabilidadPersistDTO desdeModelo(Habilidad habilidad) {
        return new HabilidadPersistDTO(habilidad.getId().toString(), habilidad.getNombre());
    }
}