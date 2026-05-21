package com.proyecto.youtube.core;

import com.proyecto.youtube.modelo.contenido.TransmisionEnVivo;
import com.proyecto.youtube.modelo.contenido.VideoLargo;
import com.proyecto.youtube.modelo.contenido.Short;
import com.proyecto.youtube.modelo.interacciones.Comentario;
import com.proyecto.youtube.modelo.interacciones.Interaccion;
import com.proyecto.youtube.modelo.interacciones.Reaccion;
import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.usuario.canal.Canal;
import com.proyecto.youtube.modelo.contenido.Contenido;
import com.proyecto.youtube.modelo.usuario.canal.RolCanal;
import com.proyecto.youtube.modelo.usuario.excepciones.ContenidoNoEncontradoException;
import com.proyecto.youtube.modelo.usuario.excepciones.PermisoDenegadoException;
import com.proyecto.youtube.modelo.usuario.permisos.PermisoCanalStrategy;
import com.proyecto.youtube.modelo.usuario.permisos.PermisoModeradorGestor;
import com.proyecto.youtube.servicios.fabricas.TipoContenido;
import com.proyecto.youtube.servicios.fabricas.TipoContenidoNoReconocible;
import com.proyecto.youtube.modelo.interacciones.TipoReaccion;
import com.proyecto.youtube.servicios.notificaciones.Notificacion;
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

    public RolCanal asignarRolModeracion(UUID usuarioId, UUID canalId, PermisoCanalStrategy estrategia) {
        Usuario moderador = registroUsuarios.get(usuarioId);
        Canal canal = obtenerCanal(canalId);

        if (moderador == null || canal == null) throw new IllegalArgumentException("Usuario o Canal inválido.");

        // Instancia y registra el RolCanal consumiendo las clases de estrategias de permisos
        RolCanal nuevoRol = new RolCanal(moderador, canal, estrategia);
        registroRoles.add(nuevoRol);
        System.out.println("Rol " + estrategia.getTipoRol() + " asignado a " + moderador.getCorreo() + " en el canal " + canal.getNombreCanal());
        return nuevoRol;
    }

    public void simularModeracionComentario(RolCanal rol, String contextoOperacion) {

        System.out.println("\n--- Ejecutando Auditoría de Permisos ---");
        System.out.println("ID Asignación: " + rol.getId());
        System.out.println("Fecha de Asignación: " + rol.getFechaAsignacion().toLocalDate());
        System.out.println("Estrategia activa: " + rol.getEstrategiaPermisos().getClass().getSimpleName());

        rol.desactivar();
        System.out.println("Estado temporal del rol cambiado a Activo: " + rol.estaActivo());
        rol.activar();

        if (rol.puedeEliminarComentarios()) {
            System.out.println("Acción permitida: El moderador " + rol.getUsuarioAsignado().getCorreo() + " eliminó un comentario inapropiado (" + contextoOperacion + ").");
        } else {
            System.out.println("Acción denegada: Permisos insuficientes para eliminar comentarios.");
        }
    }

    public void imprimirPanelAdministrativoCanal(UUID canalId) {
        Canal canal = obtenerCanal(canalId);
        if (canal == null) return;

        System.out.println("\n=== PANEL ADMINISTRATIVO DE CANAL ===");
        System.out.println("Canal: " + canal.getNombreCanal());
        System.out.println("Descripción del Sistema: " + canal.getDescripcion());
        System.out.println("Fecha de Apertura: " + canal.getFechaCreacion());
        System.out.println("Propietario Principal: " + canal.getPropietario().getCorreo());
        System.out.println("Métrica de Suscriptores (Contador): " + canal.obtenerCantidadSuscriptores());
        System.out.println("Métrica de Suscripciones realizadas: " + canal.obtenerCantidadSuscripciones());
        System.out.println("Cantidad de canales en lista negra: " + canal.getCanalesOcultos().size());
        System.out.println("========================================");
    }

    public void revisarBandejaNotificacionesUsuario(UUID usuarioId) {
        Usuario usuario = registroUsuarios.get(usuarioId);
        if (usuario == null) return;

        System.out.println("\nHistorial de Notificaciones de: " + usuario.getCorreo());
        System.out.println("Miembro de la plataforma desde: " + usuario.getFechaRegistro().toLocalDate());

        var alertas = usuario.getHistorialNotificaciones();
        if (alertas.isEmpty()) {
            System.out.println("  -> Tu bandeja de entrada está limpia.");
        } else {
            for (Notificacion n : alertas) {
                System.out.println("  * [ID: " + n.getId().toString().substring(0,8) + " | " + n.getFecha().toLocalTime() + "] " + n.getMensaje());
            }
        }
    }

    public void emitirComunicadoGlobal() {
        System.out.println("\n📢 [SISTEMA] Emitiendo comunicado global a todos los usuarios...");
        gestorNotificaciones.notificar();
    }

    public void darDeBajaUsuarioAlertas(UUID usuarioId) {
        Usuario usuario = registroUsuarios.get(usuarioId);
        if(usuario != null) {
            gestorNotificaciones.desuscribir(usuario);
            System.out.println("El usuario " + usuario.getCorreo() + " ha sido desuscrito de las alertas globales.");
        }
    }

    public UUID publicarContenido(UUID canalId, TipoContenido tipo, String titulo, String descripcion, Object... datos) {
        Canal canalAutor = registroCanales.get(canalId);
        if (canalAutor == null) throw new ContenidoNoEncontradoException("El canal no existe.");

        try {
            Contenido contenido = fabricaContenido.crearContenido(tipo, titulo, descripcion, canalAutor, datos);
            baseDatosContenido.add(contenido);
            System.out.println("\n[NUEVO CONTENIDO] '" + titulo + "' publicado por " + canalAutor.getNombreCanal());
            gestorNotificaciones.notificarNuevoVideo(canalAutor, contenido);
            return contenido.getId(); // Retornamos el ID para interactuar luego
        } catch (TipoContenidoNoReconocible e) {
            System.err.println("Error al publicar: " + e.getMessage());
            return null;
        }
    }

    public Contenido obtenerContenido(UUID contenidoId) {
        for (Contenido c : baseDatosContenido) {
            if (c.getId().equals(contenidoId)) return c;
        }
        throw new ContenidoNoEncontradoException("El contenido solicitado no existe en la base de datos.");
    }

    public void verContenido(UUID usuarioId, UUID contenidoId) {
        Usuario usuario = registroUsuarios.get(usuarioId);
        Contenido contenido = obtenerContenido(contenidoId);

        if (usuario != null && contenido != null) {
            contenido.registrarVista();
            System.out.println("Usuario " + usuario.getCorreo() + " ha visto: '" + contenido.getTitulo() + "' (Total vistas: " + contenido.getVistas() + ")");
        }
    }

    public void reaccionarAContenido(UUID usuarioId, UUID contenidoId, TipoReaccion tipo) {
        Usuario usuario = registroUsuarios.get(usuarioId);
        Contenido contenido = obtenerContenido(contenidoId);

        if (usuario != null && contenido != null) {
            Reaccion reaccion = new Reaccion(usuario, tipo);
            contenido.agregarInteracciones(reaccion);
            System.out.println("Usuario " + usuario.getCorreo() + " dejó una reacción (" + tipo + ") en '" + contenido.getTitulo() + "'");
        }
    }

    public Comentario comentarContenido(UUID usuarioId, UUID contenidoId, String texto) {
        Usuario usuario = registroUsuarios.get(usuarioId);
        Contenido contenido = obtenerContenido(contenidoId);

        if (usuario != null && contenido != null) {
            Comentario comentario = new Comentario(usuario, texto, contenido);
            contenido.agregarInteracciones(comentario);
            System.out.println("Usuario " + usuario.getCorreo() + " comentó: \"" + texto + "\"");
            return comentario;
        }
        return null;
    }

    public void responderComentario(UUID usuarioId, Comentario comentarioPadre, String texto, UUID contenidoId) {
        Usuario usuario = registroUsuarios.get(usuarioId);
        Contenido contenidoPadre = obtenerContenido(contenidoId);

        if (usuario != null && comentarioPadre != null && contenidoPadre != null) {
            Comentario respuesta = new Comentario(usuario, texto, contenidoPadre);
            comentarioPadre.agregarRespuesta(respuesta);
            System.out.println("Usuario " + usuario.getCorreo() + " respondió: \"" + texto + "\"");
        }
    }

    public void actualizarEspectadoresEnVivo(UUID contenidoId, int nuevosEspectadores) {
        Contenido contenido = obtenerContenido(contenidoId);
        if (contenido instanceof TransmisionEnVivo) {
            ((TransmisionEnVivo) contenido).actualizarEspectadores(nuevosEspectadores);
            System.out.println("STREAM UPDATE: '" + contenido.getTitulo() + "' ahora tiene " + nuevosEspectadores + " espectadores.");
        }
    }

    public void finalizarTransmisionEnVivo(UUID contenidoId) {
        Contenido contenido = obtenerContenido(contenidoId);
        if (contenido instanceof TransmisionEnVivo) {
            ((TransmisionEnVivo) contenido).finalizarTransmision();
            System.out.println("STREAM FINALIZADO: '" + contenido.getTitulo() + "'.");
        }
    }

    public void mostrarDetallesEInteracciones(UUID contenidoId) {
        Contenido contenido = obtenerContenido(contenidoId);
        System.out.println("=== REPORTE DETALLADO DEL CONTENIDO ===");
        System.out.println("Título: " + contenido.getTitulo());
        System.out.println("Descripción: " + contenido.getDescripcion());
        System.out.println("Publicado el: " + contenido.getFechaPublicacion().toLocalDate());

        if (contenido instanceof VideoLargo) {
            VideoLargo vl = (VideoLargo) contenido;
            System.out.println("Duración: " + vl.getDuracionSegundos() + "s | Monetizado: " + (vl.isEsMonetizado() ? "Sí" : "No"));
        } else if (contenido instanceof Short) {
            Short sh = (Short) contenido;
            System.out.println("Música de fondo: " + sh.getMusicaDeFondo());
        }

        List<Interaccion> interacciones = contenido.getInteracciones();
        if (interacciones.isEmpty()) {
            System.out.println("\n  -> No hay interacciones registradas.");
            return;
        }

        int likes = 0, dislikes = 0;
        System.out.println("\n--- INTERACCIONES ---");
        for (Interaccion i : interacciones) {

            System.out.println("[ID: " + i.getId() + " | Fecha: " + i.getFechaCreacion().toLocalDate() + "] Autor: " + i.getAutor().getCorreo());

            if (i instanceof Reaccion) {
                Reaccion r = (Reaccion) i;
                if (r.getTipo() == TipoReaccion.LIKE) likes++;
                else dislikes++;
                System.out.println("  -> Reacción: " + r.getTipo());

            } else if (i instanceof Comentario) {
                Comentario c = (Comentario) i;

                System.out.println("  -> Comentó en '" + c.getContenidoPadre().getTitulo() + "': \"" + c.getTexto() + "\"");

                if (!c.getRespuestas().isEmpty()) {
                    for (Comentario resp : c.getRespuestas()) {
                        System.out.println("       * Respuesta de " + resp.getAutor().getCorreo() + ": \"" + resp.getTexto() + "\"");
                    }
                }
            }
        }
        System.out.println("\n[RESUMEN] Vistas Totales: " + contenido.getVistas() + " | Likes: " + likes + " | Dislikes: " + dislikes);
        System.out.println("=======================================");
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

    public void moderacionAvanzada(UUID idMod, UUID idObjetivo, boolean ocultar) {
        RolCanal rolEjecutor = registroRoles.stream()
                .filter(r -> r.getUsuarioAsignado().getId().equals(idMod))
                .findFirst().orElse(null);
        Canal canalObjetivo = obtenerCanal(idObjetivo);

        if (rolEjecutor != null && canalObjetivo != null) {
            try {
                if (ocultar) {
                    servicioCanales.ocultarCanalEnCanal(rolEjecutor, canalObjetivo);
                    System.out.println("El canal fue ocultado exitosamente.");
                } else {
                    servicioCanales.mostrarCanalEnCanal(rolEjecutor, canalObjetivo);
                    System.out.println("El canal fue mostrado exitosamente.");
                }
                boolean estaOculto = servicioCanales.canalEstaOcultoEn(rolEjecutor.getCanal(), canalObjetivo);
                System.out.println("  -> Estado actual de ocultamiento: " + estaOculto);
            } catch (PermisoDenegadoException e) {
                System.out.println("Operación denegada por seguridad: " + e.getMessage());
            }
        }
    }

    public void gestionarEstructuraRoles(UUID idPropietario, UUID idNuevoMod, UUID idCanal) {
        Usuario propietario = registroUsuarios.get(idPropietario);
        Usuario nuevoMod = registroUsuarios.get(idNuevoMod);
        Canal canal = obtenerCanal(idCanal);

        if (propietario != null && nuevoMod != null && canal != null) {
            RolCanal rolProp = servicioRolesCanal.asignarRolPropietario(propietario, canal);
            RolCanal rolMod = servicioRolesCanal.asignarRolComo(rolProp, nuevoMod, canal, new PermisoModeradorGestor());
            servicioRolesCanal.removerRol(rolMod);
        }
    }


    public Canal obtenerCanal(UUID canalId) { return registroCanales.get(canalId); }

}