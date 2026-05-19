package com.proyecto.youtube.modelo.usuario.excepciones;

public class DatosUsuarioInvalidosException extends RuntimeException {
    public DatosUsuarioInvalidosException(String message) {
        super(message);
    }
}
