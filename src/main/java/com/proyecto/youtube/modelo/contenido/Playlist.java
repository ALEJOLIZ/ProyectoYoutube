package com.proyecto.youtube.modelo.contenido;

import com.proyecto.youtube.modelo.usuario.canal.Canal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {
    private String nombre;
    private final Canal canalPropietario;
    private List<Contenido> videos;

    public Playlist(String nombre, Canal canalPropietario){
        this.nombre = nombre;
        this.canalPropietario = canalPropietario;
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

    public String getNombre() { return nombre; }
    public Canal getCanalPropietario() { return canalPropietario; }
    public List<Contenido> getVideos() {
        return new ArrayList<>(videos);
    }

}
