package com.proyecto.youtube.modelo.contenido;

import com.proyecto.youtube.modelo.usuario.canal.Canal;

public class VideoLargo extends Contenido{
    private int duracionSegundos;
    private boolean esMonetizado;

    public VideoLargo(String titulo, String descripcion, Canal canalAutor, int duracion, boolean monetizado){
        super(titulo, descripcion, canalAutor);
        this.duracionSegundos = duracion;
        this.esMonetizado = monetizado;
    }

    public int getDuracionSegundos() { return duracionSegundos; }
    public boolean isEsMonetizado() { return esMonetizado; }

    @Override
    public String toString() {
        return "Video Largo: " + super.toString() + " (" + duracionSegundos + "s)";
    }
}
