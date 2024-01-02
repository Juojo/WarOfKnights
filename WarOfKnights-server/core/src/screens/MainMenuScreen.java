package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.JuegoPeleas;

import utilidades.Boton;

public class MainMenuScreen extends Pantalla {

	final JuegoPeleas game;
	private int[] layersToRender = {0, 1};
	
	private Stage stage;
	private Image logo; 
	
	private Table tablaPrincipal;

	public MainMenuScreen(final JuegoPeleas game) {
		this.game = game;
	}
	
	@Override
	public void show() {
		logo = new Image(new Texture("myAssets/logo.png"));
		
		// Se crea el stage
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		// Se crea la tabla que va a contener los botones 
		tablaPrincipal = new Table();
		tablaPrincipal.center();
		tablaPrincipal.setFillParent(true);
        
		// Se crean los botones
        Boton botonJugar = new Boton("     Jugar     ", Color.DARK_GRAY);
        Boton botonSalir = new Boton("     Salir     ", Color.DARK_GRAY);
        
        // Se agregan los botones a la tabla
        tablaPrincipal.add(logo);
        tablaPrincipal.row();
        tablaPrincipal.add(botonJugar.getBoton());
        tablaPrincipal.row();
        tablaPrincipal.add(botonSalir.getBoton());
        
        // Se agrega la tabla con los botones al stage
        stage.addActor(tablaPrincipal);
	}

	@Override
	public void render(float delta) {
//		Gdx.gl.glClearColor(0.4706f, 0.2706f, 0.9216f, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//		camara.update();
//		renderer.setView(camara);
//		renderer.render(layersToRender);
//		game.batch.setProjectionMatrix(camara.combined);
//
//		// Se renderiza el stage
//		stage.draw();
//
//		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//			game.setScreen(new GameScreen(game));
//			dispose();
//		}	
		//game.setScreen(new GameScreen(game));
	}
	
	@Override
	public void dispose() {
		mapa.dispose();
		stage.dispose();
	}
}

