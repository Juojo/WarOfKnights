package escenas;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import jugadores.Caballero;

public class Hud {
	public Stage stage;
	public Viewport viewport;
	
	private float contadorTiempo;
	private int tiempoPartida;
	
	Label vidaJ1Label;
	Label vidaJ1ValorLabel;
	Label vidaJ2Label;
	Label vidaJ2ValorLabel;
	Label tiempoLabel;
	Label tiempoPartidaLabel;
	
	// Dev tools
	private NumberFormat nf = new DecimalFormat("##.#");
	private String devToolsText = "";
	Label avisoDevToolsLabel;
	Label devToolsLabel;
	
	
	public Hud(int vpLargo, int vpAlto, SpriteBatch sb) {
		tiempoPartida = 99;
		
		viewport = new FitViewport(vpLargo, vpAlto, new OrthographicCamera());
		stage = new Stage(viewport, sb);
		
		crearTablaMarcador();
		crearTablaDevTools();
		
	}
	
	private void crearTablaMarcador() {
		Table tabla = new Table();
		tabla.top();
		tabla.setFillParent(true);
		
		// VIDA JUGADOR 1
		vidaJ1Label = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		vidaJ1ValorLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		
		// TIEMPO
		tiempoLabel = new Label("TIEMPO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		tiempoPartidaLabel = new Label(String.format("%02d", tiempoPartida), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		
		// VIDA JUGADOR 2
		vidaJ2Label = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		vidaJ2ValorLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		
		tabla.add(vidaJ1Label).expandX().padTop(10);
		tabla.add(tiempoLabel).expandX().padTop(10);
		tabla.add(vidaJ2Label).expandX().padTop(10);
		tabla.row();
		tabla.add(vidaJ1ValorLabel).expandX().padTop(5);
		tabla.add(tiempoPartidaLabel).expandX().padTop(5);
		tabla.add(vidaJ2ValorLabel).expandX().padTop(5);
		
		stage.addActor(tabla);
		
	}

	private void crearTablaDevTools() {
		Table tablaDevTools = new Table();
		tablaDevTools.bottom();
		tablaDevTools.setFillParent(true);
		
		avisoDevToolsLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		devToolsLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		
		tablaDevTools.add(devToolsLabel).expandX().padBottom(10).padLeft(10).align(Align.left);
		tablaDevTools.row();
		tablaDevTools.add(avisoDevToolsLabel).expandX().padBottom(10).padLeft(10).align(Align.left);
		
		stage.addActor(tablaDevTools);
		
	}

	public void update(float deltaTime, int fps, Caballero j1, Caballero j2, OrthographicCamera camaraJuego, boolean devTools) {
		// Setea el nombre de cada jugador (una unica vez)
		if (contadorTiempo == 0.0f) {
			vidaJ1Label.setText("Vida " + j1.getNombre());
			vidaJ2Label.setText("Vida " + j2.getNombre());
		}
		
		
		// Actualiza el timer
		contadorTiempo += deltaTime;
		while (contadorTiempo >= 1) {
			tiempoPartida--;
			contadorTiempo -= 1;
		}
		tiempoPartidaLabel.setText(String.format("%02d", tiempoPartida));
		
		
		// Actualiza devTools
		//avisoDevToolsLabel.setText("(presione la tecla O para " + ((devTools) ? "desactivar" : "activar") + " las herramientas de desarrollo)");
		if (devTools) {
			devToolsText = "FPS: " + fps + "\n"
					+ "MOUSE X: " + Gdx.input.getX() + " MOUSE Y: " + Gdx.input.getY() + "\n"
					//+ "JUGADOR X: " + nf.format(j1..getX()+132) + " (REAL: " + nf.format(j1.miCaballero.getX()) + ")\n"
					+ "nHITBOX X: " + nf.format(j1.nHitbox.getX()) + " (X der: " + nf.format(j1.nHitbox.getX()+j1.nHitbox.getWidth()) + ") \n"
					+ "centro X: " + nf.format(j1.centroBrazoX) + " centro y: " + nf.format(j1.centroBrazoY) + "\n"					
					+ "CAMARA X: " + nf.format(camaraJuego.position.x) + " CAMARA Y: " + nf.format(camaraJuego.position.x);
		} else {
			devToolsText = "";
		}
		devToolsLabel.setText(devToolsText);
		
		
		// Actualiza vida jugadores
		vidaJ1ValorLabel.setText(String.format(j1.getVida() + "  [ " + j1.getArmadura() + " ]"));
		vidaJ2ValorLabel.setText(String.format(j2.getVida() + "  [ " + j2.getArmadura() + " ]")); // CAMBIAR POR JUGADOR 2 CUANDO SE PROGRAME LA CLASE
	}

	public int getTiempoPartida() {
		return tiempoPartida;
		
	}

	public void setTiempoPartida(int tiempoPartida) {
		this.tiempoPartida = tiempoPartida;
		
	}
}
