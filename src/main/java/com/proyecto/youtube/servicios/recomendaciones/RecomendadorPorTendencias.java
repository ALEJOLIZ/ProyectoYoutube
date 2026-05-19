package com.proyecto.youtube.servicios.recomendaciones;

import com.proyecto.youtube.modelo.usuario.Usuario;
import com.proyecto.youtube.modelo.contenido.Contenido;

import java.util.List;
import java.util.stream.Collectors;
public class RecomendadorPorTendencias implements EstrategiaRecomendacion{
    @Override
    public List<Contenido> generarSugerencias(Usuario usuario, List<Contenido> baseDeDatos){
        return baseDeDatos.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getVistas(), c1.getVistas()))
                .collect(Collectors.toList());
    }
}
