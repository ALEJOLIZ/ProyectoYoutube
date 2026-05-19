package com.proyecto.youtube.modelo.usuario;

import com.proyecto.youtube.modelo.usuario.excepciones.PermisoDenegadoException;
import com.proyecto.youtube.modelo.usuario.excepciones.RolCanalInvalidoException;
import com.proyecto.youtube.modelo.usuario.permisos.PermisoCanalStrategy;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class RolCanal {

    private final UUID id;
    private final Usuario usuarioAsignado;
    private final Canal canal;
    private final PermisoCanalStrategy estrategiaPermisos;
    private final LocalDateTime fechaAsignacion;
    private boolean activo;

    public RolCanal(Usuario usuarioAsignado, Canal canal, PermisoCanalStrategy estrategiaPermisos) {
        validarRol(usuarioAsignado, canal, estrategiaPermisos);

        this.id = UUID.randomUUID();
        this.usuarioAsignado = usuarioAsignado;
        this.canal = canal;
        this.estrategiaPermisos = estrategiaPermisos;
        this.fechaAsignacion = LocalDateTime.now();
        this.activo = true;
    }

    private void validarRol(Usuario usuarioAsignado, Canal canal, PermisoCanalStrategy estrategiaPermisos) {
        if (usuarioAsignado == null) {
            throw new RolCanalInvalidoException("El usuario asignado no puede ser nulo.");
        }

        if (canal == null) {
            throw new RolCanalInvalidoException("El canal no puede ser nulo.");
        }

        if (estrategiaPermisos == null) {
            throw new RolCanalInvalidoException("La estrategia de permisos no puede ser nula.");
        }

        if (estrategiaPermisos.getTipoRol() == TipoRolCanal.PROPIETARIO
                && !canal.getPropietario().equals(usuarioAsignado)) {
            throw new RolCanalInvalidoException(
                    "Solo el propietario real del canal puede tener rol PROPIETARIO."
            );
        }
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }

    public boolean estaActivo() {
        return activo;
    }

    public TipoRolCanal getTipoRol() {
        return estrategiaPermisos.getTipoRol();
    }

    public boolean puedeOcultarUsuarios() {
        verificarRolActivo();
        return estrategiaPermisos.puedeOcultarUsuarios();
    }

    public boolean puedeMostrarUsuarios() {
        verificarRolActivo();
        return estrategiaPermisos.puedeMostrarUsuarios();
    }

    public boolean puedeEliminarComentarios() {
        verificarRolActivo();
        return estrategiaPermisos.puedeEliminarComentarios();
    }

    public boolean puedeGestionarContenido() {
        verificarRolActivo();
        return estrategiaPermisos.puedeGestionarContenido();
    }

    public boolean puedeAsignarRoles() {
        verificarRolActivo();
        return estrategiaPermisos.puedeAsignarRoles();
    }

    public boolean puedeVerMetricas() {
        verificarRolActivo();
        return estrategiaPermisos.puedeVerMetricas();
    }

    private void verificarRolActivo() {
        if (!activo) {
            throw new PermisoDenegadoException("El rol está inactivo.");
        }
    }

    public UUID getId() {
        return id;
    }

    public Usuario getUsuarioAsignado() {
        return usuarioAsignado;
    }

    public Canal getCanal() {
        return canal;
    }

    public PermisoCanalStrategy getEstrategiaPermisos() {
        return estrategiaPermisos;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RolCanal)) return false;

        RolCanal otro = (RolCanal) obj;

        return Objects.equals(this.usuarioAsignado, otro.usuarioAsignado)
                && Objects.equals(this.canal, otro.canal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioAsignado, canal);
    }

    @Override
    public String toString() {
        return "RolCanal{" +
                "id=" + id +
                ", usuario=" + usuarioAsignado.getCorreo() +
                ", canal=" + canal.getNombreCanal() +
                ", tipoRol=" + getTipoRol() +
                ", activo=" + activo +
                '}';
    }
}