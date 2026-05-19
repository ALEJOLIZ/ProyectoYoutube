package com.proyecto.youtube.modelo.usuario.canal;

import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.usuario.excepciones.CanalInvalidoException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Canal implements Comparable<Canal> {

    private final UUID id;
    private String nombreCanal;
    private String descripcion;
    private final Usuario propietario;
    private final LocalDateTime fechaCreacion;

    private final Set<Canal> suscripciones;
    private final Set<Canal> suscriptores;
    private final Set<Canal> canalesOcultos;

    public Canal(String nombreCanal, String descripcion, Usuario propietario) {
        validarNombreCanal(nombreCanal);

        if (propietario == null) {
            throw new CanalInvalidoException("El propietario del canal no puede ser nulo.");
        }

        this.id = UUID.randomUUID();
        this.nombreCanal = nombreCanal.trim();
        this.descripcion = descripcion == null ? "" : descripcion.trim();
        this.propietario = propietario;
        this.fechaCreacion = LocalDateTime.now();

        this.suscripciones = new HashSet<>();
        this.suscriptores = new HashSet<>();
        this.canalesOcultos = new HashSet<>();
    }

    public void actualizarNombre(String nuevoNombre) {
        validarNombreCanal(nuevoNombre);
        this.nombreCanal = nuevoNombre.trim();
    }

    public void actualizarDescripcion(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion == null ? "" : nuevaDescripcion.trim();
    }

    public void suscribirseA(Canal canalObjetivo) {
        validarCanalRelacionado(canalObjetivo, "No se puede suscribir a un canal nulo.");

        if (this.equals(canalObjetivo)) {
            throw new CanalInvalidoException("Un canal no puede suscribirse a sí mismo.");
        }

        boolean agregado = this.suscripciones.add(canalObjetivo);

        if (agregado) {
            canalObjetivo.suscriptores.add(this);
        }
    }

    public void cancelarSuscripcion(Canal canalObjetivo) {
        validarCanalRelacionado(canalObjetivo, "No se puede cancelar una suscripción a un canal nulo.");

        boolean eliminado = this.suscripciones.remove(canalObjetivo);

        if (eliminado) {
            canalObjetivo.suscriptores.remove(this);
        }
    }

    public boolean estaSuscritoA(Canal canalObjetivo) {
        validarCanalRelacionado(canalObjetivo, "El canal objetivo no puede ser nulo.");
        return this.suscripciones.contains(canalObjetivo);
    }

    public void ocultarCanal(Canal canalAOcultar) {
        validarCanalRelacionado(canalAOcultar, "No se puede ocultar un canal nulo.");

        if (this.equals(canalAOcultar)) {
            throw new CanalInvalidoException("Un canal no puede ocultarse a sí mismo.");
        }

        this.canalesOcultos.add(canalAOcultar);
    }

    public void mostrarCanal(Canal canalAMostrar) {
        validarCanalRelacionado(canalAMostrar, "No se puede mostrar un canal nulo.");
        this.canalesOcultos.remove(canalAMostrar);
    }

    public boolean estaOculto(Canal canal) {
        validarCanalRelacionado(canal, "El canal no puede ser nulo.");
        return this.canalesOcultos.contains(canal);
    }

    public int obtenerCantidadSuscriptores() {
        return suscriptores.size();
    }

    public int obtenerCantidadSuscripciones() {
        return suscripciones.size();
    }

    private void validarNombreCanal(String nombreCanal) {
        if (nombreCanal == null || nombreCanal.isBlank()) {
            throw new CanalInvalidoException("El nombre del canal no puede estar vacío.");
        }
    }

    private void validarCanalRelacionado(Canal canal, String mensaje) {
        if (canal == null) {
            throw new CanalInvalidoException(mensaje);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Set<Canal> getSuscripciones() {
        return Collections.unmodifiableSet(suscripciones);
    }

    public Set<Canal> getSuscriptores() {
        return Collections.unmodifiableSet(suscriptores);
    }

    public Set<Canal> getCanalesOcultos() {
        return Collections.unmodifiableSet(canalesOcultos);
    }

    @Override
    public int compareTo(Canal otro) {
        if (otro == null) {
            return 1;
        }

        return this.nombreCanal.compareToIgnoreCase(otro.nombreCanal);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Canal)) return false;

        Canal otro = (Canal) obj;
        return Objects.equals(this.id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Canal{" +
                "id=" + id +
                ", nombreCanal='" + nombreCanal + '\'' +
                ", propietario=" + propietario.getCorreo() +
                ", suscriptores=" + suscriptores.size() +
                ", suscripciones=" + suscripciones.size() +
                '}';
    }
}
