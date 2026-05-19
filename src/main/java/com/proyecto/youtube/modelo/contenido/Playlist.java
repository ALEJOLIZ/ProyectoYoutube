package com.proyecto.youtube.modelo.contenido;

import com.proyecto.youtube.modelo.usuario.CreadorContenido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {
    private String nombre;
    private final CreadorContenido propietario;
    private List<Contenido> videos;

    public Playlist(String nombre, CreadorContenido propietario){
        this.nombre = nombre;
        this.propietario = propietario;
        this.videos = new ArrayList<>();
    }

    public void agregarVideo(Contenido video) {
        if (video != null) {
            videos.add(video);
        }
    }

    public void organizarPorVistas() {
        Collections.sort(videos);
    }

    public List<Contenido> getVideos() {
        return new ArrayList<>(videos);
    }

}
