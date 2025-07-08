package com.ShadowMaze.core;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.Game;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen()); // Bắt đầu ở GameScreen
    }

}
