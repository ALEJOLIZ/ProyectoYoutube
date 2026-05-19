package com.proyecto.youtube.modelo.interacciones;

import com.proyecto.youtube.modelo.usuario.Usuario;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;

public abstract class Interaccion {
    private final UUID id;
    private final Usuario autor;
    private final LocalDateTime fechaCreacion;

    public Interaccion(Usuario autor) {
        this.id = UUID.randomUUID();
        this.autor = Objects.requireNonNull(autor, "El autor no puede ser nulo");
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public Usuario getAutor() { return autor; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interaccion that = (Interaccion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}