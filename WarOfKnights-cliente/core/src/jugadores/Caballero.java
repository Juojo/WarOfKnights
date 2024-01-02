package jugadores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Caballero {
	
	// Idle
	private TextureRegion[] regionMovimientoIdle;
	private Animation<TextureRegion> animacionIdle;
	
	// Walk
	private TextureRegion[] regionMovimientoWalk;
	private Animation<TextureRegion> animacionWalk;
	
	// backWalk
	private TextureRegion[] regionMovimientoBackWalk;
	private Animation<TextureRegion> animacionBackWalk;
	
	private Texture texturaBrazo;
	private Sprite spriteBrazo;
	
	protected TextureRegion frameAcutal;
	protected int escala;
	private int largo, alto, margenX, margenYB, margenYT;
	private float tiempo;
	
	private String nombre;
	private int vida = 100;
	private int armadura = 0; // Maximo 50
	private int numero;
	
	public Sprite hitbox;
	public Rectangle nHitbox, hitboxCabeza, hitboxPecho, hitboxSegundaMano, hitboxPiernas, hitboxPie;
	public Polygon hitboxBrazo, centroHitboxBrazo, nHitboxP;
	
	public float centroBrazoX, centroBrazoY, anguloBrazoGrados;
	
	protected float segundaManoX;
	private boolean fueAtacado = false;
	
	private Movimiento movimientoActual = Movimiento.QUIETO;
	
	public Caballero(int numero, String nombre, int armadura, Texture spriteSheetIdle, Texture spriteSheetRun) {
		this.nombre = nombre;
		this.armadura = armadura;
		this.numero = numero;
		
		cargarAnimacion(spriteSheetIdle, 3, 1, "idle", false);
		cargarAnimacion(spriteSheetRun, 6, 1, "walk", false);
		cargarAnimacion(spriteSheetRun, 6, 1, "backWalk", true);
		
		texturaBrazo = new Texture("myAssets/newSize/brazo.png");
		spriteBrazo = new Sprite(texturaBrazo);
		
		largo = spriteSheetIdle.getWidth()/3;
		alto = spriteSheetIdle.getHeight()/1;
		escala = 10;
		
		nHitbox = new Rectangle(
		/* X */		( ( nombre.equals("Jugador 2") ? ( (1280/2)+(1280/2)/2 ) : ((1280/2)/2 ) ) - (17*escala)/2),
		/* Y */		128,
		/* largo */	17*escala,
		/* alto */	alto*escala
		);
		
		if (nombre.equals("Jugador 1")) centroBrazoX = 3.5f*escala;
		else centroBrazoX = nHitbox.getWidth() - 3.5f*escala;
		
		centroBrazoY = nHitbox.getY()+nHitbox.getHeight()-9f*escala; // -8.5f

		hitboxCabeza = new Rectangle(
				0,
				(nHitbox.getY()+nHitbox.getHeight())-8*escala,
				5*escala,
				6*escala
			);
		hitboxPecho = new Rectangle(
				0,
				(nHitbox.getY()+nHitbox.getHeight())-19*escala,
				8*escala,
				11*escala
			);
		hitboxSegundaMano = new Rectangle(
				0,
				(nHitbox.getY()+nHitbox.getHeight())-18*escala,
				5*escala,
				2*escala
			);
		hitboxPiernas = new Rectangle(
				0,
				nHitbox.getY(),
				10*escala,
				13*escala
			);
		hitboxPie = new Rectangle(
				0,
				nHitbox.getY(),
				2*escala,
				5*escala
			);
		
		hitboxBrazo = new Polygon(new float[]{
				centroBrazoX-10, centroBrazoY+10,
				centroBrazoX+10+28.5f*escala, centroBrazoY+10,
				centroBrazoX+10+28.5f*escala, centroBrazoY-10,
				centroBrazoX+10+20*escala, centroBrazoY-10,
				centroBrazoX-10, centroBrazoY-10
			}
		);
		
		hitboxBrazo.setOrigin(centroBrazoX, centroBrazoY);
		spriteBrazo.setOrigin(2, spriteBrazo.getHeight()/2);
		spriteBrazo.setScale(escala);
		
		// Solo para render
		centroHitboxBrazo = new Polygon (new float[] {
				centroBrazoX-5, centroBrazoY+5,
				centroBrazoX+5, centroBrazoY+5,
				centroBrazoX+5, centroBrazoY-5,
				centroBrazoX-5, centroBrazoY-5
			}
		);
	}
	
	public void actualizarAtaque() {
		fueAtacado = false;
	}

	public void mover(float x) {
		nHitbox.setPosition(x, nHitbox.getY());
		hitboxBrazo.setPosition(x, hitboxBrazo.getY());
		if (nombre.equals("Jugador 1")) {
			hitboxCabeza.setPosition(x+5*escala, hitboxCabeza.getY());
			hitboxPecho.setPosition(x+4*escala, hitboxPecho.getY());
			hitboxSegundaMano.setPosition(x+12*escala, hitboxSegundaMano.getY());
			hitboxPiernas.setPosition(x+3*escala, hitboxPiernas.getY());
			hitboxPie.setPosition(x+13*escala, hitboxPie.getY());
		} else {
			hitboxCabeza.setPosition(x+7*escala, hitboxCabeza.getY());
			hitboxPecho.setPosition(x+5*escala, hitboxPecho.getY());
			hitboxSegundaMano.setPosition(x+segundaManoX, hitboxSegundaMano.getY());
			hitboxPiernas.setPosition(x+4*escala, hitboxPiernas.getY());
			hitboxPie.setPosition(x+2*escala, hitboxPie.getY());
		}
		
		centroHitboxBrazo.setPosition(x, hitboxBrazo.getY());
		if (nombre.equals("Jugador 1")) centroBrazoX = x + 3.5f*escala;
		else centroBrazoX = x + (nHitbox.getWidth() - 3.5f*escala);
		
		//hitboxBrazo.setRotation(calcularGradoBrazo());
		
		spriteBrazo.setPosition(centroBrazoX, centroBrazoY-5);
		//spriteBrazo.setRotation(hitboxBrazo.getRotation());
	}
	
	public void moverAnguloBrazo(float anguloBrazo) {
		hitboxBrazo.setRotation(anguloBrazo);
		spriteBrazo.setRotation(anguloBrazo);
	}

	public TextureRegion getFrameActual(Movimiento mov) {
		switch (mov) {
		case QUIETO:
			frameAcutal = animacionIdle.getKeyFrame(tiempo, true);
			hitboxCabeza.height = 6*escala;
			hitboxSegundaMano.width = 5*escala;
			if (nombre.equals("Jugador 2")) segundaManoX = 0; 
			hitboxPie.width = 2*escala;
			hitboxPie.height = 5*escala;
			break;
		case ADELANTE:
			frameAcutal = animacionWalk.getKeyFrame(tiempo, true);
			// Ajusta tamaño de hitbox para tener una mejor representacion en la animacion
			hitboxCabeza.height = 7*escala;
			hitboxSegundaMano.width = 2*escala;
			if (nombre.equals("Jugador 2")) segundaManoX = 3*escala; 
			hitboxPie.width = 0;
			hitboxPie.height = 0;
			break;
		case ATRAS:
			frameAcutal = animacionBackWalk.getKeyFrame(tiempo, true);
			hitboxCabeza.height = 7*escala;
			hitboxSegundaMano.width = 2*escala;
			if (nombre.equals("Jugador 2")) segundaManoX = 3*escala; 
			hitboxPie.width = 0;
			hitboxPie.height = 0;
			break;
		default:
			frameAcutal = null;
			break;
		}
		
		return frameAcutal;
	}
	
	private void cargarAnimacion(Texture textura, int spriteColumnas, int spriteFilas, String tipo, boolean isReverse) {
		// Se inicializan las variables temporales
		int ancho = textura.getWidth();
		int alto = textura.getHeight();
		TextureRegion[] regionMovimiento;
		Animation<TextureRegion> animacion;
		
		TextureRegion[][] temp = TextureRegion.split(textura, ancho/spriteColumnas, alto/spriteFilas);
		regionMovimiento = new TextureRegion[spriteColumnas];
		if (!isReverse) {
			int indice = 0;
			for (int i = 0; i < spriteFilas; i++) {
				for (int j = 0; j < spriteColumnas; j++) {
					regionMovimiento[indice++] = temp[i][j];
				}
			};
		} else {
			int indice = spriteColumnas-1;
			for (int i = 0; i < spriteFilas; i++) {
				for (int j = 0; j < spriteColumnas; j++) {
					regionMovimiento[indice--] = temp[i][j];
				}
			};
		}
				
		animacion = new Animation((tipo.equals("idle") ? 0.32f : 0.17f), regionMovimiento);
		
		// Se asignan los valores de las variables temporales a sus varibles fijas y unicas para cada tipo de animacion
		if (tipo.equals("idle")) {
			animacionIdle = animacion;
		} else if (tipo.equals("walk")) {
			animacionWalk = animacion;
		} else if (tipo.equals("backWalk")) {
			animacionBackWalk = animacion;
		}
		
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
	
	
	public Sprite getSpriteBrazo() {
		return spriteBrazo;
	}
	
	public int getNumero() {
		return numero;
	}

	public void setMovimientoActual(Movimiento mov) {
		movimientoActual = mov;	
	}
	
	public Movimiento getMovimientoActual() {
		return movimientoActual;
	}

	public void setVida(int vida) {
		this.vida = vida;
		
	}

	
}
