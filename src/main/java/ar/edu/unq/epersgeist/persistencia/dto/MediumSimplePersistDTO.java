package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("mediumsSimple")
public record MediumSimplePersistDTO(@Id String id, String nombre) {
    public static MediumSimplePersistDTO desdeModelo(Medium medium) {
        return new MediumSimplePersistDTO(
                medium.getId().toString(),
                medium.getNombre());
    }
}

