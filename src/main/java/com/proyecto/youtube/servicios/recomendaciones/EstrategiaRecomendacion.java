package com.proyecto.youtube.servicios.recomendaciones;

import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.contenido.Contenido;
import java.util.List;

public interface EstrategiaRecomendacion {
    List<Contenido> generarSugerencias(Usuario usuario, List<Contenido> baseDeDatos);
}