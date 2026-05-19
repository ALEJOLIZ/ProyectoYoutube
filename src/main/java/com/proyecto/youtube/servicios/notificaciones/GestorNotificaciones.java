package com.proyecto.youtube.servicios.notificaciones;

import com.proyecto.youtube.modelo.contenido.Contenido;
import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.usuario.canal.Canal;

public class GestorNotificaciones implements SujetoObservable {

    @Override
    public void suscribir(Observador o) {
        // En la nueva arquitectura, las suscripciones se añaden y validan en el modelo Canal.
    }

    @Override
    public void desuscribir(Observador o) {
        // En la nueva arquitectura, las cancelaciones se manejan en el modelo Canal.
    }

    @Override
    public void notificar() {
        // Método base genérico. En su lugar se utiliza la sobrecarga específica de abajo.
    }

    public void notificarNuevoVideo(Canal canalQuePublica, Contenido nuevoVideo) {
        Notificacion notificacion = new Notificacion(
                "¡" + canalQuePublica.getNombreCanal() + " ha publicado un nuevo video: " + nuevoVideo.getTitulo() + "!"
        );

        if (canalQuePublica.getSuscriptores() != null) {
            for (Canal suscriptor : canalQuePublica.getSuscriptores()) {
                suscriptor.actualizarNombre(String.valueOf(notificacion));
            }
        }
    }
}