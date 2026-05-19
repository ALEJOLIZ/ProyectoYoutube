package com.proyecto.youtube.servicios.fabricas;

import com.proyecto.youtube.modelo.usuario.canal.Canal;
import com.proyecto.youtube.modelo.contenido.Contenido;
import com.proyecto.youtube.modelo.contenido.TransmisionEnVivo;
import com.proyecto.youtube.modelo.contenido.Short;
import com.proyecto.youtube.modelo.contenido.VideoLargo;
public class CreadorContenidoFactory implements  FabricaContenido{

    @Override
    public Contenido crearContenido(TipoContenido tipo, String titulo, String descripcion, CreadorContenido autor, Object... args) {
        switch (tipo){
            case VIDEO_LARGO:
                int duracion=(args.length > 0 && args[0] instanceof Integer)?(Integer) args[0]: 0;
                boolean monetizado =(args.length > 1 && args[1] instanceof Boolean) ? (Boolean) args[1] : false;;
                return new VideoLargo(titulo,descripcion,canalAutor,duracion,monetizado);

            case SHORT:
                String pistaAudio=(args.length > 0 && args[0] instanceof String) ? (String) args[0] : "";
                return new Short(titulo,descripcion,canalAutor,pistaAudio);

            case TRANSMISION_EN_VIVO:
                return new TransmisionEnVivo(titulo,descripcion,canalAutor);


            default:
                throw new TipoContenidoNoReconocible("Tipo de contenido no reconocido.");
        }
    }
}
