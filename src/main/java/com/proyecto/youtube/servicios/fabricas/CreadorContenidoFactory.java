package com.proyecto.youtube.servicios.fabricas;

import com.proyecto.youtube.modelo.usuario.CreadorContenido;
import com.proyecto.youtube.modelo.contenido.Contenido;
import com.proyecto.youtube.modelo.contenido.TransmisionEnVivo;
import com.proyecto.youtube.modelo.contenido.Short;
import com.proyecto.youtube.modelo.contenido.VideoLargo;
public class CreadorContenidoFactory implements  FabricaContenido{

    @Override
    public Contenido crearContenido(TipoContenido tipo, String titulo, String descripcion, CreadorContenido autor, Object... args) {
        switch (tipo){
            case VIDEO_LARGO:
                int duracion=(Integer) args[0];
                boolean monetizado =(Boolean) args[1];
                return new VideoLargo(titulo,descripcion,autor,duracion,monetizado);

            case SHORT:
                String pistaAudio=(String)  args[0];
                return new Short(titulo,descripcion,autor,pistaAudio);

            case TRANSMISION_EN_VIVO:
                return new TransmisionEnVivo(titulo,descripcion,autor);


            default:
                throw new TipoContenidoNoReconocible("Tipo de contenido no reconocido.");
        }
    }
}
