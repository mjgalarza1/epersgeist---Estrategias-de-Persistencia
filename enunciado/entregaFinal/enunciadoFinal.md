# Continuación...
La tarea nos llegó de forma inesperada, encomendados por RDJ con una misión que rozaba lo absurdo: recuperar las partes perdidas de su brazo. 
Cuando le preguntamos dónde podrían estar, su voz temblorosa y quebrada nos puso en alerta:
“Mi brazo... está en posesión de varios espíritus demoníacos… despiadados”
Sin tiempo para digerir la extraña confesión, nos embarcamos en un viaje que nos llevó a las zonas más oscuras y perturbadoras que jamás habíamos 
imaginado. El aire era denso, cargado de olores nauseabundos capaces de revolver el estómago del más estoico. A lo lejos, nuestras miradas se cruzaron 
con una escena tan grotesca como inquietante: un grupo de espíritus se entretenía lanzándose partes del brazo de RDJ, como si fuera una pelota de basquet.

<p align="left">
  <img src="ouijpers.png.jpeg" />
</p>

Ocultos en las sombras, los observamos en silencio hasta que uno de nosotros, armado con una valentía forzada, dio un paso al frente. Tragó saliva y, 
con un tono firme que apenas disimulaba su miedo, habló:

“Disculpen… esas partes del brazo que tienen son de un amigo mío. ¿Qué puedo ofrecerles para que me las devuelvan?”

Los espíritus detuvieron su juego al instante. Sus miradas se posaron sobre él, inquisitivas y gélidas. Luego, como si compartieran un chiste privado, 
rompieron en una carcajada colectiva tan estridente como siniestra. Tras unos segundos, se reunieron en un círculo y comenzaron a susurrar, lanzando 
miradas de reojo que sólo aumentaban la tensión.
Cuando volvieron a mirarnos, uno de ellos, con una sonrisa torcida, anunció:

“Si quieren recuperar ese asqueroso brazo y salir con vida, deberán ganarnos en un juego… nuestro juego. Lo llamamos ‘Ouijpers’. Es como su patético 
‘ahorcado’, pero mucho más... entretenido para nosotros.”

Nos explicaron las reglas con una calma que helaba la sangre:
Tres rondas. Tres palabras por adivinar. Por cada una que adivinen, les daremos una parte del maldito brazo. Cada error les costará una parte de sus 
almas. La incertidumbre nos asaltó. ¿Qué significaba “tomar partes de nuestras almas”? Al preguntar, la respuesta llegó con una frialdad que 
traspasó nuestros huesos:

“Si pierden todas las rondas, sus almas quedarán atrapadas en este mundo, y sus cuerpos serán nuestros para regresar al mundo humano.”

La amenaza era clara. Con pocas opciones y el tiempo en nuestra contra, decidimos aceptar las condiciones. Nos adentramos en el juego diabólico, 
conscientes de que la victoria era nuestra única esperanza para devolverle el brazo a RDJ… y salvar nuestras almas de un destino peor que la muerte.

Funciones
Jugador
La clase Jugador será una clase sencilla que deberá contener como mínimo:
un String para el nombre,
y un int para el puntaje total de las partidas del juego.

## Juego
El Juego sigue los mismos principios que los del “ahorcado”, por lo tanto, se espera que se este se comporte de la misma manera, con el detalle de que:
el Juego tiene tres Rondas
cada ronda tiene un set de palabras atribuidas a su respectivo nivel (es decir, las Rondas van aumentando en dificultad, donde las palabras son más 
difíciles a medida que se avanza)
en cada Ronda, el Jugador deberá intentar adivinar la palabra letra por letra
el Jugador solo tiene hasta 6 intentos por Ronda (cada intento representa una parte del cuerpo del ahorcado)
por cada letra acertada, el Jugador deberá obtener 1 punto.
por cada palabra acertada, el Jugador deberá obtener 5 puntos.


## JugadorService
La clase JugadorService deberá implementarse utilizando la base de datos Firestore, y debe tener los métodos CRUD.
También deberá implementar:
- `Mono<Jugador> adivinarLetra(Jugador jugador, Character letra, Juego juego):` que al darle un jugador, una letra y un juego deberá devolver un Jugador
 con su puntaje actualizado.
- `Mono<Integer> obtenerPuntaje(String nombre):` que al darle un nombre de un jugador, devuelve el puntaje total obtenido.
- `Flux<Jugador> obtenerRanking():` que deberá mostrar un ranking ordenado de forma descendente con los puntajes de todos los jugadores.

## JuegoService
La clase JuegoService deberá implementarse usando la base de datos MySql, y debe tener los métodos CRUD.
También deberá implementar:
- `int cantIntentosRestantes(Long id):` que al darle la id de un juego, devuelve la cantidad de intentos que quedan restantes de la ronda actual.
- `String palabraAdivinando(Long id):` que al darle la id de un juego, devuelve un String de la palabra a adivinar de la ronda actual.
- `Jugador empezarJuego(String nombre):` que al darle un nombre, deberá crear un juego y persistirlo, además de crear un jugador y persistir al mismo, 
además de devolverlo.
- `String letrasEquivocadas(Long id):` que al darle la id de un juego, devuelve un String con las letras que se intentaron adivinar pero eran incorrectas.
- `String rondaActual(Long id):` que al darle la id de un juego, devuelve un String con la ronda actual.

Por último, se piden `Controllers` tanto para Jugador como para Juego, con endpoints por cada función mencionada arriba en sus respectivos services.

## Bonus
Se pide crear una ronda final, en la que los 3 mejores jugadores luchen juntos para recuperar el brazo de RDJ. Se deberan mostrar los cambios de cada uno
en tiempo real para que los 3 jugadores puedan adivinar la misma palabra en simultaneo desde diferentes dispositivos.
Para eso se tendrá que implementar el metodo:
- `Juego empezarRondaUltimate(Jugador j1, Jugador j2, Jugador j3, Long idJuego):` que al darle los 3 mejores jugadores de la partida y el id del juego que 
van a compartir, crea la RondaUltimate con la palabra a adivinar y se la asigna a todos los jugadores.

