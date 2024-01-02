package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.JuegoPeleas;

import escenas.Hud;
import jugadores.Caballero;
import jugadores.J1;
import jugadores.J2;
import red.HiloCliente;
import utilidades.Global;

public class GameScreen extends Pantalla {

	final JuegoPeleas game;
	private HiloCliente hc;
	
	// Jugadores
	private Caballero j1;
	private Caballero j2;

	private Hud hud;
	
	private int vpLargo, vpAlto;
	
	private int escala;
	public int largoPiso = 1536;
	
	private boolean devTools = false;
	
	private ShapeRenderer shapeRenderer;
	
	long tiempoQueNoColisiona;
	
	public GameScreen(final JuegoPeleas game, HiloCliente hc) {
		this.game = game;
		this.hc = hc;
		
		hc.setApp(this);
	}
	
	public void show() {
		vpLargo = Gdx.graphics.getWidth(); 
		vpAlto = Gdx.graphics.getHeight();
		
		j1 = new J1();
		j2 = new J2();
		
		
		
		
		escala = j1.getEscala();

		camara.position.set(vpLargo/2, camara.viewportHeight / 2f, 0f);
		camara.zoom = 1;
		camara.update();
		
		hud = new Hud(vpLargo, vpAlto, game.batch);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(super.camara.combined);
		j2.getSpriteBrazo().flip(false, true);
		
	}
	
	
	
	public void render(float delta) {
		Gdx.gl.glClearColor(0.4706f, 0.6706f, 0.9216f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (Global.ganador == -1) {
			j1.setTiempo(Gdx.graphics.getDeltaTime()); // Tiempo que pasa desde el ultimo frame (Latencia)
			j2.setTiempo(Gdx.graphics.getDeltaTime());

			
			camara.update();
			renderer.setView(camara);
			renderer.render();

			hud.update(Gdx.graphics.getFramesPerSecond(), j1, j2, devTools);
			hud.stage.draw();
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(hud.getBarraVida(1).getX(), hud.getBarraVida(1).getY(), hud.getBarraVida(1).getWidth(), hud.getBarraVida(1).getHeight());
			shapeRenderer.rect(hud.getBarraVida(2).getX(), hud.getBarraVida(2).getY(), hud.getBarraVida(2).getWidth(), hud.getBarraVida(2).getHeight());
			shapeRenderer.end();
			
			game.batch.begin();
				game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
				game.batch.setProjectionMatrix(camara.combined);
				
				procesarEntradas();
				
				game.batch.draw(j1.getFrameActual(j1.getMovimientoActual()), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
				game.batch.draw(j2.getFrameActual(j2.getMovimientoActual()), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
				
				j1.getSpriteBrazo().draw(game.batch);
				j2.getSpriteBrazo().draw(game.batch);
			game.batch.end();
			
			if (devTools) renderDevTools();
		} else {
			game.setScreen(new PantallaGanador(game, hc));
			dispose();
		}
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
		boolean izquierda = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
		boolean derecha = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
		boolean oJP = Gdx.input.isKeyJustPressed(Input.Keys.O);
		
		procesarDevTools(oJP);
		procesarMovimiento(derecha, izquierda);
	}
	
	private void procesarDevTools(boolean oJustPressed) {
		if (oJustPressed && !devTools) {
			devTools = true;
		} else if (oJustPressed && devTools ) {
			devTools = false;
		}
	}

	
	private void procesarMovimiento(boolean derecha, boolean izquierda) {
		
		// Datos para el movimiento del jugador
		if (derecha && !izquierda) {
			hc.enviarMensaje("MoverDerecha");
		} else if (izquierda && !derecha) {
			hc.enviarMensaje("MoverIzquierda");
		} else {
			hc.enviarMensaje("Quieto");
			hc.enviarMensaje("NoMoverDerecha");
			hc.enviarMensaje("NoMoverIzquierda");
		}
		
		// Datos para el movimiento del brazo
		hc.enviarMensaje("Brazo#AltoPantalla#"+Gdx.graphics.getHeight());
		hc.enviarMensaje("Brazo#MouseX#"+Gdx.input.getX());
		hc.enviarMensaje("Brazo#MouseY#"+Gdx.input.getY());
		
//		if (derecha && !izquierda ) {
//			
//				// J1
//				if (!j1.nHitbox.overlaps(j2.nHitbox)) {
//					if (j1.nHitbox.getX()+j1.nHitbox.getWidth() < vpLargo) {
//						x1 += VELOCIDAD * Gdx.graphics.getDeltaTime();
//						game.batch.draw(j1.getFrameActual(Movimiento.ADELANTE), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//					} else if (j1.nHitbox.getX()+j1.nHitbox.getWidth() >= vpLargo) {
//						game.batch.draw(j1.getFrameActual(Movimiento.QUIETO), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//					}
//				} else game.batch.draw(j1.getFrameActual(Movimiento.QUIETO), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//				 
//				// J2
//				if (j2.nHitbox.getX()+j2.nHitbox.getWidth() < vpLargo) {
//					x2 += VELOCIDAD * Gdx.graphics.getDeltaTime();
//					game.batch.draw(j2.getFrameActual(Movimiento.ATRAS), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//				} else if (j2.nHitbox.getX()+j2.nHitbox.getWidth() >= vpLargo) {
//					game.batch.draw(j2.getFrameActual(Movimiento.QUIETO), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//				}
//				
//		} else if (izquierda && !derecha) {
//			
//			// J1
//			if (j1.nHitbox.getX() > 0) {
//				x1 -= VELOCIDAD * Gdx.graphics.getDeltaTime();
//				game.batch.draw(j1.getFrameActual(Movimiento.ATRAS), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//			} else if (j1.nHitbox.getX() <= 0) {
//				game.batch.draw(j1.getFrameActual(Movimiento.QUIETO), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//			}
//			
//			// J2
//			if (!j2.nHitbox.overlaps(j1.nHitbox)) {
//				if (j2.nHitbox.getX() > 0) {
//					x2 -= VELOCIDAD * Gdx.graphics.getDeltaTime();
//					game.batch.draw(j2.getFrameActual(Movimiento.ADELANTE), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//				} else if (j2.nHitbox.getX() <= 0) {
//					game.batch.draw(j2.getFrameActual(Movimiento.QUIETO), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//				} else if (j2.nHitbox.overlaps(j1.nHitbox)) {
//					game.batch.draw(j2.getFrameActual(Movimiento.QUIETO), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//				}
//			} else game.batch.draw(j2.getFrameActual(Movimiento.QUIETO), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//			
//		} else {
//			game.batch.draw(j1.getFrameActual(Movimiento.QUIETO), j1.nHitbox.getX(), j1.nHitbox.getY(), j1.getLargo()*escala, j1.getAlto()*escala);
//			game.batch.draw(j2.getFrameActual(Movimiento.QUIETO), j2.nHitbox.getX()+j2.nHitbox.getWidth(), j2.nHitbox.getY(), -j2.getLargo()*escala, j2.getAlto()*escala);
//		}
		
//		j1.mover(x1);
//		j2.mover(x2);
	}

	
	public void dispose() {
		//game.batch.dispose();
		game.font.dispose();
		shapeRenderer.dispose();
		mapa.dispose();
		renderer.dispose();
	}

	public Caballero getJugador(int jugador) {
		return (jugador == 1) ? j1 : j2;
	}
	
	public Hud getHud() {
		return hud;
	}
	
}