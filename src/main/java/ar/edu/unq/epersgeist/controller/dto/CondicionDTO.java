package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.modelo.Habilidad;

public record CondicionDTO (Integer cantidad, Evaluacion evaluacion){

    public static CondicionDTO desdeModelo(Condicion condicion) {
        return new CondicionDTO(condicion.getCantidad(),condicion.getEvaluacion());
    }

    public static Condicion aModelo(CondicionDTO dto) {
        return new Condicion(dto.cantidad(),dto.evaluacion());
    }
}
