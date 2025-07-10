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
 *
 * @author letan
 */
public class FadeTransitionScreen implements Screen {
    private final Game game;
    private final Screen fromScreen;
    private final Screen toScreen;
    private float alpha = 0f;
    private boolean transitioning = true;

    public FadeTransitionScreen(Game game, Screen from, Screen to) {
        this.game = game;
        this.fromScreen = from;
        this.toScreen = to;
        fromScreen.show();
        toScreen.show();
    }

    @Override
    public void render(float delta) {
        if (transitioning) {
            alpha += delta; 
            if (alpha >= 1f) {
                transitioning = false;
                game.setScreen(toScreen);
            } else {
                fromScreen.render(delta);
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(0, 0, 0, alpha);
                shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                shapeRenderer.end();
                shapeRenderer.dispose();
            }
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}

