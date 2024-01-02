# Desafios encontrados

* Por el momento no contamos con la implementacion en red del juego por lo que para poder ir haciendo pruebas el J2 es un cactus. Hara daño cuando entras en contacto con su hitbox y podra recibir ataques del J1.
  
* Al crear una clase "Hud" se adaptaron las herramientas de desarrollo para que sen parte de la misma. Esto trajo el problema de que no se tenia acceso a los valores de posicion de la camara principal y de los jugadores. Para solucionarlo se pasaron las variables por parametros.
  
* Para lograr que el Hud se actualize por cada frame se incluye un "hud.update" dentro del render en la clase principal del juego.

# Desarrollo de consignas
1. **Diseñar e implementar múltiples pantallas**: Se comenzo a desarrollar pero no se logro llegar a un resultado funcional
2. **Crear y mostrar un HUD**: Implementado y funcionando correctamente. Cuando el timer llega a 0 se sigue restando, esto se evitara en un futuro implementando un listener que detecte cuando se termine el tiempo para mostrar la pantalla de fin de juego.
3. **Añadir interacciones básicas en el mundo del juego**: Se creo un enemigo pero por el momento no se puede interactuar de ninguna forma con el (se puede ver su vida y armadura en el hud)
4. **Añadir una música de fondo que se adapte al ambiente del juego y efectos sonoros**: No implementado

<br>
<br>

> Equipo de desarrollo: Miñarro y Piromalli