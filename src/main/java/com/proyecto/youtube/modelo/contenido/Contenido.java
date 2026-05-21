package com.proyecto.youtube.modelo.contenido;
import com.proyecto.youtube.modelo.usuario.canal.Canal;
import com.proyecto.youtube.modelo.interacciones.Interaccion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class Contenido implements Comparable<Contenido> {
    private final UUID id;
    private String titulo;
    private String descripcion;
    private final Canal canalAutor;
    private final LocalDateTime fechaPublicacion;
    private int vistas;
    private List<Interaccion> interacciones;

    public Contenido(String titulo, String descripcion, Canal canalAutor) {
        this.id = UUID.randomUUID();
        this.titulo = Objects.requireNonNull(titulo, "El título no puede ser nulo");
        this.descripcion = descripcion;
        // Validación de la existencia de un canal
        this.canalAutor = Objects.requireNonNull(canalAutor, "Todo contenido debe tener un canal");
        this.fechaPublicacion = LocalDateTime.now();
        this.vistas = 0;
        this.interacciones = new ArrayList<>();
    }

    //Métodos de comportamiento
    public void registrarVista(){
        this.vistas++;
    }

    public void agregarInteracciones(Interaccion interaccion){
        if(interaccion!=null){
            this.interacciones.add(interaccion);
        }
    }
    @Override
    public int compareTo(Contenido otro){
        return Integer.compare(otro.getVistas(), this.vistas);
    }

    //  Getters
    public UUID getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public Canal getCanalAutor() { return canalAutor; }
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public int getVistas() { return vistas; }
    public List<Interaccion> getInteracciones() {
        return new ArrayList<>(interacciones);
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Contenido contenido = (Contenido) o;
        return Objects.equals(id, contenido.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public String toString(){
        return "[" + titulo + "] por " + canalAutor.getNombreCanal() + " | Vistas: " + vistas;
    }
}