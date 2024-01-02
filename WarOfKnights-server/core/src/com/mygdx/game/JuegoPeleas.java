package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import screens.PantallaEspera;

public class JuegoPeleas extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	
	@Override
	public void create () {
		font = new BitmapFont();
		batch = new SpriteBatch();
		
		this.setScreen(new PantallaEspera(this));
	}
	
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
