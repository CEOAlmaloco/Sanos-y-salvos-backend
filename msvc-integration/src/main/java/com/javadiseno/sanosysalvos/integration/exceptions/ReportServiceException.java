package com.javadiseno.sanosysalvos.integration.exceptions;

public class ReportServiceException extends RuntimeException {
  private final int upstreamStatus;

  public ReportServiceException(int upstreamStatus, String body) {
        super("Error al llamar a Report Service [" + upstreamStatus + "]: " + body);
        this.upstreamStatus = upstreamStatus;
    }

    public int getUpstreamStatus() {
      return upstreamStatus;
    }
}
