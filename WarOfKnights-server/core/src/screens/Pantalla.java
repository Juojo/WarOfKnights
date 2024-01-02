package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class Pantalla implements Screen {

	protected OrthographicCamera camara;
	protected Viewport vp;
	protected TiledMap mapa;
	protected OrthogonalTiledMapRenderer renderer;
	final public int LARGO = 1280;
	final public int ALTO = 720;
	
	public Pantalla() {
		camara = new OrthographicCamera();
		camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// ?? se puede reemplazar por: camara = new OrthographicCamera(vpLargo, vpAlto);
		
		vp = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camara);
		mapa = new TmxMapLoader().load("myAssets/mapa3/mapa.tmx");
		renderer = new OrthogonalTiledMapRenderer(mapa, 2f);
		camara.update();
	}
	
	@Override
	public void resize(int width, int height) {
		camara.viewportWidth = width;
		camara.viewportHeight = height;
		vp.update(width, height);
	}
	
	@Override
	public void render(float delta) {
		
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}	
	
	@Override
	public void resume() {
		
	}

	@Override
	public void show() {
		
	}
}
