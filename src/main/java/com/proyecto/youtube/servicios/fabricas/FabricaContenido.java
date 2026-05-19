package com.proyecto.youtube.servicios.fabricas;

import com.proyecto.youtube.modelo.usuario.canal.Canal;
import com.proyecto.youtube.modelo.contenido.Contenido;
public interface FabricaContenido {
    Contenido crearContenido(TipoContenido tipo, String titulo,String descripcion, Canal canalAutor, Object... args);
}