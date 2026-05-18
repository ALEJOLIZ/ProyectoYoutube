package com.proyecto.youtube.utilidades;

import java.util.UUID;

public class UsuarioNoEncontradoException extends Exception {

    public UsuarioNoEncontradoException(UUID id) {
        super("Error: El usuario con el ID [" + id + "] no fue encontrado en la base de datos.");
    }

    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}