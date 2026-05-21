package com.proyecto.youtube.core;

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

        System.out.println("\n==========================================================");
        System.out.println("      SIMULACIÓN FINALIZADA CON ÉXITO   ");
        System.out.println("==========================================================");
    }
}