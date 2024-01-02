package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.JuegoPeleas;

import red.HiloCliente;
import utilidades.Global;

public class PantallaEspera extends Pantalla {

	private final JuegoPeleas game;
	private HiloCliente hc;
	
	private Stage stage;
	private Table tablaPrincipal;
	
	public PantallaEspera (final JuegoPeleas game) {
		this.game = game;
	}
	
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		tablaPrincipal = new Table();
		tablaPrincipal.center();
		tablaPrincipal.setFillParent(true);
		
		Label mensajeLabel = new Label("Esperando jugador", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		
		tablaPrincipal.add(mensajeLabel).expandX();
		
		stage.addActor(tablaPrincipal);
		
		hc = new HiloCliente();
        hc.start();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.4706f, 0.6706f, 0.9216f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		
		if (Global.empieza) {
			game.setScreen(new GameScreen(game , hc));
			dispose();
		}
	}
	
}
