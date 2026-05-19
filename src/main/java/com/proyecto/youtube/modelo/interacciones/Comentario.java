package com.proyecto.youtube.modelo.interacciones;
import com.proyecto.youtube.modelo.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Comentario extends Interaccion {
    private String texto;
    private List<Comentario> respuestas;

    public Comentario(Usuario autor, String texto){
        super(autor);
        this.texto = texto;
        this.respuestas = new ArrayList<>();
    }

    public void agregarRespuesta(Comentario respuesta) {
        if (respuesta != null) {
            this.respuestas.add(respuesta);
        }
    }

    public String getTexto() { return texto; }
    public List<Comentario> getRespuestas() { return new ArrayList<>(respuestas); }

    @Override
    public String toString() {
        return super.toString() + " | Comentó: \"" + texto + "\" (" + respuestas.size() + " respuestas)";
    }

}
