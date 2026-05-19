package com.proyecto.youtube.modelo.contenido;

import com.proyecto.youtube.modelo.usuario.canal.Canal;

public class TransmisionEnVivo extends Contenido {
    private boolean estaActivo;
    private int espectadoresActuales;

    public TransmisionEnVivo(String titulo, String descripcion, Canal canalAutor) {
        super(titulo, descripcion, canalAutor);
        this.estaActivo = true;
        this.espectadoresActuales = 0;
    }

    public void finalizarTransmision() {
        this.estaActivo = false;
        this.espectadoresActuales = 0;
    }

    public void actualizarEspectadores(int cantidad) {
        this.espectadoresActuales = cantidad;
    }

    @Override
    public String toString() {
        String estado = estaActivo ? "• EN VIVO (" + espectadoresActuales + " mirando)" : "Grabación";
        return "Stream: " + super.toString() + " | " + estado;
    }
}
