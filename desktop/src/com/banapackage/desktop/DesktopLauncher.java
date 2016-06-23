package com.banapackage.desktop;

import main.Bana;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title ="Bana";
		cfg.width = Bana.SCREENWIDTH ;
		cfg.height= Bana.SCREENHEIGHT;
		new LwjglApplication(new Bana(), cfg);
	}
}