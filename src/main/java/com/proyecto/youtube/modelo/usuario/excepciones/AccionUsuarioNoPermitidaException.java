package com.proyecto.youtube.modelo.usuario.excepciones;

public class AccionUsuarioNoPermitidaException extends RuntimeException {
    public AccionUsuarioNoPermitidaException(String message) {
        super(message);
    }
}
