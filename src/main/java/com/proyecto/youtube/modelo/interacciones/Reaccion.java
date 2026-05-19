package com.proyecto.youtube.modelo.interacciones;

import com.proyecto.youtube.modelo.usuario.Usuario;

public class Reaccion extends Interaccion {
    private final TipoReaccion tipo;

    public Reaccion(Usuario autor, TipoReaccion tipo){
        super(autor);
        this.tipo=tipo;
    }

    public TipoReaccion getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return super.toString() + " | Reacción: " + tipo;
    }

}
