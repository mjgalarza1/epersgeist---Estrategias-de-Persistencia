package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface HabilidadDAO extends Neo4jRepository<Habilidad, Long> {

    @Query("""
            MATCH (hO:Habilidad {nombre: $nombreOrigen}), (hD:Habilidad {nombre: $nombreDestino})
            CREATE(hO)-[c:CONDICION {evaluacion: $evaluacion, cantidad: $cantidad}] -> (hD)
            RETURN c
    """)
    void descubrirHabilidad(@Param("nombreOrigen") String nombreHabilidadOrigen, @Param("nombreDestino") String nombreHabilidadDestino,
                            @Param("evaluacion")Evaluacion evaluacion,
                            @Param("cantidad")Integer cantidad);

    @Query("""
            MATCH(h: Habilidad{nombre: $nombre})
            RETURN h
            """)
    Habilidad recuperar(@Param("nombre") String nombre);

    @Query("""
            MATCH (h:Habilidad {nombre: $nombre})-[c:CONDICION]->(:Habilidad {nombre: $nombreDestino})
            RETURN c.evaluacion
            """)
    Evaluacion evaluacionDeCondicionDeLaHabilidad(@Param("nombre") String nombre, @Param("nombreDestino") String nombreDestino);

    @Query("""
            MATCH (h:Habilidad {nombre: $nombre})-[c:CONDICION]->(:Habilidad {nombre: $nombreDestino})
            RETURN c.cantidad
            """)
    Integer cantidadDeCondicionDeLaHabilidad(@Param("nombre") String nombre, @Param("nombreDestino") String nombreDestino);


    @Query("""
        MATCH (h)
        WHERE NOT (()-[:CONDICION*]->(h))
        RETURN h.nombre
        """)
    List<String> nombresHabilidadesRaices();

    @Query("""
        MATCH (h)
        WHERE NOT (()-[:CONDICION*]->(h))
        RETURN h
        """)
    Set<Habilidad> habilidadesRaices();

    @Query("""
            MATCH (h:Habilidad)-[c:CONDICION]->(vecina:Habilidad)
            WHERE h.nombre IN $habilidades
            AND ((c.evaluacion = 'ENERGIA' AND c.cantidad <= $energia)
                OR (c.evaluacion = 'NIVEL_DE_CONEXION' AND c.cantidad <= $nivelDeConexion)
                OR (c.evaluacion = 'CANT_EXORCISMOS_EVITADOS' AND c.cantidad <= $cantExorcismosEvitados)
                OR (c.evaluacion = 'CANT_EXORCISMOS_HECHOS' AND c.cantidad <= $cantExorcismosHechos))
            AND NOT vecina.nombre IN $habilidades
            RETURN vecina.nombre
        """)
    List<String> nombresDeHabilidadesPosibles(@Param("habilidades") Set<String> habilidades, @Param("energia") Integer energia,
                                          @Param("nivelDeConexion") Integer nivelDeConexion, @Param("cantExorcismosEvitados") Integer cantExorcismosEvitados,
                                          @Param("cantExorcismosHechos") Integer cantExorcismosHechos);

    @Query("""
            MATCH (h:Habilidad)-[c:CONDICION]->(vecina:Habilidad)
            WHERE h.nombre = $habilidad
            RETURN vecina
            """)
    Set<Habilidad> habilidadesConectadas(@Param("habilidad") String nombreHabilidad);

    @Query("""
            MATCH (h:Habilidad)-[c:CONDICION]->(vecina:Habilidad)
            WHERE h.nombre IN $habilidades
            AND ((c.evaluacion = 'ENERGIA' AND c.cantidad <= $energia)
                OR (c.evaluacion = 'NIVEL_DE_CONEXION' AND c.cantidad <= $nivelDeConexion)
                OR (c.evaluacion = 'CANT_EXORCISMOS_EVITADOS' AND c.cantidad <= $cantExorcismosEvitados)
                OR (c.evaluacion = 'CANT_EXORCISMOS_HECHOS' AND c.cantidad <= $cantExorcismosHechos))
            AND NOT vecina.nombre IN $habilidades
            RETURN vecina
        """)
    Set<Habilidad> habilidadesPosibles(@Param("habilidades") Set<String> habilidades, @Param("energia") Integer energia,
                                       @Param("nivelDeConexion") Integer nivelDeConexion,
                                       @Param("cantExorcismosEvitados") Integer cantExorcismosEvitados,
                                       @Param("cantExorcismosHechos") Integer cantExorcismosHechos);

    @Query("""
            MATCH (habilidadOrigen:Habilidad {nombre: $origen}), (habilidadDestino:Habilidad {nombre: $destino}),
                  path = shortestPath((habilidadOrigen)-[:CONDICION*]->(habilidadDestino))
            WHERE ALL(camino IN relationships(path) WHERE camino.evaluacion IN $evaluaciones)
            UNWIND nodes(path) AS habilidad
            WITH habilidad
            RETURN habilidad
            """)
    List<Habilidad> caminoMasRentable(@Param("origen") String nombreHabilidadOrigen, @Param("destino") String nombreHabilidadDestino,
                                      @Param("evaluaciones") Set<Evaluacion> evaluaciones);

    @Query(
            """
            RETURN EXISTS {
                MATCH (h:Habilidad {nombre:$origen})-[:CONDICION*]->(h2:Habilidad {nombre:$destino})
            } AS estanConectadosIndirectamente
            """
    )
    Boolean estanConectados(@Param("origen") String origen, @Param("destino") String destino);

    @Query("""
    MATCH camino=(h:Habilidad {nombre: $nombreHabilidad})-[condicion:CONDICION*]->(h2:Habilidad)
    WHERE ALL(c IN condicion
        WHERE (c.evaluacion = 'ENERGIA' AND c.cantidad <= $energia)
            OR (c.evaluacion = 'NIVEL_DE_CONEXION' AND c.cantidad <= $nivelDeConexion)
            OR (c.evaluacion = 'CANT_EXORCISMOS_EVITADOS' AND c.cantidad <= $cantExorcismosEvitados)
            OR (c.evaluacion = 'CANT_EXORCISMOS_HECHOS' AND c.cantidad <= $cantExorcismosHechos))
    WITH camino
    ORDER BY length(camino) DESC
    LIMIT 1
    UNWIND nodes(camino) AS habilidad
    RETURN habilidad
    """)
    List<Habilidad> caminoMasMutable(@Param("nombreHabilidad") String nombreHabilidad, @Param("energia") Integer energia,
                                     @Param("nivelDeConexion") Integer nivelDeConexion,
                                     @Param("cantExorcismosEvitados") Integer cantExorcismosEvitados,
                                     @Param("cantExorcismosHechos") Integer cantExorcismosHechos);

    @Query("""
    MATCH camino = shortestPath((h:Habilidad {nombre: $nombreHabilidad})-[condicion:CONDICION*]->(h2:Habilidad))
        WHERE ALL(c IN condicion
            WHERE (c.evaluacion = 'ENERGIA' AND c.cantidad <= $energia)
                OR (c.evaluacion = 'NIVEL_DE_CONEXION' AND c.cantidad <= $nivelDeConexion)
                OR (c.evaluacion = 'CANT_EXORCISMOS_EVITADOS' AND c.cantidad <= $cantExorcismosEvitados)
                OR (c.evaluacion = 'CANT_EXORCISMOS_HECHOS' AND c.cantidad <= $cantExorcismosHechos))
        AND NOT (h2)-[:CONDICION]->()
        WITH camino, length(camino) AS caminoLength
        ORDER BY caminoLength ASC
        LIMIT 1
        UNWIND nodes(camino) AS habilidad
        RETURN DISTINCT habilidad
    """)
    List<Habilidad> caminoMenosMutable(@Param("nombreHabilidad") String nombreHabilidad, @Param("energia") Integer energia,
                                       @Param("nivelDeConexion") Integer nivelDeConexion,
                                       @Param("cantExorcismosEvitados") Integer cantExorcismosEvitados,
                                       @Param("cantExorcismosHechos") Integer cantExorcismosHechos);

    @Query("""
        MATCH (n {nombre: $nombreHabilidad})-[c:CONDICION]->()
        RETURN COUNT(c) > 0
    """)
    boolean tieneRelacionLindante(@Param("nombreHabilidad") String nombreHabilidad);

    @Query("""
        MATCH (h:Habilidad {nombre: $nombreHabilidad}) RETURN count(h) > 0 AS existe
    """)
    boolean habilidadExiste(@Param("nombreHabilidad") String nombreHabilidad);
}
