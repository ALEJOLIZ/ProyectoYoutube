package com.proyecto.youtube.core;

import com.proyecto.youtube.modelo.interacciones.Comentario;
import com.proyecto.youtube.modelo.interacciones.TipoReaccion;
import com.proyecto.youtube.servicios.fabricas.TipoContenido;
import com.proyecto.youtube.servicios.recomendaciones.RecomendadorPorTendencias;

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

        System.out.println("\n==========================================================");
        System.out.println("      SIMULACIÓN FINALIZADA CON ÉXITO   ");
        System.out.println("==========================================================");
    }
}