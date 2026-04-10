package com.javadiseno.sanosysalvos.report.dtos.requests;

import lombok.Data;

/** Cuerpo opcional para POST .../resolve (wiki SY-25). */
@Data
public class ResolveReportRequest {

    private String motivo;

    private String nota;
}
