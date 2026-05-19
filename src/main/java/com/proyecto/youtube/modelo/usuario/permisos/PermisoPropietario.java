package com.proyecto.youtube.modelo.usuario.permisos;

import com.proyecto.youtube.modelo.usuario.TipoRolCanal;

public class PermisoPropietario implements PermisoCanalStrategy {

    @Override
    public TipoRolCanal getTipoRol() {
        return TipoRolCanal.PROPIETARIO;
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
        return true;
    }

    @Override
    public boolean puedeAsignarRoles() {
        return true;
    }

    @Override
    public boolean puedeVerMetricas() {
        return true;
    }
}