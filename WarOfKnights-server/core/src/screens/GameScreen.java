package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.JuegoPeleas;

import escenas.Hud;
import jugadores.Caballero;
import jugadores.J1;
import jugadores.J2;
import red.HiloServidor;
import utilidades.Global;

public class GameScreen extends Pantalla {

	final JuegoPeleas game;
	private HiloServidor hs;
	
	// Jugadores
	private Caballero j1;
	private Caballero j2;
	
	
	private Hud hud;
	
	private int vpLargo, vpAlto;
	
	private int escala;
	private int largoPiso = 1536;
	private int margenIzquierda = 0;
	
	private boolean devTools = true;
	
	private ShapeRenderer shapeRenderer;
	
	private boolean estaSiendoAtacado = false;
	long tiempoQueNoColisiona;
	
	public boolean izquierda1;
	public boolean derecha1;
	public boolean izquierda2;
	public boolean derecha2;
	
	public GameScreen(final JuegoPeleas game, HiloServidor hs) {
		this.game = game;
		this.hs = hs;
		
		hs.setApp(this);
	}
	
	public void show() {
		vpLargo = Gdx.graphics.getWidth(); 
		vpAlto = Gdx.graphics.getHeight();
		
		j1 = new J1();
		j2 = new J2();
		
		escala = j1.getEscala();
		
		margenIzquierda = largoPiso/2 - (largoPiso - vpLargo) / 2;

		camara.position.set(vpLargo/2, camara.viewportHeight / 2f, 0f);
		camara.zoom = 1;
		camara.update();
		
		hud = new Hud(vpLargo, vpAlto, game.batch);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(super.camara.combined);
		j2.getSpriteBrazo().flip(false, true);
		
		// Se restablecen los jugadores y el tiempo
		
	}
	
	
	
	public void render(float delta) {
		Gdx.gl.glClearColor(0.4706f, 0.2706f, 0.9216f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camara.update();
		renderer.setView(camara);
		renderer.render();

		if (Global.empieza) {
			//hud.stage.draw();
			hud.update(Gdx.graphics.getDeltaTime(), Gdx.graphics.getFramesPerSecond(), j1, j2, camara, devTools);
			
			
			procesarAtaque(j1, j2); // De J1 a J2 
			procesarAtaque(j2, j1); // De J2 a J1
			
			
//			game.batch.begin();
//				game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
//				game.batch.setProjectionMatrix(camara.combined);
//				
//				j1.getSpriteBrazo().draw(game.batch);
//				j2.getSpriteBrazo().draw(game.batch);
//			game.batch.end();
			procesarEntradas();
			if (devTools) renderDevTools();
			
			hs.enviarMensajeATodos("Actualizar#J1X#" + j1.nHitbox.getX());
			hs.enviarMensajeATodos("Actualizar#J2X#" + j2.nHitbox.getX());
			
			hs.enviarMensajeATodos("Actualizar#J1Brazo#" + j1.calcularGradoBrazo());
			hs.enviarMensajeATodos("Actualizar#J2Brazo#" + j2.calcularGradoBrazo());
			
			hs.enviarMensajeATodos("Actualizar#Reloj#" + hud.getTiempoPartida());
			
			hs.enviarMensajeATodos("Actualizar#VidaJ1#" + j1.getVida());
			hs.enviarMensajeATodos("Actualizar#VidaJ2#" + j2.getVida());
			
			if (hud.getTiempoPartida() <= 0) {
				int n = -1;
				if (j1.getVida() > j2.getVida()) {
					n = 1;
				} else if (j1.getVida() < j2.getVida()){
					n = 2;
				} else {
					n = 3;
				}
				hs.enviarMensajeATodos("Ganador#J"+n);
				//Global.empieza = false;
				hs.reiniciar();
				System.out.println("Se reinician valores del juego");
				j1.reiniciarJuego();
				j2.reiniciarJuego();
				hud.setTiempoPartida(99);
			} else if (j1.getVida() <= 0) {
				hs.enviarMensajeATodos("Ganador#J2");
				//Global.empieza = false;
				hs.reiniciar();
				System.out.println("Se reinician valores del juego");
				j1.reiniciarJuego();
				j2.reiniciarJuego();
				hud.setTiempoPartida(99);
			} else if (j2.getVida() <= 0) {
				hs.enviarMensajeATodos("Ganador#J1");
//				Global.empieza = false;
//				hs.setFin(true);
				hs.reiniciar();
				System.out.println("Se reinician valores del juego");
				j1.reiniciarJuego();
				j2.reiniciarJuego();
				hud.setTiempoPartida(99);
			}
		}
		
	}

	private void procesarAtaque(Caballero jAtaca, Caballero jRecibe) {
		if (detectarColisionaPR(jAtaca.hitboxBrazo, jRecibe.hitboxCabeza)) jAtaca.ejecutarAtaque(jRecibe, 15);
		else if (detectarColisionaPR(jAtaca.hitboxBrazo, jRecibe.hitboxPecho)) jAtaca.ejecutarAtaque(jRecibe, 10);
		else if (detectarColisionaPR(jAtaca.hitboxBrazo, jRecibe.hitboxPiernas)) jAtaca.ejecutarAtaque(jRecibe, 8);
		else if (detectarColisionaPR(jAtaca.hitboxBrazo, jRecibe.hitboxPie) || detectarColisionaPR(jAtaca.hitboxBrazo, jRecibe.hitboxSegundaMano)) jAtaca.ejecutarAtaque(jRecibe, 5);
		else jAtaca.actualizarAtaque();	
	}

	
	private boolean detectarColisionaPR(Polygon poligono, Rectangle rectangulo) {
        float[] verticesPoligono = poligono.getTransformedVertices();
        float[] verticesRectangulo = {
                rectangulo.x, rectangulo.y,
                rectangulo.x + rectangulo.width, rectangulo.y,
                rectangulo.x + rectangulo.width, rectangulo.y + rectangulo.height,
                rectangulo.x, rectangulo.y + rectangulo.height
        };
        
        return Intersector.overlapConvexPolygons(verticesPoligono, verticesRectangulo, null); 
    }
	
	private void renderDevTools() {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(j1.nHitbox.x, j1.nHitbox.y, j1.nHitbox.width, j1.nHitbox.height);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(j2.nHitbox.x, j2.nHitbox.y, j2.nHitbox.width, j2.nHitbox.height);
		shapeRenderer.end();
		
		// Brazos
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(Color.PINK);
			shapeRenderer.polygon(j1.hitboxBrazo.getTransformedVertices());
			shapeRenderer.polygon(j2.hitboxBrazo.getTransformedVertices());
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);
		renderRectangulo(j1.hitboxCabeza);
		renderRectangulo(j1.hitboxPecho);
		renderRectangulo(j1.hitboxSegundaMano);
		renderRectangulo(j1.hitboxPiernas);
		renderRectangulo(j1.hitboxPie);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		renderRectangulo(j2.hitboxCabeza);
		renderRectangulo(j2.hitboxPecho);
		renderRectangulo(j2.hitboxSegundaMano);
		renderRectangulo(j2.hitboxPiernas);
		renderRectangulo(j2.hitboxPie);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.polygon(j1.centroHitboxBrazo.getTransformedVertices());
			shapeRenderer.polygon(j2.centroHitboxBrazo.getTransformedVertices());
		shapeRenderer.end();
		
	}

	private void renderRectangulo(Rectangle rect) {
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
	}

	private void procesarEntradas() {
		boolean izquierdaJP = Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
		boolean derechaJP = Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);
		boolean oJP = Gdx.input.isKeyJustPressed(Input.Keys.O);
		
		procesarDevTools(oJP);
		procesarMovimiento(derecha1, izquierda1, derecha2, izquierda2);
	}
	
	private void procesarDevTools(boolean oJustPressed) {
		if (oJustPressed && !devTools) {
			devTools = true;
		} else if (oJustPressed && devTools ) {
			devTools = false;
		}
	}

	
	private void procesarMovimiento(boolean derecha1, boolean izquierda1, boolean derecha2, boolean izquierda2) {
		float x1 = j1.nHitbox.getX();
		float x2 = j2.nHitbox.getX();
		final int VELOCIDAD = 250; //250
		
		
		// J1
		if (!j1.nHitbox.overlaps(j2.nHitbox) && j1.nHitbox.getX()+j1.nHitbox.getWidth() < vpLargo) {
			if (derecha1) x1 += VELOCIDAD * Gdx.graphics.getDeltaTime();
		}
		if (j1.nHitbox.getX() > 0) {
			if (izquierda1) x1 -= VELOCIDAD * Gdx.graphics.getDeltaTime();
		}
		// J2
		if (j2.nHitbox.getX()+j2.nHitbox.getWidth() < vpLargo) {
			if (derecha2) x2 += VELOCIDAD * Gdx.graphics.getDeltaTime();
		}
		if (!j2.nHitbox.overlaps(j1.nHitbox) && j2.nHitbox.getX() > 0) {
			if (izquierda2) x2 -= VELOCIDAD * Gdx.graphics.getDeltaTime();
		}
		
//		if (derecha1 && !izquierda1 ) {
//			
//				// J1
//				if (!j1.nHitbox.overlaps(j2.nHitbox)) {
//					if (j1.nHitbox.getX()+j1.nHitbox.getWidth() < vpLargo) {
//						x1 += VELOCIDAD * Gdx.graphics.getDeltaTime();
//						//game.batch.draw(j1.getFrameActual(Movimiento.ADELANTE), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//					} else if (j1.nHitbox.getX()+j1.nHitbox.getWidth() >= vpLargo) {
//						x1 += 0;
//						//game.batch.draw(j1.getFrameActual(Movimiento.QUIETO), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//					}
//				} //else //game.batch.draw(j1.getFrameActual(Movimiento.QUIETO), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//		}
//		
//		else if (derecha2 && !izquierda2) {
//			
//				// J2
//				if (j2.nHitbox.getX()+j2.nHitbox.getWidth() < vpLargo) {
//					x2 += VELOCIDAD * Gdx.graphics.getDeltaTime();
//					//game.batch.draw(j2.getFrameActual(Movimiento.ATRAS), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//				} else if (j2.nHitbox.getX()+j2.nHitbox.getWidth() >= vpLargo) {
//					x2 += 0;
//					//game.batch.draw(j2.getFrameActual(Movimiento.QUIETO), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//				}
//				
//		}
//		
//		else if (izquierda1 && !derecha1) {
//			
//			// J1
//			if (j1.nHitbox.getX() > 0) {
//				x1 -= VELOCIDAD * Gdx.graphics.getDeltaTime();
//				//game.batch.draw(j1.getFrameActual(Movimiento.ATRAS), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//			} else if (j1.nHitbox.getX() <= 0) {
//				x1 -= 0;
//				//game.batch.draw(j1.getFrameActual(Movimiento.QUIETO), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//			}
//		}
//		
//		else if (izquierda2 && !derecha2) {
//			
//			// J2
//			if (!j2.nHitbox.overlaps(j1.nHitbox)) {
//				if (j2.nHitbox.getX() > 0) {
//					x2 -= VELOCIDAD * Gdx.graphics.getDeltaTime();
//					//game.batch.draw(j2.getFrameActual(Movimiento.ADELANTE), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//				} else if (j2.nHitbox.getX() <= 0) {
//					x2 -= 0;
//					//game.batch.draw(j2.getFrameActual(Movimiento.QUIETO), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//				} else if (j2.nHitbox.overlaps(j1.nHitbox)) {
//					//game.batch.draw(j2.getFrameActual(Movimiento.QUIETO), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//				}
//			} //else game.batch.draw(j2.getFrameActual(Movimiento.QUIETO), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//			
//		}
//		
//		else {
//			//game.batch.draw(j1.getFrameActual(Movimiento.QUIETO), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//			//game.batch.draw(j2.getFrameActual(Movimiento.QUIETO), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//		}
		
		j1.mover(x1);
		j2.mover(x2);
	}

	
	public void dispose() {
		game.batch.dispose();
		game.font.dispose();
		shapeRenderer.dispose();
		mapa.dispose();
		renderer.dispose();
	}

	public Caballero getJugador(int jugador) {
		return (jugador == 1) ? j1 : j2;
	}
	
}
