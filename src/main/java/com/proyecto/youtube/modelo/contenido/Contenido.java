package com.proyecto.youtube.modelo.contenido;

import com.proyecto.youtube.modelo.usuario.CreadorContenido;

public abstract class Contenido {
    private java.util.UUID id;
    private CreadorContenido autor;

    public java.util.UUID getId() {
        return id;
    }
}