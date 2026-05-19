package com.proyecto.youtube.modelo.contenido;
import com.proyecto.youtube.modelo.usuario.CreadorContenido;

public class VideoLargo extends Contenido{
    private int duracionSegundos;
    private boolean esMonetizado;

    public VideoLargo(String titulo, String descripcion, CreadorContenido autor, int duracion){
        super(titulo, descripcion, autor);
        this.duracionSegundos = duracion;
        this.esMonetizado = false;
    }

    public int getDuracionSegundos() { return duracionSegundos; }

    @Override
    public String toString() {
        return "Video Largo: " + super.toString() + " (" + duracionSegundos + "s)";
    }
}
