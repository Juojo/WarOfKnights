package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.JuegoPeleas;

import red.HiloCliente;
import utilidades.Boton;
import utilidades.Global;

public class PantallaGanador extends Pantalla {

	private final JuegoPeleas game;
	private HiloCliente hc;
	
	private Stage stage;
	private Table tablaPrincipal;
	
	public PantallaGanador (final JuegoPeleas game, HiloCliente hc) {
		this.game = game;
		this.hc = hc;
		
		
		//hc.reiniciar(); // Reinicia el cliente
	}
	
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		tablaPrincipal = new Table();
		tablaPrincipal.center();
		tablaPrincipal.setFillParent(true);
		
		String msgGanador = "";
		if (Global.ganador == 1) {
			msgGanador = "Gana el jugador VERDE";
		} else if (Global.ganador == 2) {
			msgGanador = "Gana el jugador ROJO";
		} else if (Global.ganador == 3) {
			msgGanador = "La partida queda en empate";
		}
		
		Label mensajeLabel = new Label(msgGanador, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		
		// Se crean los botones
        Boton botonVolverMenu = new Boton("     Volver al menu     ", Color.DARK_GRAY, game);
        Boton botonSalir = new Boton("     Salir     ", Color.DARK_GRAY, game);
        
        botonVolverMenu.getBoton().addListener( new ClickListener() {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		game.setScreen(new MainMenuScreen(game));
        		Global.ganador = -1;
        		Global.empieza = false;
        		System.out.println("Pantalla ganador envia reinicio");
        		//hc.enviarMensaje("Reiniciar"); // Reinicia el server
				dispose();
        	}
        });
        botonSalir.getBoton().addListener( new ClickListener() {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		Gdx.app.exit();
        		dispose();
				game.batch.dispose();
        	}
        });
        
        // Se agregan los botones a la tabla
        tablaPrincipal.add(mensajeLabel).expandX();
        tablaPrincipal.row();
        tablaPrincipal.add(botonVolverMenu.getBoton());
        tablaPrincipal.row();
        tablaPrincipal.add(botonSalir.getBoton());
		
		stage.addActor(tablaPrincipal);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.4706f, 0.6706f, 0.9216f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		
	}
	
}
