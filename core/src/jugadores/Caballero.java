package jugadores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Caballero {
	
	public Sprite miCaballero;

	private float tiempo;
	
	// Idle
	private TextureRegion[] regionMovimientoIdle;
	private Animation<TextureRegion> animacionIdle;
	
	// Run
	private TextureRegion[] regionMovimientoRun;
	private Animation<TextureRegion> animacionRun;
	
	protected TextureRegion frameAcutal;
	private int largo = 0, alto = 0;
	private int escala = 3;
	public Sprite hitbox;
	
	private String nombre;
	private int vida = 100;
	private int armadura = 0; // Maximo 50

	public TextureRegion region; // Solo se usa para el cactus (J2)

	
	public Caballero(String nombre, int armadura, Texture spriteSheetIdle, Texture spriteSheetRun, int col, int row) {
		this.nombre = nombre;
		this.armadura = armadura;
		
		if (spriteSheetIdle != null) {
			cargarAnimacion(spriteSheetIdle, col, row, "idle");
			
			largo = spriteSheetIdle.getWidth();
			alto = spriteSheetIdle.getHeight();
			
			miCaballero = new Sprite(spriteSheetIdle, largo, alto);
			miCaballero.setSize(largo*escala, alto*escala);
			miCaballero.setPosition(-(44*escala) + 768, -(49*escala));
		}
		if (spriteSheetRun != null) cargarAnimacion(spriteSheetRun, col, row, "run");
		
		hitbox = new Sprite(new Texture("hitbox_cell.png"));
		hitbox.setSize(21*escala, 39*escala);
		hitbox.setPosition(0, 223);
		
		
	}
	
	private void ejecutarAtaque(Caballero jugadorAtacado) {
		jugadorAtacado.vida -= 25;
	}
	
	private void cargarAnimacion(Texture textura, int spriteColumnas, int spriteFilas, String tipo) {
		// Se inicializan las variables temporales
		int ancho = textura.getWidth();
		int alto = textura.getHeight();
		TextureRegion[] regionMovimiento;
		Animation<TextureRegion> animacion;
		float tiempo;
		
		TextureRegion[][] temp = TextureRegion.split(textura, ancho/spriteColumnas, alto/spriteFilas);
		regionMovimiento = new TextureRegion[spriteColumnas];
		int indice = 0;
		for (int i = 0; i < spriteFilas; i++) {
			for (int j = 0; j < spriteColumnas; j++) {
				regionMovimiento[indice++] = temp[i][j];
			}
		};
		
		animacion = new Animation((tipo.equals("idle") ? 0.12f : 0.06f), regionMovimiento);
		tiempo = 0f;
		
		// Se asignan los valores de las variables temporales a sus varibles fijas y unicas para cada tipo de animacion
		if (tipo.equals("idle")) {
			regionMovimientoIdle = regionMovimiento;
			animacionIdle = animacion;
			this.tiempo = tiempo;
		} else if (tipo.equals("run")) {
			regionMovimientoRun = regionMovimiento;
			animacionRun = animacion;
			this.tiempo = tiempo;
		}
		
	}

	public void mover(float x) {
		hitbox.setPosition(x+44*escala, hitbox.getY());
		miCaballero.setPosition(x, miCaballero.getY());
		
	}

	public TextureRegion getFrameActual(String anim) {
		if (anim.equals("idle")) {
			frameAcutal = animacionIdle.getKeyFrame(tiempo, true);
		} else if (anim.equals("run")) {
			frameAcutal = animacionRun.getKeyFrame(tiempo, true);
		}

		return frameAcutal;
	}
	
	public int getLargo() {
		return largo;
	}
	
	public int getAlto() {
		return alto;
	}
	
	public int getEscala() {
		return escala;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getVida() {
		return vida;
	}

	public int getArmadura() {
		return armadura;
	}
	
	public void setTiempo(float tiempo) {
		this.tiempo += tiempo;
	}
}
