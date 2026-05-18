package com.proyecto.youtube.utilidades;

import java.util.UUID;

public class ContenidoNoEncontradoException extends Exception {

    public ContenidoNoEncontradoException(UUID id) {
        super("Error: El contenido multimedia con ID [" + id + "] ha sido eliminado o no existe.");
    }

    public ContenidoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}