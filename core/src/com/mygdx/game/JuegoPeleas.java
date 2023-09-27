package com.mygdx.game;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import escenas.Hud;
import jugadores.Caballero;
import jugadores.J1;
import jugadores.J2;

public class JuegoPeleas extends ApplicationAdapter {
	private SpriteBatch batch;
	private Caballero j1;
	private Caballero j2;
	private BitmapFont font;
	// Mapa
	private TiledMap mapa;
	private OrthogonalTiledMapRenderer renderer;
	// Camara
	private OrthographicCamera camara;
	private Viewport vp;
	
	private Hud hud;
	
	private int vpLargo, vpAlto;
	
	private int escala;
	private int largoPiso = 1536;
	private int margenIzquierda = 0;
	
	private boolean devTools = true;
	
	private NumberFormat nf = new DecimalFormat("##.#");
	
	@Override
	public void create () {
		vpLargo = Gdx.graphics.getWidth(); 
		vpAlto = Gdx.graphics.getHeight();
		
		font = new BitmapFont();
		batch = new SpriteBatch();
		j1 = new J1();
		j2 = new J2();
		
		escala = j1.getEscala();
		
		margenIzquierda = largoPiso/2 - (largoPiso - vpLargo) / 2;
		// Calculo:
			// Largo del tilemap / 2 para sacar el medio - (el largo del tilemap - el largo del viewport) / 2 para repartir en cada lado el margen
			//
			// largoPiso/2 - (largoPiso - largo) / 2 = 640
		
		TmxMapLoader cargadorTmx = new TmxMapLoader();
		mapa = cargadorTmx.load("tilemap/mapajuego.tmx");
		renderer = new OrthogonalTiledMapRenderer(mapa);
		
		camara = new OrthographicCamera(vpLargo, vpAlto);
		camara.position.set(largoPiso/2, camara.viewportHeight / 2f, 0);
		camara.zoom = 1;
		camara.update();
		vp = new FitViewport(vpLargo, vpAlto, camara);
		
		hud = new Hud(vpLargo, vpAlto, batch);
		
	}
	

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		j1.setTiempo(Gdx.graphics.getDeltaTime()); // Tiempo que pasa desde el ultimo frame (Latencia)
		
		verificarLimitesCamara();
		camara.update();
		
		renderer.setView(camara);
		renderer.render();

		hud.stage.draw();
		hud.update(Gdx.graphics.getDeltaTime(), Gdx.graphics.getFramesPerSecond(), j1, j2, camara, devTools);
		
		batch.begin();
			batch.setProjectionMatrix(hud.stage.getCamera().combined);
			batch.setProjectionMatrix(camara.combined);
			renderizarJuego();
			procesarEntrada();
		batch.end();
		
	}
	
	private void renderizarJuego() {
		if (devTools) {
			j1.hitbox.draw(batch);
		}
		batch.draw(j2.region, 1000, 198, 0, 0, j2.region.getRegionWidth(), j2.region.getRegionHeight(), 2.5f, 2.5f, 0);
	}
	
	@Override
	public void resize(int width, int height) {
		camara.viewportWidth = width;
		camara.viewportHeight = height;
		vp.update(width, height);
	}
	
	private void verificarLimitesCamara() {
		if (j1.hitbox.getX() < margenIzquierda) {
			// Por izquierda
			camara.position.x = margenIzquierda;
		} else if (j1.hitbox.getX() > largoPiso/2 + 128) {
			// Por derecha
			camara.position.x = (largoPiso + largoPiso/2) - vpLargo - (largoPiso - vpLargo) / 2;
		} else {
			// Medio (seguimiento comun)
			camara.position.set(j1.hitbox.getX(), camara.viewportHeight / 2f, 0);
		}
	}

	private void procesarEntrada() {
		boolean izquierda = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
		boolean derecha = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

		if (Gdx.input.isKeyJustPressed(Input.Keys.O) && !devTools) {
			devTools = true;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.O) && devTools ) {
			devTools = false;
		}

		float x = j1.miCaballero.getX();
		final int VELOCIDAD = 250;
		
		if (derecha && !izquierda && j1.hitbox.getX() < largoPiso - j1.hitbox.getWidth()) {
			x += VELOCIDAD * Gdx.graphics.getDeltaTime();
			batch.draw(j1.getFrameActual("run"), x-9, 223, (j1.getLargo()/10)*escala, j1.getAlto()*escala);
		} else if (izquierda && !derecha && j1.hitbox.getX() > 0) {
			x -= VELOCIDAD * Gdx.graphics.getDeltaTime();
			batch.draw(j1.getFrameActual("run"), x+(j1.getLargo()/10)*escala-24, 223, 0, 0, (j1.getLargo()/10)*escala, j1.getAlto()*escala, -1, 1, 0);
		} else if (
				(!izquierda && !derecha)
				|| (izquierda && derecha)
				|| (izquierda && j1.hitbox.getX() <= 0)
				|| (derecha && j1.hitbox.getX() > largoPiso - j1.hitbox.getWidth())
			) {
			batch.draw(j1.getFrameActual("idle"), x, 223, (j1.getLargo()/10)*escala, j1.getAlto()*escala);
		}
		
		j1.mover(x);
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		mapa.dispose();
		renderer.dispose();
	}
}
