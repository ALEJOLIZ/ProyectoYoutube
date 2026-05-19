package com.proyecto.youtube.modelo.usuario;

import com.proyecto.youtube.modelo.usuario.excepciones.CanalInvalidoException;
import com.proyecto.youtube.modelo.usuario.excepciones.DatosUsuarioInvalidosException;
import com.proyecto.youtube.servicios.notificaciones.Notificacion;
import com.proyecto.youtube.servicios.notificaciones.Observador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Usuario implements Comparable<Usuario>, Observador {

    private final UUID id;
    private String correo;
    private final LocalDateTime fechaRegistro;
    private Canal canalPropio;
    private final List<Notificacion> historialNotificaciones;

    public Usuario(String correo) {
        validarCorreo(correo);

        this.id = UUID.randomUUID();
        this.correo = correo.trim().toLowerCase();
        this.fechaRegistro = LocalDateTime.now();
        this.canalPropio = null;
        this.historialNotificaciones = new ArrayList<>();
    }

    @Override
    public void actualizar(Notificacion notificacion) {
        if (notificacion == null) {
            return;
        }

        historialNotificaciones.add(notificacion);
    }

    public List<Notificacion> getHistorialNotificaciones() {
        return Collections.unmodifiableList(historialNotificaciones);
    }

    public void asignarCanalPropio(Canal canal) {
        if (canal == null) {
            throw new CanalInvalidoException("El canal no puede ser nulo.");
        }

        if (this.canalPropio != null) {
            throw new CanalInvalidoException("El usuario ya tiene un canal propio asignado.");
        }

        if (!canal.getPropietario().equals(this)) {
            throw new CanalInvalidoException("El canal no pertenece a este usuario.");
        }

        this.canalPropio = canal;
    }

    public void actualizarCorreo(String nuevoCorreo) {
        validarCorreo(nuevoCorreo);
        this.correo = nuevoCorreo.trim().toLowerCase();
    }

    private void validarCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            throw new DatosUsuarioInvalidosException("El correo no puede estar vacío.");
        }

        if (!correo.contains("@")) {
            throw new DatosUsuarioInvalidosException("El correo no tiene un formato válido.");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getCorreo() {
        return correo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public Canal getCanalPropio() {
        return canalPropio;
    }

    @Override
    public int compareTo(Usuario otro) {
        if (otro == null) {
            return 1;
        }

        return this.correo.compareToIgnoreCase(otro.correo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Usuario)) return false;

        Usuario otro = (Usuario) obj;
        return Objects.equals(this.id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", correo='" + correo + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}