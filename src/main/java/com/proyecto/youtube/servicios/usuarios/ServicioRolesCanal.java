package com.proyecto.youtube.servicios.usuarios;

import com.proyecto.youtube.modelo.usuario.canal.Canal;
import com.proyecto.youtube.modelo.usuario.canal.RolCanal;
import com.proyecto.youtube.modelo.usuario.canal.TipoRolCanal;
import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.usuario.excepciones.PermisoDenegadoException;
import com.proyecto.youtube.modelo.usuario.excepciones.RolCanalInvalidoException;
import com.proyecto.youtube.modelo.usuario.permisos.PermisoCanalStrategy;
import com.proyecto.youtube.modelo.usuario.permisos.PermisoPropietario;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ServicioRolesCanal {

    private final ServicioUsuarios servicioUsuarios;
    private final ServicioCanales servicioCanales;
    private final Set<RolCanal> rolesAsignados;

    public ServicioRolesCanal(
            ServicioUsuarios servicioUsuarios,
            ServicioCanales servicioCanales
    ) {
        if (servicioUsuarios == null) {
            throw new RolCanalInvalidoException("El servicio de usuarios no puede ser nulo.");
        }

        if (servicioCanales == null) {
            throw new RolCanalInvalidoException("El servicio de canales no puede ser nulo.");
        }

        this.servicioUsuarios = servicioUsuarios;
        this.servicioCanales = servicioCanales;
        this.rolesAsignados = new HashSet<>();
    }

    public RolCanal asignarRol(
            Usuario usuarioAsignado,
            Canal canal,
            PermisoCanalStrategy estrategiaPermisos
    ) {
        validarUsuarioRegistrado(usuarioAsignado);
        validarCanalRegistrado(canal);

        RolCanal nuevoRol = new RolCanal(usuarioAsignado, canal, estrategiaPermisos);

        if (rolesAsignados.contains(nuevoRol)) {
            throw new RolCanalInvalidoException(
                    "El usuario ya tiene un rol asignado sobre este canal."
            );
        }

        rolesAsignados.add(nuevoRol);
        return nuevoRol;
    }

    public RolCanal asignarRolPropietario(Usuario propietario, Canal canal) {
        return asignarRol(propietario, canal, new PermisoPropietario());
    }

    public RolCanal asignarRolComo(
            RolCanal rolEjecutor,
            Usuario usuarioAsignado,
            Canal canal,
            PermisoCanalStrategy estrategiaPermisos
    ) {
        validarRolEjecutor(rolEjecutor);
        validarUsuarioRegistrado(usuarioAsignado);
        validarCanalRegistrado(canal);

        if (!rolEjecutor.getCanal().equals(canal)) {
            throw new PermisoDenegadoException(
                    "El rol ejecutor no pertenece al canal indicado."
            );
        }

        if (!rolEjecutor.puedeAsignarRoles()) {
            throw new PermisoDenegadoException(
                    "El rol ejecutor no tiene permisos para asignar roles."
            );
        }

        return asignarRol(usuarioAsignado, canal, estrategiaPermisos);
    }

    public void removerRol(RolCanal rol) {
        if (rol == null) {
            throw new RolCanalInvalidoException("El rol no puede ser nulo.");
        }

        if (!rolesAsignados.contains(rol)) {
            throw new RolCanalInvalidoException("El rol no existe en el sistema.");
        }

        rol.desactivar();
        rolesAsignados.remove(rol);
    }

    public Optional<RolCanal> obtenerRolDeUsuarioEnCanal(Usuario usuario, Canal canal) {
        validarUsuarioRegistrado(usuario);
        validarCanalRegistrado(canal);

        return rolesAsignados.stream()
                .filter(RolCanal::estaActivo)
                .filter(rol -> rol.getUsuarioAsignado().equals(usuario))
                .filter(rol -> rol.getCanal().equals(canal))
                .findFirst();
    }

    public Set<RolCanal> obtenerRolesDeUsuario(Usuario usuario) {
        validarUsuarioRegistrado(usuario);

        return rolesAsignados.stream()
                .filter(RolCanal::estaActivo)
                .filter(rol -> rol.getUsuarioAsignado().equals(usuario))
                .collect(Collectors.toSet());
    }

    public Set<RolCanal> obtenerRolesDeCanal(Canal canal) {
        validarCanalRegistrado(canal);

        return rolesAsignados.stream()
                .filter(RolCanal::estaActivo)
                .filter(rol -> rol.getCanal().equals(canal))
                .collect(Collectors.toSet());
    }

    public boolean usuarioTieneRol(Usuario usuario, Canal canal, TipoRolCanal tipoRol) {
        validarUsuarioRegistrado(usuario);
        validarCanalRegistrado(canal);

        if (tipoRol == null) {
            throw new RolCanalInvalidoException("El tipo de rol no puede ser nulo.");
        }

        return rolesAsignados.stream()
                .filter(RolCanal::estaActivo)
                .filter(rol -> rol.getUsuarioAsignado().equals(usuario))
                .filter(rol -> rol.getCanal().equals(canal))
                .anyMatch(rol -> rol.getTipoRol() == tipoRol);
    }

    public boolean usuarioPuedeOcultarUsuarios(Usuario usuario, Canal canal) {
        return obtenerRolDeUsuarioEnCanal(usuario, canal)
                .map(RolCanal::puedeOcultarUsuarios)
                .orElse(false);
    }

    public boolean usuarioPuedeMostrarUsuarios(Usuario usuario, Canal canal) {
        return obtenerRolDeUsuarioEnCanal(usuario, canal)
                .map(RolCanal::puedeMostrarUsuarios)
                .orElse(false);
    }

    public boolean usuarioPuedeGestionarContenido(Usuario usuario, Canal canal) {
        return obtenerRolDeUsuarioEnCanal(usuario, canal)
                .map(RolCanal::puedeGestionarContenido)
                .orElse(false);
    }

    public boolean usuarioPuedeAsignarRoles(Usuario usuario, Canal canal) {
        return obtenerRolDeUsuarioEnCanal(usuario, canal)
                .map(RolCanal::puedeAsignarRoles)
                .orElse(false);
    }

    public boolean usuarioPuedeVerMetricas(Usuario usuario, Canal canal) {
        return obtenerRolDeUsuarioEnCanal(usuario, canal)
                .map(RolCanal::puedeVerMetricas)
                .orElse(false);
    }

    public Set<RolCanal> obtenerRolesAsignados() {
        return new HashSet<>(rolesAsignados);
    }

    private void validarUsuarioRegistrado(Usuario usuario) {
        if (!servicioUsuarios.existeUsuario(usuario)) {
            throw new RolCanalInvalidoException("El usuario debe estar registrado.");
        }
    }

    private void validarCanalRegistrado(Canal canal) {
        if (!servicioCanales.existeCanal(canal)) {
            throw new RolCanalInvalidoException("El canal debe estar registrado.");
        }
    }

    private void validarRolEjecutor(RolCanal rolEjecutor) {
        if (rolEjecutor == null) {
            throw new RolCanalInvalidoException("El rol ejecutor no puede ser nulo.");
        }

        if (!rolEjecutor.estaActivo()) {
            throw new PermisoDenegadoException("El rol ejecutor está inactivo.");
        }
    }
}