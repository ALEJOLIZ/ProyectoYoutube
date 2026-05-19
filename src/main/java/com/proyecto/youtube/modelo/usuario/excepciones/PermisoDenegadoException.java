package com.proyecto.youtube.modelo.usuario.excepciones;

public class PermisoDenegadoException extends RuntimeException {
    public PermisoDenegadoException(String message) {
        super(message);
    }
}
