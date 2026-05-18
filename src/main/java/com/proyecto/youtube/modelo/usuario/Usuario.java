package com.proyecto.youtube.modelo.usuario;

import com.proyecto.youtube.servicios.notificaciones.Observador;
import com.proyecto.youtube.servicios.notificaciones.Notificacion;

public abstract class Usuario implements Observador {
    private java.util.UUID id;

    public java.util.UUID getId() {
        return id;
    }

    @Override
    public void actualizar(Notificacion notificacion) {
    }
}