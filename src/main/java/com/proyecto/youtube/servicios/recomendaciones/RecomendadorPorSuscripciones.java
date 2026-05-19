package com.proyecto.youtube.servicios.recomendaciones;

import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.contenido.Contenido;
import com.proyecto.youtube.modelo.usuario.canal.Canal;
import java.util.List;
import java.util.stream.Collectors;

public class RecomendadorPorSuscripciones implements EstrategiaRecomendacion{
    @Override
    public List<Contenido> generarSugerencias(Usuario usuario, List<Contenido> baseDeDatos) {
        if (usuario.getCanalPropio()==null){
            return List.of();
        }

        Set<Canal> canalesSuscritos = usuario.getCanalPropio().getCanalesSuscritos();

        return baseDeDatos.stream()
                .filter(contenido -> canalesSuscritos.contains(contenido.getCanalAutor()))
                .collect(Collectors.toList());
    }
}
