package com.proyecto.youtube.servicios.usuarios;

import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.usuario.excepciones.DatosUsuarioInvalidosException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServicioUsuarios {

    private final Map<UUID, Usuario> usuariosPorId;
    private final Map<String, Usuario> usuariosPorCorreo;

    public ServicioUsuarios() {
        this.usuariosPorId = new HashMap<>();
        this.usuariosPorCorreo = new HashMap<>();
    }

    public Usuario registrarUsuario(String correo) {
        validarCorreoDisponible(correo);

        Usuario usuario = new Usuario(correo);

        usuariosPorId.put(usuario.getId(), usuario);
        usuariosPorCorreo.put(usuario.getCorreo(), usuario);

        return usuario;
    }

    public Optional<Usuario> buscarUsuarioPorId(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(usuariosPorId.get(id));
    }

    public Optional<Usuario> buscarUsuarioPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return Optional.empty();
        }

        String correoNormalizado = normalizarCorreo(correo);

        return usuariosPorId.values()
                .stream()
                .filter(usuario -> usuario.getCorreo().equals(correoNormalizado))
                .findFirst();
    }

    public boolean existeUsuario(Usuario usuario) {
        return usuario != null && usuariosPorId.containsKey(usuario.getId());
    }

    public boolean existeCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return false;
        }

        String correoNormalizado = normalizarCorreo(correo);

        return usuariosPorId.values()
                .stream()
                .anyMatch(usuario -> usuario.getCorreo().equals(correoNormalizado));
    }

    public void actualizarCorreoUsuario(Usuario usuario, String nuevoCorreo) {
        if (!existeUsuario(usuario)) {
            throw new DatosUsuarioInvalidosException("El usuario debe estar registrado.");
        }

        String correoAnterior = usuario.getCorreo();
        String correoNuevoNormalizado = normalizarCorreo(nuevoCorreo);

        if (!correoAnterior.equals(correoNuevoNormalizado) && existeCorreo(correoNuevoNormalizado)) {
            throw new DatosUsuarioInvalidosException("Ya existe un usuario con ese correo.");
        }

        usuariosPorCorreo.remove(correoAnterior);
        usuario.actualizarCorreo(nuevoCorreo);
        usuariosPorCorreo.put(usuario.getCorreo(), usuario);
    }

    public List<Usuario> listarUsuariosOrdenadosPorCorreo() {
        return usuariosPorId.values()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Usuario> listarUsuariosPorFechaRegistroAscendente() {
        return usuariosPorId.values()
                .stream()
                .sorted(Comparator.comparing(Usuario::getFechaRegistro))
                .collect(Collectors.toList());
    }

    public Set<Usuario> obtenerUsuariosRegistrados() {
        return new HashSet<>(usuariosPorId.values());
    }

    public int contarUsuariosRegistrados() {
        return usuariosPorId.size();
    }

    private void validarCorreoDisponible(String correo) {
        if (correo == null || correo.isBlank()) {
            throw new DatosUsuarioInvalidosException("El correo no puede estar vacío.");
        }

        String correoNormalizado = normalizarCorreo(correo);

        if (existeCorreo(correoNormalizado)) {
            throw new DatosUsuarioInvalidosException("Ya existe un usuario con ese correo.");
        }
    }

    private String normalizarCorreo(String correo) {
        return correo.trim().toLowerCase();
    }
}
