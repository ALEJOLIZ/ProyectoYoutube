package com.proyecto.youtube.modelo.usuario.permisos;

import com.proyecto.youtube.modelo.usuario.canal.TipoRolCanal;

public class PermisoModeradorGestor implements PermisoCanalStrategy {

    @Override
    public TipoRolCanal getTipoRol() {
        return TipoRolCanal.MODERADOR_GESTOR;
    }

    @Override
    public boolean puedeOcultarUsuarios() {
        return true;
    }

    @Override
    public boolean puedeMostrarUsuarios() {
        return true;
    }

    @Override
    public boolean puedeEliminarComentarios() {
        return true;
    }

    @Override
    public boolean puedeGestionarContenido() {
        return false;
    }

    @Override
    public boolean puedeAsignarRoles() {
        return false;
    }

    @Override
    public boolean puedeVerMetricas() {
        return true;
    }
}