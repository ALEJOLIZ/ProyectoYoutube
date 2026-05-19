package com.proyecto.youtube.servicios.usuarios;

import com.proyecto.youtube.modelo.usuario.canal.Canal;
import com.proyecto.youtube.modelo.usuario.canal.RolCanal;
import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.usuario.excepciones.CanalInvalidoException;
import com.proyecto.youtube.modelo.usuario.excepciones.PermisoDenegadoException;
import com.proyecto.youtube.modelo.usuario.excepciones.RolCanalInvalidoException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServicioCanales {

    private final ServicioUsuarios servicioUsuarios;
    private final Map<UUID, Canal> canalesPorId;
    private final Map<String, Canal> canalesPorNombre;

    public ServicioCanales(ServicioUsuarios servicioUsuarios) {
        if (servicioUsuarios == null) {
            throw new CanalInvalidoException("El servicio de usuarios no puede ser nulo.");
        }

        this.servicioUsuarios = servicioUsuarios;
        this.canalesPorId = new HashMap<>();
        this.canalesPorNombre = new HashMap<>();
    }

    public Canal crearCanal(Usuario propietario, String nombreCanal, String descripcion) {
        validarUsuarioRegistrado(propietario);
        validarNombreCanalDisponible(nombreCanal);

        Canal canal = new Canal(nombreCanal, descripcion, propietario);
        propietario.asignarCanalPropio(canal);

        canalesPorId.put(canal.getId(), canal);
        canalesPorNombre.put(normalizarNombreCanal(canal.getNombreCanal()), canal);

        return canal;
    }

    public Optional<Canal> buscarCanalPorId(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(canalesPorId.get(id));
    }

    public Optional<Canal> buscarCanalPorNombre(String nombreCanal) {
        if (nombreCanal == null || nombreCanal.isBlank()) {
            return Optional.empty();
        }

        String nombreNormalizado = normalizarNombreCanal(nombreCanal);

        return canalesPorId.values()
                .stream()
                .filter(canal -> normalizarNombreCanal(canal.getNombreCanal()).equals(nombreNormalizado))
                .findFirst();
    }

    public boolean existeCanal(Canal canal) {
        return canal != null && canalesPorId.containsKey(canal.getId());
    }

    public boolean existeNombreCanal(String nombreCanal) {
        if (nombreCanal == null || nombreCanal.isBlank()) {
            return false;
        }

        String nombreNormalizado = normalizarNombreCanal(nombreCanal);

        return canalesPorId.values()
                .stream()
                .anyMatch(canal -> normalizarNombreCanal(canal.getNombreCanal()).equals(nombreNormalizado));
    }

    public void actualizarNombreCanal(Canal canal, String nuevoNombre) {
        validarCanalRegistrado(canal);

        String nombreAnterior = normalizarNombreCanal(canal.getNombreCanal());
        String nombreNuevoNormalizado = normalizarNombreCanal(nuevoNombre);

        if (!nombreAnterior.equals(nombreNuevoNormalizado) && existeNombreCanal(nombreNuevoNormalizado)) {
            throw new CanalInvalidoException("Ya existe un canal con ese nombre.");
        }

        canalesPorNombre.remove(nombreAnterior);
        canal.actualizarNombre(nuevoNombre);
        canalesPorNombre.put(normalizarNombreCanal(canal.getNombreCanal()), canal);
    }

    public void suscribirCanales(Canal canalSuscriptor, Canal canalObjetivo) {
        validarCanalRegistrado(canalSuscriptor);
        validarCanalRegistrado(canalObjetivo);

        canalSuscriptor.suscribirseA(canalObjetivo);
    }

    public void cancelarSuscripcion(Canal canalSuscriptor, Canal canalObjetivo) {
        validarCanalRegistrado(canalSuscriptor);
        validarCanalRegistrado(canalObjetivo);

        canalSuscriptor.cancelarSuscripcion(canalObjetivo);
    }

    public boolean estaSuscrito(Canal canalSuscriptor, Canal canalObjetivo) {
        validarCanalRegistrado(canalSuscriptor);
        validarCanalRegistrado(canalObjetivo);

        return canalSuscriptor.estaSuscritoA(canalObjetivo);
    }

    public Set<Canal> obtenerSuscriptoresDe(Canal canal) {
        validarCanalRegistrado(canal);
        return canal.getSuscriptores();
    }

    public Set<Canal> obtenerSuscripcionesDe(Canal canal) {
        validarCanalRegistrado(canal);
        return canal.getSuscripciones();
    }

    public void ocultarCanalEnCanal(RolCanal rolEjecutor, Canal canalAOcultar) {
        validarRolEjecutor(rolEjecutor);
        validarCanalRegistrado(canalAOcultar);

        if (!rolEjecutor.puedeOcultarUsuarios()) {
            throw new PermisoDenegadoException(
                    "El rol no tiene permisos para ocultar canales."
            );
        }

        Canal canalAfectado = rolEjecutor.getCanal();
        validarCanalRegistrado(canalAfectado);

        canalAfectado.ocultarCanal(canalAOcultar);
    }

    public void mostrarCanalEnCanal(RolCanal rolEjecutor, Canal canalAMostrar) {
        validarRolEjecutor(rolEjecutor);
        validarCanalRegistrado(canalAMostrar);

        if (!rolEjecutor.puedeMostrarUsuarios()) {
            throw new PermisoDenegadoException(
                    "El rol no tiene permisos para mostrar canales ocultos."
            );
        }

        Canal canalAfectado = rolEjecutor.getCanal();
        validarCanalRegistrado(canalAfectado);

        canalAfectado.mostrarCanal(canalAMostrar);
    }

    public boolean canalEstaOcultoEn(Canal canalAfectado, Canal canalConsultado) {
        validarCanalRegistrado(canalAfectado);
        validarCanalRegistrado(canalConsultado);

        return canalAfectado.estaOculto(canalConsultado);
    }

    public List<Canal> listarCanalesOrdenadosPorNombre() {
        return canalesPorId.values()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Set<Canal> obtenerCanalesRegistrados() {
        return new HashSet<>(canalesPorId.values());
    }

    public int contarCanalesRegistrados() {
        return canalesPorId.size();
    }

    private void validarUsuarioRegistrado(Usuario usuario) {
        if (!servicioUsuarios.existeUsuario(usuario)) {
            throw new CanalInvalidoException("El propietario debe ser un usuario registrado.");
        }
    }

    private void validarCanalRegistrado(Canal canal) {
        if (canal == null) {
            throw new CanalInvalidoException("El canal no puede ser nulo.");
        }

        if (!canalesPorId.containsKey(canal.getId())) {
            throw new CanalInvalidoException("El canal no está registrado en el sistema.");
        }
    }

    private void validarNombreCanalDisponible(String nombreCanal) {
        if (nombreCanal == null || nombreCanal.isBlank()) {
            throw new CanalInvalidoException("El nombre del canal no puede estar vacío.");
        }

        String nombreNormalizado = normalizarNombreCanal(nombreCanal);

        if (existeNombreCanal(nombreNormalizado)) {
            throw new CanalInvalidoException("Ya existe un canal con ese nombre.");
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

    private String normalizarNombreCanal(String nombreCanal) {
        return nombreCanal.trim().toLowerCase();
    }
}
