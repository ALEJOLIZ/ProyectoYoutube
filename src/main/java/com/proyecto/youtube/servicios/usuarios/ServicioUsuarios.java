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

        return Optional.ofNullable(usuariosPorCorreo.get(normalizarCorreo(correo)));
    }

    public boolean existeUsuario(Usuario usuario) {
        return usuario != null && usuariosPorId.containsKey(usuario.getId());
    }

    public boolean existeCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return false;
        }

        return usuariosPorCorreo.containsKey(normalizarCorreo(correo));
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

        if (usuariosPorCorreo.containsKey(correoNormalizado)) {
            throw new DatosUsuarioInvalidosException("Ya existe un usuario con ese correo.");
        }
    }

    private String normalizarCorreo(String correo) {
        return correo.trim().toLowerCase();
    }
}