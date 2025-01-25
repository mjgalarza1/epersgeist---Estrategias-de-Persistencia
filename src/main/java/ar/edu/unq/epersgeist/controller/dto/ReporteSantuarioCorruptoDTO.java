package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;

public record ReporteSantuarioCorruptoDTO(String nombreDelSantuario, MediumSimpleDTO mediumConMasDemonios, Integer cantTotalDemonios, Integer cantDemoniosLibres) {
    public static ReporteSantuarioCorruptoDTO desdeModelo(ReporteSantuarioMasCorrupto reporte) {
        return new ReporteSantuarioCorruptoDTO(
                reporte.getNombreDelSantuario(),
                reporte.getMediumConMasDemonios() != null ? MediumSimpleDTO.desdeModelo(reporte.getMediumConMasDemonios()) :
                        new MediumSimpleDTO(null, "No hay Mediums en este Santuario"),
                reporte.getCantTotalDemonios(),
                reporte.getCantDemoniosLibres());
    }
}

