package jugadores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class J2 extends Caballero {

	// Por el momento el jugador 2 es un cactus (jaja), hace daño cuando entras en contacto con su hitbox.
	// Una vez que se implemente el juego en red se eliminara y pasara a ser un caballero como lo es el J1
	
	private Texture textura = new Texture("Cactus_Sprite.png");
	
	public J2() {
		super("Cactus", 0, null, null, 10, 1);
		super.region = new TextureRegion(textura, 0, 0, textura.getWidth()/9, textura.getHeight()/2);
	}
}
