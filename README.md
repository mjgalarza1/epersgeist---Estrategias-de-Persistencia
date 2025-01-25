# Descripción
Este repositorio contiene el material práctico desarrollado durante la cursada de la materia [***Estrategias de Persistencia***](https://github.com/EPERS-UNQ) en la Universidad Nacional de Quilmes.

Se trata de un proyecto que se realizó en equipos de 4~5 personas, en el que, dado cada enunciado, se tuvo que implementar distintas estrategias de persistencia, cada una con una diferente base de datos.

# Enunciados del proyecto

+ [Entrega 1 - JDBC](https://github.com/mjgalarza1/epersgeist---Estrategias-de-Persistencia/blob/main/enunciado/entrega1/entrega1.md)
+ [Entrega 2 - ORM - Hibernate](https://github.com/mjgalarza1/epersgeist---Estrategias-de-Persistencia/blob/main/enunciado/entrega2/entrega2.md)
+ [Entrega 3 - ORM - Spring](https://github.com/mjgalarza1/epersgeist---Estrategias-de-Persistencia/blob/main/enunciado/entrega3/entrega3.md)
+ [Entrega 4 - NoSQL - Neo4j - Spring](https://github.com/mjgalarza1/epersgeist---Estrategias-de-Persistencia/blob/main/enunciado/entrega4/enunciado_tp4.md)
+ [Entrega 5 - NoSQL - MongoDB - Spring](https://github.com/mjgalarza1/epersgeist---Estrategias-de-Persistencia/blob/main/enunciado/entrega5/enunciado_tp5.md)
+ [Entrega 6 - NoSQL - Firebase - Spring](https://github.com/mjgalarza1/epersgeist---Estrategias-de-Persistencia/blob/main/enunciado/entregaFinal/enunciadoFinal.md)

# Tecnologías usadas
+ **`Java 21`**<br> como lenguaje de programación.
+ **`Bases de Datos`**
  - Relacional
    - **`MySQL`**.
  - NoSQL
    - **`Neo4J`**: Uso práctico de bases de grafos para recorrer nodos.
    - **`MongoDB`**: Para consultas geo-espaciales.
  - Extra
    - **`Firestore`**: Investigación e implementación como proyecto final.
+ **`Spring Boot`**<br> como framework principal del proyecto.
+ **`Programación Reactiva`**<br> para manejo asíncrono de datos en tiempo real utilizando Mono y Flux _(implementado en el proyecto final para potenciar la propiedad realtime de Firebase)._
+ **`GitHub`**<br> para el control de versiones y trabajo en equipo.
+ **`Gitflow`**<br> para trabajar en distintas `features` en el equipo, y así no generar conflictos en el código.

### Otras herramientas usadas
+ **IntelliJ IDEA**<br> como IDE principal para el proyecto.
+ **MySQL Workbench 8.0 CE**, **Neo4j Desktop** y **MongoDBCompass**<br> para administrar y visualizar el comportamiento del código a la hora de persistir datos.
+ **Postman**<br> para testear los endpoints REST.
+ **MockMVC**<br> Pruebas unitarias e integración para APIs.

# Contenidos abordados
Durante la cursada, se exploraron y enseñaron los siguientes contenidos:

### Persistencia y bases de datos
+ **ORM y Hibernate**: Introducción, ciclo de vida de los objetos y manejo de transacciones.
+ **Teorema CAP**: Consistencia, disponibilidad y tolerancia a particiones.
+ **Propiedades ACID**: Atomicidad, consistencia, aislamiento y durabilidad.
+ **Optimización de performance**: Uso de índices y consultas eficientes para evitar datos innecesarios.
+ **Permisos en bases de datos**: Tipos como Read-Only, Read-Write y No-Access.

### Frameworks y arquitectura
+ **Spring Framework**: Configuración del entorno, inyección de dependencias y manejo de transacciones.
+ **Clean Architecture**:
  - **Controller**: APIs REST y DTOs.
  - **Modelo**: Lógica de negocio.
  - **Service**: Intermediario entre la lógica de negocio y la persistencia.
  - **Persistencia**: DAOs para interactuar con bases de datos.

### Problemáticas abordadas
+ **Concurrencia**: Lockeo optimista y pesimista, estrategias para acceso concurrente y caché L2 en Hibernate.
+ **Impedancia objeto-relacional**: Soluciones para transformar estructuras de objetos en bases de datos.
+ **Estrategias de actualización**: Reachability y cascada.

### Testing
+ **Unitarias**: Pruebas de componentes individuales.
+ **Integración**: Verificación de interacción entre módulos.
+ **End-to-End**: Validación completa del flujo de la aplicación.

# ¿Cómo instalar el proyecto?
1. Clonar el repositorio
```
git clone https://github.com/mjgalarza1/epersgeist---Estrategias-de-Persistencia.git
```
2. Configurar las bases de datos necesarias:
    - **MySQL**: Crear la base de datos y actualizar `application.properties` con las credenciales.
    - **MongoDB**: Asegurarse de que el servidor esté corriendo.
    - **Neo4J**: Proveer la URL y las credenciales.
    - **Firebase**: Descarga la key de Firebase y guardala en el directorio `resources` con el nombre `epers-key.json`.
3. Ejecutar `EpersgeistApplication` que se encuentra dentro de `src/main/java/ar.edu.unq.epersgeist`.
4. La API se levantará de manera local en `http://localhost:8080`

# ⚠ Importante
+ **`¿Por qué no hay commits?`** <br> El repositorio original **es privado**, y no tengo el control para hacerlo público _(ni haciendo un fork podía hacerlo público)_, por lo no me quedó otra alternativa que crear uno nuevo.
+ Este repositorio está bajo licencia de uso académico, destinado a fines educativos y como muestra de los contenidos vistos en la materia.

