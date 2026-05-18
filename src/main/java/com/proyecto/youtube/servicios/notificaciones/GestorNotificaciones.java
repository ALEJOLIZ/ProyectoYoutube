package com.proyecto.youtube.servicios.notificaciones;

import java.util.ArrayList;
import java.util.List;

public class GestorNotificaciones implements SujetoObservable {
    private List<Observador> suscriptores = new ArrayList<>();

    @Override
    public void suscribir(Observador obs) {
        if (!suscriptores.contains(obs)) {
            suscriptores.add(obs);
        }
    }

    @Override
    public void desuscribir(Observador obs) {
        suscriptores.remove(obs);
    }

    @Override
    public void notificar(Notificacion notificacion) {
        for (Observador obs : suscriptores) {
            obs.actualizar(notificacion);
        }
    }
}
