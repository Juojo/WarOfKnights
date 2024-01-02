package jugadores;

import com.badlogic.gdx.graphics.Texture;

public class J2 extends Caballero {
	
	public J2() {
		super(2, "Jugador 2", 0, new Texture("myAssets/newSize/idle-red.png"), new Texture("myAssets/newSize/walk-red.png"));
		segundaManoX = 0;
	}
}
