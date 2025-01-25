package ar.edu.unq.epersgeist.controller.utils;


import ar.edu.unq.epersgeist.controller.dto.*;

import java.util.Arrays;

public class Validator {

    private static volatile Validator instance;
    private static final String NOMBRE_VACIO_O_NULO = "El nombre no puede estar vacio o nulo.";
    private static final String ENERGIA_POSITIVA_O_MENOR_A_100 = "La energia debe ser positiva y menor a 100";

    private Validator() {}

    public static Validator getInstance() {
        if (instance == null) {
            synchronized (Validator.class) {
                if (instance == null) {
                    instance = new Validator();
                }
            }
        }
        return instance;
    }


    public void validarCreateMediumDTO(CreateMediumDTO createMediumDTO) {
        if (createMediumDTO.nombre() == null || createMediumDTO.nombre().isBlank()) {
            throw new IllegalArgumentException(NOMBRE_VACIO_O_NULO);
        }
        if (createMediumDTO.manaMax() == null || createMediumDTO.manaMax() < 0) {
            throw new IllegalArgumentException("El mana maximo no puede estar vacio o ser menor a 0.");
        }
        if (createMediumDTO.mana() == null || createMediumDTO.mana() < 0) {
            throw new IllegalArgumentException("El mana no puede ser nulo o negativo.");
        }
        if (createMediumDTO.mana() > createMediumDTO.manaMax()){
            throw new IllegalArgumentException("El mana no puede superar el mana Max.");
        }
        if (createMediumDTO.ubicacion() == null || createMediumDTO.ubicacion().isBlank()) {
            throw new IllegalArgumentException(NOMBRE_VACIO_O_NULO);
        }
    }

    public void validarUbicacionDTO(CreateUbicacionDTO dto){
        if (dto.nombre() == null || dto.nombre().isBlank()) {
            throw new IllegalArgumentException(NOMBRE_VACIO_O_NULO);
        }
        if (dto.energiaUbicacion() < 0 || dto.energiaUbicacion() > 100  ) {
            throw new IllegalArgumentException(ENERGIA_POSITIVA_O_MENOR_A_100);
        }
        if (dto.tipo() == null) {
            throw new IllegalArgumentException("El tipo de ubicacion es invalido.");
        }
        if (dto.idPoligono() == null) {
            throw new IllegalArgumentException("El id no puede ser nulo.");
        }
    }

    public void validarCreateEspirituDTO(CreateEspirituDTO dto) {
        if (dto.energia() == null || dto.energia() < 0 || dto.energia() > 100) {
            throw new IllegalArgumentException(ENERGIA_POSITIVA_O_MENOR_A_100);
        }
        if (dto.nivelDeConexion() == null || dto.nivelDeConexion() < 0) {
            throw new IllegalArgumentException("El nivel de conexion no puede ser nulo ni negativo.");
        }
        if (dto.nombre() == null || dto.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        this.validarNombre(dto.nombre());
        if (dto.tipo() == null) {
            throw new IllegalArgumentException("El tipo de espiritu es invalido.");
        }
        if (dto.nombreUbicacion() == null || dto.nombreUbicacion().isBlank()) {
            throw new IllegalArgumentException(NOMBRE_VACIO_O_NULO);
        }
    }

    private void validarNombre(String nombre){
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
    }

    public void validarCreateHabilidadDTO(CreateHabilidadDTO dto) {
        if ( dto.nombre() == null || dto.nombre().isBlank() ) {
            throw new IllegalArgumentException(NOMBRE_VACIO_O_NULO);
        }
    }

    public void validarCondicionDTO(CondicionDTO condicionDTO) {
        if (condicionDTO.evaluacion() == null ){
            throw new IllegalArgumentException("La condicion no puede ser nula.");
        }
    }

    public void validarPoligonoDTO(CreatePoligonoDTO dto) {
        if ( dto == null || dto.coordenadas().length < 4) {
            throw new IllegalArgumentException("El polígono es invalido.");
        }
    }

    public void validarJugador(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(NOMBRE_VACIO_O_NULO);
        }
    }

    public void validarLetra(Character letra) {
        if (!Character.isLetter(letra)) {
            throw new IllegalArgumentException("La letra no es valida.");
        }
    }

    public void validarIdDeJuego(Long idJuego) {
        if (idJuego == null) {
            throw new IllegalArgumentException("El id del juego no puede ser nulo.");
        }
    }

    public void validarNombres(NombresDTO nombres) {
        this.validarNombre(nombres.nombreJ1());
        this.validarNombre(nombres.nombreJ2());
        this.validarNombre(nombres.nombreJ3());
    }
}
