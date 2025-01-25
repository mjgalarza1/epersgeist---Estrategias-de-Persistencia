package ar.edu.unq.epersgeist.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter @AllArgsConstructor
public class ReporteSantuarioMasCorrupto {
    private String nombreDelSantuario;
    private Medium mediumConMasDemonios;
    private Integer cantTotalDemonios;
    private Integer cantDemoniosLibres;


}
