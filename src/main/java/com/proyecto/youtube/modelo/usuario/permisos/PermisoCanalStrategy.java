package com.proyecto.youtube.modelo.usuario.permisos;

import com.proyecto.youtube.modelo.usuario.canal.TipoRolCanal;

public interface PermisoCanalStrategy {

    TipoRolCanal getTipoRol();

    boolean puedeOcultarUsuarios();

    boolean puedeMostrarUsuarios();

    boolean puedeEliminarComentarios();

    boolean puedeGestionarContenido();

    boolean puedeAsignarRoles();

    boolean puedeVerMetricas();
}