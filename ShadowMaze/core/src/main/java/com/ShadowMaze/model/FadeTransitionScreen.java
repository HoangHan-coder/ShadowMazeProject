/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * This class handles a fade transition effect between two LibGDX screens.
 * It overlays a black transparent rectangle that increases in opacity over time,
 * and switches to the destination screen when the fade is complete.
 */
public class FadeTransitionScreen implements Screen {

    private final Game game;              // The game instance managing screens
    private final Screen fromScreen;      // The current screen to fade from
    private final Screen toScreen;        // The target screen to fade to
    private float alpha = 0f;             // Alpha level for the fade (0.0 = fully transparent, 1.0 = fully black)
    private boolean transitioning = true; // Indicates if transition is still ongoing

    /**
     * Constructor for FadeTransitionScreen.
     * @param game The LibGDX Game object.
     * @param from The screen to transition from.
     * @param to The screen to transition to.
     */
    public FadeTransitionScreen(Game game, Screen from, Screen to) {
        this.game = game;
        this.fromScreen = from;
        this.toScreen = to;

        // Ensure both screens are prepared
        fromScreen.show();
        toScreen.show();
    }

    /**
     * Renders the transition effect every frame.
     * It renders the fromScreen and overlays a black rectangle
     * whose opacity increases gradually until fully opaque.
     * Once opacity reaches 1, it switches to the toScreen.
     */
    @Override
    public void render(float delta) {
        if (transitioning) {
            alpha += delta; // Increase alpha based on frame time

            // Once alpha reaches 1.0, transition is complete
            if (alpha >= 1f) {
                transitioning = false;
                game.setScreen(toScreen); // Switch to the new screen
            } else {
                // Render the fromScreen normally
                fromScreen.render(delta);

                // Enable alpha blending
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

                // Draw the black transparent rectangle
                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(0, 0, 0, alpha); // Black with increasing alpha
                shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                shapeRenderer.end();

                // Dispose renderer after use
                shapeRenderer.dispose();
            }
        }
    }

    // These methods are required by the Screen interface but unused here

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
