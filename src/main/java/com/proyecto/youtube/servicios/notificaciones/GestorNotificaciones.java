package com.proyecto.youtube.servicios.notificaciones;

import com.proyecto.youtube.modelo.contenido.Contenido;
import com.proyecto.youtube.modelo.usuario.canal.Canal;

import java.util.ArrayList;
import java.util.List;

public class GestorNotificaciones implements SujetoObservable {

    private final List<Observador> observadoresSistema = new ArrayList<>();

    @Override
    public void suscribir(Observador o) {
        if (o != null && !observadoresSistema.contains(o)) {
            observadoresSistema.add(o);
        }
    }

    @Override
    public void desuscribir(Observador o) {
        observadoresSistema.remove(o);
    }

    @Override
    public void notificar() {
        Notificacion global = new Notificacion("¡Bienvenido! YouTube ha actualizado sus políticas de privacidad.");
        for (Observador obs : observadoresSistema) {
            obs.actualizar(global);
        }
    }

    public void notificarNuevoVideo(Canal canalQuePublica, Contenido nuevoVideo) {
        Notificacion notificacion = new Notificacion(
                "¡" + canalQuePublica.getNombreCanal() + " ha publicado un nuevo video: " + nuevoVideo.getTitulo() + "!"
        );

        if (canalQuePublica.getSuscriptores() != null) {
            for (Canal canalSuscriptor : canalQuePublica.getSuscriptores()) {
                canalSuscriptor.getPropietario().actualizar(notificacion);
            }
        }
    }
}