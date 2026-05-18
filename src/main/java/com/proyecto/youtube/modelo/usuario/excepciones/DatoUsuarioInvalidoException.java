package com.proyecto.youtube.modelo.usuario.excepciones;

public class DatoUsuarioInvalidoException extends RuntimeException {
    public DatoUsuarioInvalidoException(String message) {
        super(message);
    }
}
