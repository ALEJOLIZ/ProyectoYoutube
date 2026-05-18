package com.proyecto.youtube.servicios.notificaciones;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notificacion {
    private UUID id;
    private String mensaje;
    private LocalDateTime fecha;

    public Notificacion(String mensaje) {
        this.id = UUID.randomUUID();
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }

    public String getMensaje() { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }
}
