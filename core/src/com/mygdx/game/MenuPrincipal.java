import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuPrincipal implements Screen {
    
	private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("C:\Users\Nahele\Desktop\assets menu\default.png");
		
    public MenuPrincipal() {
        stage = new Stage(new FitViewport(800, 480)); // Tamaño de la pantalla
        Gdx.input.setInputProcessor(stage);

		Label Label = new label("Bienvenido al videojuego de peleas", skin);
		label setPosition(400, 400);
		stage.addActor(label);

        TextButton playButton = new TextButton("Jugar", skin); // skin es tu Skin de estilo
        playButton.setPosition(400, 140);
		playButton.setSize(100,50);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar a la pantalla de juego o realizar la acción deseada
                game.setScreen(new GameScreen());
            }
        });

        stage.addActor(playButton);
		
		
		TextButton BotonSalir = new TextButton("Salir", skin); // skin es tu Skin de estilo
        BotonSalir.setPosition(400, 240);
		BotonSalir.setSize(100, 50);
        BotonSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar a la pantalla de juego o realizar la acción deseada
                Gdx.app.exit());
            }
		
		stage.addActor(BotonSalir);
			
        });				
    }




    @Override
    public void show() {
        // No se necesita nada aquí en este ejemplo
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    // Implementa otros métodos de la interfaz Screen según sea necesario

    @Override
    public void dispose() {
        stage.dispose();
    }
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}