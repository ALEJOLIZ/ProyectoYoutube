package com.proyecto.youtube.servicios.notificaciones;

import com.proyecto.youtube.modelo.contenido.Contenido;
import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.usuario.canal.Canal;

public class GestorNotificaciones {
    public void notificarNuevoVideo(Canal canalQuePublica, Contenido nuevoVideo) {
        String mensaje = "¡" + canalQuePublica.getNombreCanal() + " ha publicado: " + nuevoVideo.getTitulo() + "!";
        canalQuePublica.notificar(mensaje);
    }
}