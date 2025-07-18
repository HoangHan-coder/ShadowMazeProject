package com.ShadowMaze.lwjgl3;

import com.ShadowMaze.core.Main;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.ShadowMaze.screen.GameScreen;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class Lwjgl3Launcher {

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) {
            return; // This handles macOS support and helps on Windows.
        }

        new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("ShadowMaze");
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);
        // Maze size (??ng b? v?i MazeGenerator trong GameScreen)
        final int TILE_SIZE = 42;
        final int MAZE_COLS = 21;
        final int MAZE_ROWS = 21;

        int screenWidth = MAZE_COLS * TILE_SIZE;
        int screenHeight = MAZE_ROWS * TILE_SIZE;

        configuration.setWindowedMode(screenWidth, screenHeight);

        configuration.setWindowedMode(GameScreen.SCREEN_WIDTH, GameScreen.SCREEN_HEIGHT);

        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}