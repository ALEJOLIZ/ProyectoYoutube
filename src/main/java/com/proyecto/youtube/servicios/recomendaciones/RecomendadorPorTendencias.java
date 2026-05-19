package com.proyecto.youtube.servicios.recomendaciones;

import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.contenido.Contenido;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
public class RecomendadorPorTendencias implements EstrategiaRecomendacion{
    @Override
    public List<Contenido> generarSugerencias(Usuario usuario, List<Contenido> baseDeDatos){
        return baseDeDatos.stream()
                .sorted(Comparator.comparingInt(Contenido::getContadorVisualizaciones).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
