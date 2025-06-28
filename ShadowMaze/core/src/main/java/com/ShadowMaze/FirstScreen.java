/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
   private ShapeRenderer shapeRenderer;
    private MazeRenderer mazeRenderer;

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        MazeGenerator generator = new MazeGenerator(32, 32); // Kích thước lẻ để có đường đi
        int[][] maze = generator.generate();
        mazeRenderer = new MazeRenderer(maze, 20); // Mỗi ô 20px
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mazeRenderer.render();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        shapeRenderer.dispose();
    }
}
