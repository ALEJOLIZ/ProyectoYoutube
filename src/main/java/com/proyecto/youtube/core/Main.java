package com.proyecto.youtube.core;

import com.proyecto.youtube.modelo.contenido.Contenido;
import com.proyecto.youtube.modelo.contenido.Playlist;
import com.proyecto.youtube.modelo.contenido.VideoLargo;
import com.proyecto.youtube.modelo.interacciones.Comentario;
import com.proyecto.youtube.modelo.interacciones.TipoReaccion;
import com.proyecto.youtube.modelo.usuario.canal.RolCanal;
import com.proyecto.youtube.modelo.usuario.permisos.PermisoCanalStrategy;
import com.proyecto.youtube.modelo.usuario.permisos.PermisoModeradorEstandar;
import com.proyecto.youtube.servicios.fabricas.TipoContenido;
import com.proyecto.youtube.servicios.recomendaciones.RecomendadorPorSuscripciones;
import com.proyecto.youtube.servicios.recomendaciones.RecomendadorPorTendencias;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("   INICIANDO PLATAFORMA YOUTUBE   ");
        System.out.println("==========================================================\n");

        // ESCENARIO 1: Inicialización
        System.out.println(">> ESCENARIO 1: Inicializando sistema...");
        PlataformaYouTube youtube = new PlataformaYouTube(new RecomendadorPorTendencias());
        System.out.println("Plataforma inicializada.\n");

        // ESCENARIO 2: Gestión de Usuarios y Canales
        System.out.println(">> ESCENARIO 2: Registro de Usuarios y Creación de Canales");
        UUID idCarlos = youtube.registrarUsuario("Carlos Rodriguez", "carlos.dev@mail.com");
        UUID idElena = youtube.registrarUsuario("Elena Gomez", "elena.vlogs@mail.com");
        UUID idProfesor = youtube.registrarUsuario("Dr. Arcadio Sistemas", "arcadio.poo@universidad.edu");

        UUID canalCarlos = youtube.crearCanal(idCarlos, "Carlos Tech Solutions");
        UUID canalElena = youtube.crearCanal(idElena, "Elena Life");
        System.out.println();

        // ESCENARIO 3: Suscripciones
        System.out.println(">> ESCENARIO 3: Gestión de Suscripciones");
        youtube.suscribirUsuarioACanal(idElena, canalCarlos);
        youtube.suscribirUsuarioACanal(idProfesor, canalCarlos);
        youtube.suscribirUsuarioACanal(idCarlos, canalElena);
        System.out.println();

        // ESCENARIO 4: Publicación de Contenido
        System.out.println(">> ESCENARIO 4: Publicación de Contenido (Factory)");
        UUID idVideoLargo = youtube.publicarContenido(
                canalCarlos, TipoContenido.VIDEO_LARGO,
                "Guía de Arquitectura Clean Code", "Tutorial de POO", 1200, true
        );

        UUID idStream = youtube.publicarContenido(
                canalElena, TipoContenido.TRANSMISION_EN_VIVO,
                "Viaje a Tokyo en Vivo", "¡Acompáñenme!", true, 0
        );
        System.out.println();

        // ESCENARIO 5: VISUALIZACIONES E INTERACCIONES
        System.out.println(">> ESCENARIO 5: Visualizaciones e Interacciones");

        youtube.verContenido(idElena, idVideoLargo);
        youtube.verContenido(idProfesor, idVideoLargo);

        youtube.reaccionarAContenido(idElena, idVideoLargo, TipoReaccion.LIKE);
        youtube.reaccionarAContenido(idCarlos, idVideoLargo, TipoReaccion.DISLIKE);

        Comentario comentarioProfesor = youtube.comentarContenido(idProfesor, idVideoLargo, "Excelente explicación sobre los patrones de diseño, muy útil.");
        youtube.responderComentario(idCarlos, comentarioProfesor, "¡Muchas gracias, doctor!", idVideoLargo);
        System.out.println();

        System.out.println(">> ESCENARIO 5.1: Desglose de Estadísticas");
        youtube.mostrarDetallesEInteracciones(idVideoLargo);
        System.out.println();

        // ESCENARIO 6: GESTIÓN DE TRANSMISIÓN EN VIVO
        System.out.println(">> ESCENARIO 6: Estado de Transmisiones en Vivo");
        // Se conectan usuarios al stream
        youtube.actualizarEspectadoresEnVivo(idStream, 45);
        youtube.actualizarEspectadoresEnVivo(idStream, 120);
        // El creador finaliza el stream
        youtube.finalizarTransmisionEnVivo(idStream);
        System.out.println();

        // ESCENARIO 7: Generación de Feed (Strategy)
        System.out.println(">> ESCENARIO 7: Generación de Feed");
        System.out.println("Feed de Carlos (Estrategia: TENDENCIAS):");
        imprimirFeed(youtube.mostrarFeed(idCarlos));

        System.out.println("\nCambiando algoritmo del feed en tiempo real...");
        youtube.setAlgoritmoActual(new RecomendadorPorSuscripciones());

        System.out.println("Feed de Carlos (Estrategia: SUSCRIPCIONES):");
        imprimirFeed(youtube.mostrarFeed(idCarlos));
        System.out.println();

        // ESCENARIO 8: GESTIÓN DE PLAYLISTS
        System.out.println(">> ESCENARIO 8: Gestión de Playlists");

        // Creamos un segundo video largo para que la lista tenga varios elementos
        UUID idVideoLargo2 = youtube.publicarContenido(
                canalCarlos, TipoContenido.VIDEO_LARGO,
                "Patrones de diseño en Java", "Curso intermedio", 800, false
        );
        // Le damos vistas al segundo video para que al organizar la playlist se note la diferencia
        youtube.verContenido(idElena, idVideoLargo2);
        youtube.verContenido(idProfesor, idVideoLargo2);
        youtube.verContenido(idCarlos, idVideoLargo2);

        // Uso de la clase Playlist
        Playlist miLista = youtube.crearPlaylist("Curso de Backend", canalCarlos);
        youtube.agregarVideoAPlaylist(miLista, idVideoLargo);
        youtube.agregarVideoAPlaylist(miLista, idVideoLargo2);

        System.out.println("\nMostrando Playlist ordenada por Vistas (Mayor a Menor):");
        youtube.mostrarContenidoPlaylist(miLista);
        System.out.println();

        // ESCENARIO 9: GESTIÓN DE ROLES DINÁMICOS
        System.out.println(">> ESCENARIO 9: Asignación Dinámica de Permisos Administrativos");

        PermisoCanalStrategy moderadorEstandar = new PermisoModeradorEstandar();

        // Le asignamos al usuario Elena la labor de moderar el canal de Carlos
        RolCanal rolElena = youtube.asignarRolModeracion(idElena, canalCarlos, moderadorEstandar);

        // Simulamos la operación de moderación consumiendo las propiedades internas del rol
        youtube.simularModeracionComentario(rolElena, "Spam detectado en el video de Java");
        System.out.println();

        // ESCENARIO 10: AUDITORÍA DE CANALES Y NOTIFICACIONES
        System.out.println(">> ESCENARIO 10: Auditoría del Sistema e Historiales");

        // Modificar descripción y validar contadores de Canal
        youtube.modificarMetadatosCanal(canalCarlos, "Canal oficial dedicado al desarrollo de software y backend.");
        youtube.imprimirPanelAdministrativoCanal(canalCarlos);

        youtube.emitirComunicadoGlobal();
        youtube.darDeBajaUsuarioAlertas(idProfesor);

        youtube.revisarBandejaNotificacionesUsuario(idCarlos);
        youtube.revisarBandejaNotificacionesUsuario(idElena);
        youtube.revisarBandejaNotificacionesUsuario(idProfesor);
        System.out.println();

        // ESCENARIO 11: PRUEBA INTEGRAL DE SERVICIOS
        System.out.println(">> ESCENARIO 11: Auditoría y Pruebas de Servicios");

        // 11.1 Actualización de perfiles
        youtube.actualizarPerfilCompleto(idCarlos, "carlos.nuevo@mail.com", "Carlos Tech & Dev");

        // 11.2 Gestión cruzada de Suscripciones
        youtube.gestionarSuscripcion(canalCarlos, canalElena, true); // Carlos se suscribe a Elena
        youtube.gestionarSuscripcion(canalCarlos, canalElena, false); // Se desuscribe

        // 11.3 Gestión estructural de Roles Dinámicos (Simulando ascensos y despidos)
        youtube.gestionarEstructuraRoles(idCarlos, idElena, canalCarlos);

        // 11.4 Moderación avanzada: Elena oculta/muestra el canal del profe en su chat
        UUID canalProfe = youtube.crearCanal(idProfesor, "Clases POO");
        youtube.moderacionAvanzada(idElena, canalProfe, true);  // Lo oculta
        youtube.moderacionAvanzada(idElena, canalProfe, false); // Lo vuelve a mostrar (Lanzará excepción de seguridad)
        System.out.println();

        // ESCENARIO 12: FILTRADO CON STREAMS (API DE JAVA)

        System.out.println(">> ESCENARIO 12: Filtrado de contenido avanzado (Streams)");

        // Solicitamos la lista a través de la fachada
        List<Contenido> todoElContenido = youtube.obtenerTodosLosContenidos();

        System.out.println("Top contenido por vistas:");
        todoElContenido.stream()
                .sorted()                                  // usa el compareTo ya implementado
                .limit(3)
                .forEach(c -> System.out.println("  - " + c.getTitulo() + " (" + c.getVistas() + " vistas)"));

        System.out.println("\nSolo VideoLargo:");
        todoElContenido.stream()
                .filter(c -> c instanceof VideoLargo)
                .forEach(c -> System.out.println("  - " + c.getTitulo()));

        System.out.println("\nPublicados hoy:");
        todoElContenido.stream()
                .filter(c -> c.getFechaPublicacion().toLocalDate().equals(LocalDate.now()))
                .forEach(c -> System.out.println("  - " + c.getTitulo()));
        System.out.println();

        // Generar el gran reporte de auditoría global
        youtube.generarReporteAuditoriaGlobal();

        System.out.println("\n==========================================================");
        System.out.println("      SIMULACIÓN FINALIZADA CON ÉXITO   ");
        System.out.println("==========================================================");
    }

    private static void imprimirFeed(List<Contenido> feed) {
        if (feed == null || feed.isEmpty()) {
            System.out.println("  [El feed está vacío]");
        } else {
            for (int i = 0; i < feed.size(); i++) {
                Contenido c = feed.get(i);
                System.out.println("  " + (i + 1) + ". " + c.toString());
            }
        }
    }
}