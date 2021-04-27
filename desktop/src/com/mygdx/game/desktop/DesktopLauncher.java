package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MainLogic;

public class DesktopLauncher {
	public static void main (String[] arg) {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "ESTRUCTURA DE DATOS";
            config.width = 800;
            config.height = 600;
            new LwjglApplication(new MainLogic(), config);
	}
}
