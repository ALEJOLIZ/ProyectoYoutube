package com.proyecto.youtube.modelo.contenido;



import com.proyecto.youtube.modelo.usuario.canal.Canal;

public class Short extends Contenido {
    private String musicaDeFondo;

    public Short(String titulo, String descripcion, Canal canalAutor, String musica) {
        super(titulo, descripcion, canalAutor);
        this.musicaDeFondo = musica;
    }

    public String getMusicaDeFondo() { return musicaDeFondo; }

    @Override
    public String toString() {
        return "YouTube Short: " + super.toString() + " | Música: " + musicaDeFondo;
    }
}
