package com.proyecto.youtube.modelo.usuario.excepciones;

public class CanalInvalidoException extends RuntimeException {
    public CanalInvalidoException(String message) {
        super(message);
    }
}
