package com.proyecto.youtube.servicios.fabricas;

import com.proyecto.youtube.modelo.usuario.CreadorContenido;
import com.proyecto.youtube.modelo.contenido.Contenido;
public interface FabricaContenido {
    Contenido crearContenido(TipoContenido tipo, String titulo,String descripcion, CreadorContenido autor, Object... args);
}