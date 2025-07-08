package com.ShadowMaze.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.ShadowMaze.core.Main;
import com.ShadowMaze.screen.GameScreen;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class Lwjgl3Launcher {

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) {
            return; // This handles macOS support and helps on Windows.
        }
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
    Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
    configuration.setTitle("ShadowMaze");
    configuration.useVsync(true);
    configuration.setForegroundFPS(60);


    configuration.setWindowedMode(GameScreen.SCREEN_WIDTH, GameScreen.SCREEN_HEIGHT);

    configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
    return configuration;
}

}
