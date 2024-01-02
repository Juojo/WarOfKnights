package utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.mygdx.game.JuegoPeleas;

public class Boton {

	private TextButton button;
	private TextButtonStyle textButtonStyle;
	private BitmapFont font;
	private Skin skin;
	private TextureAtlas buttonAtlas;
    
	
	public Boton(String texto, Color colorTexto, JuegoPeleas game) {
        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("button.pack"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = colorTexto;
        textButtonStyle.up = skin.getDrawable("buttonnormal");
        textButtonStyle.down = skin.getDrawable("buttonpressed");
        button = new TextButton(texto, textButtonStyle);
	}
	
	public TextButton getBoton() {
		return button;
	}
	
}
