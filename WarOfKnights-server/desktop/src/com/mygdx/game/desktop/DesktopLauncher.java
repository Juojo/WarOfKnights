package com.mygdx.game.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.JuegoPeleas;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = 1280;
		cfg.height = 720;
		cfg.title = "SERVER";
		// War of Knights, Steel Strike
		cfg.backgroundFPS = 60;
		cfg.foregroundFPS = 60;
		cfg.addIcon("icono/icono_32.png", FileType.Internal);
		cfg.addIcon("icono/icono_64.png", FileType.Internal);
		cfg.addIcon("icono/icono_128.png", FileType.Internal);
		new LwjglApplication(new JuegoPeleas(), cfg);
	}
}
