package com.proyecto.youtube.servicios.recomendaciones;

import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.contenido.Contenido;
import java.util.List;
import java.util.stream.Collectors;

public class RecomendadorPorSuscripciones implements EstrategiaRecomendacion{
    @Override
    public List<Contenido> generarSugerencias(Usuario usuario, List<Contenido> baseDeDatos) {
        return baseDeDatos.stream()
                .filter(contenido -> usuario.getSuscripciones().contains(contenido.getAutor()))
                .sorted((c1,c2)->c2.getFechaPublicacion().compareTo(c1.getFechaPublicacion()))
                .collect(Collectors.toList());
    }
}
