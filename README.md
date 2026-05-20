# Proyecto YouTube
En este repositorio se encuentra desarrollado un proyecto el cual busca emular el funcionamiento 
de la red social YouTube, por medio del uso de estrategias de programación orientada a objetos.
## Aspectos importantes del proyecto:
### Desiciones de diseño:
El proyecto cuenta con un sistema donde se podrá crear contenido en tres formatos diferentes 
(shor, video, transmisión en vivo), además incluyendo un sistema de interacciones para que los 
usuarios puedan dar like o comentar en el contenido que hayan consumido. También cuenta con un 
sistema de recomendaciones, el cual puede ser por medio de suscripciones o de tendencias. Para 
finalizar, se integro un sistema de notificaciones donde se mantendrán informado a los usuarios sobre 
nuevo contenido que suba alguno de los canales a los que estén suscritos.  
En un principio se había pensado la parte de los usuarios donde  existen creadores de contenidos 
o consumidores, pero se prefirio irse por un sistema de canales donde cada usuario que cree una cuenta 
contara con un canal en el cual podrá subir contenido o simplemente consumir contenido de otros canales.
### Patrones de diseño
Para reducir las posibles causas de error y hacer más escalable el proyecto, se implementaron algunos 
patrones de diseño. Entre estos se encuentran:
* Factory Method: este es un patrón que permite crear objetos con una interfaz en una superclase,
permitiendo editar el tipo de producto que se crean, siempre y cuando dichos productos tienen una clase
o interfaz en común. En este caso se usa para los diferentes tipos de contenido.
* Strategy: este patrón usa un conjunto de algoritmos intercambiables para resolver un mismo problema
de diferentes maneras. En este caso se usa para recomendar videos por medio de dos estrategias diferentes,
por suscripción y por tendencias.
* Observer: este es un patrón que permite informar sobre el cambio de estado de un objeto observado a los
objetos que los están observando. En este caso se uso para el sistema de notificaciones implementado.
