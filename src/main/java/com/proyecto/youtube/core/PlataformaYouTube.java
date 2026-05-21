package com.proyecto.youtube.core;

import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.usuario.canal.Canal;
import com.proyecto.youtube.modelo.contenido.Contenido;
import com.proyecto.youtube.modelo.usuario.canal.RolCanal;
import com.proyecto.youtube.servicios.usuarios.ServicioUsuarios;
import com.proyecto.youtube.servicios.usuarios.ServicioCanales;
import com.proyecto.youtube.servicios.usuarios.ServicioRolesCanal;
import com.proyecto.youtube.servicios.notificaciones.GestorNotificaciones;
import com.proyecto.youtube.servicios.recomendaciones.EstrategiaRecomendacion;
import com.proyecto.youtube.servicios.fabricas.CreadorContenidoFactory;

import java.util.*;

public class PlataformaYouTube {

    private final Map<UUID, Usuario> registroUsuarios;
    private final Map<UUID, Canal> registroCanales;
    private final List<Contenido> baseDatosContenido;
    private final List<RolCanal> registroRoles;

    private final GestorNotificaciones gestorNotificaciones;
    private EstrategiaRecomendacion algoritmoActual;

    private final ServicioUsuarios servicioUsuarios;
    private final ServicioCanales servicioCanales;
    private final ServicioRolesCanal servicioRolesCanal;
    private final CreadorContenidoFactory fabricaContenido;

    public PlataformaYouTube(EstrategiaRecomendacion algoritmoInicial) {
        this.registroUsuarios = new HashMap<>();
        this.registroCanales = new HashMap<>();
        this.baseDatosContenido = new ArrayList<>();
        this.registroRoles = new ArrayList<>();

        this.gestorNotificaciones = new GestorNotificaciones();
        this.algoritmoActual = algoritmoInicial;

        this.servicioUsuarios = new ServicioUsuarios();
        this.servicioCanales = new ServicioCanales(this.servicioUsuarios);
        this.servicioRolesCanal = new ServicioRolesCanal(this.servicioUsuarios, this.servicioCanales);
        this.fabricaContenido = new CreadorContenidoFactory();
    }

    public UUID registrarUsuario(String nombre, String correo) {
        Usuario nuevoUsuario = servicioUsuarios.registrarUsuario(correo);
        registroUsuarios.put(nuevoUsuario.getId(), nuevoUsuario);
        gestorNotificaciones.suscribir(nuevoUsuario);
        System.out.println("Usuario registrado: " + nombre + " (" + correo + ")");
        return nuevoUsuario.getId();
    }

    public UUID crearCanal(UUID usuarioId, String nombreCanal) {
        Usuario propietario = registroUsuarios.get(usuarioId);
        if (propietario == null) throw new IllegalArgumentException("Usuario no encontrado.");

        Canal nuevoCanal = servicioCanales.crearCanal(propietario, nombreCanal, "Canal de " + nombreCanal);
        registroCanales.put(nuevoCanal.getId(), nuevoCanal);
        System.out.println("Canal '" + nombreCanal + "' creado con éxito.");
        return nuevoCanal.getId();
    }

    public void suscribirUsuarioACanal(UUID usuarioId, UUID canalId) {
        Usuario usuario = registroUsuarios.get(usuarioId);
        Canal canalDestino = registroCanales.get(canalId);
        if (usuario != null && canalDestino != null && usuario.getCanalPropio() != null) {
            usuario.getCanalPropio().suscribirseA(canalDestino);
            System.out.println("Suscripción exitosa a " + canalDestino.getNombreCanal());
        }
    }

    public void modificarMetadatosCanal(UUID canalId, String nuevaDescripcion) {
        Canal canal = obtenerCanal(canalId);
        if (canal != null) {
            canal.actualizarDescripcion(nuevaDescripcion);
            System.out.println("Descripción del canal '" + canal.getNombreCanal() + "' actualizada.");
        }
    }

    public void actualizarPerfilCompleto(UUID usuarioId, String nuevoCorreo, String nuevoNombreCanal) {
        Usuario usuario = registroUsuarios.get(usuarioId);
        if (usuario != null) {
            servicioUsuarios.actualizarCorreoUsuario(usuario, nuevoCorreo);
            if (usuario.getCanalPropio() != null) {
                servicioCanales.actualizarNombreCanal(usuario.getCanalPropio(), nuevoNombreCanal);
            }
            System.out.println("Perfil de usuario actualizado exitosamente.");
        }
    }

    public void gestionarSuscripcion(UUID idSuscriptor, UUID idObjetivo, boolean suscribir) {
        Canal suscriptor = obtenerCanal(idSuscriptor);
        Canal objetivo = obtenerCanal(idObjetivo);

        if (suscriptor != null && objetivo != null) {
            if (suscribir) servicioCanales.suscribirCanales(suscriptor, objetivo);
            else servicioCanales.cancelarSuscripcion(suscriptor, objetivo);

            boolean estado = servicioCanales.estaSuscrito(suscriptor, objetivo);
            int subsCanal = servicioCanales.obtenerSuscriptoresDe(objetivo).size();
            int seguidos = servicioCanales.obtenerSuscripcionesDe(suscriptor).size();
            System.out.println("Estado de suscripción: " + estado + " | Subs del objetivo: " + subsCanal + " | Suscripciones del usuario: " + seguidos);
        }
    }

    public Canal obtenerCanal(UUID canalId) { return registroCanales.get(canalId); }

}