package com.ShadowMaze.core;


import com.ShadowMaze.screen.MainMenuScreen;
import com.badlogic.gdx.Game;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {

    @Override
    public void create() {
        setScreen(new MainMenuScreen(this)); // start in GameScreen
    }

}
