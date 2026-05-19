package com.proyecto.youtube.servicios.notificaciones;

public interface SujetoObservable {
    void suscribir(Observador obs);
    void desuscribir(Observador obs);
    void notificar();
}
