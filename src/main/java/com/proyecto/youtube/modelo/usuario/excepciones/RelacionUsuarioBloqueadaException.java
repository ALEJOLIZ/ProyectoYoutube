package com.proyecto.youtube.modelo.usuario.excepciones;

public class RelacionUsuarioBloqueadaException extends RuntimeException {
    public RelacionUsuarioBloqueadaException(String message) {
        super(message);
    }
}
