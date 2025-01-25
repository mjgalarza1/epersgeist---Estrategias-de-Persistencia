package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.dto.HabilidadPersistDTO;
import ar.edu.unq.epersgeist.persistencia.dto.PoligonoPersistDTO;
import ar.edu.unq.epersgeist.persistencia.dto.SqlDataDTO;
import lombok.*;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Document("Snapshot")
public class Snapshot  implements Serializable {
    @Id
    private String id;
    private SqlDataDTO sql;
    private List<HabilidadPersistDTO> neo4j;
    private List<PoligonoPersistDTO> mongo;
    private LocalDate fecha;

    public Snapshot(SqlDataDTO sql, List<HabilidadPersistDTO> neo4j, List<PoligonoPersistDTO> mongo, LocalDate fecha) {
        this.sql = sql;
        this.neo4j = neo4j;
        this.mongo = mongo;
        this.fecha = fecha;
    }
}
