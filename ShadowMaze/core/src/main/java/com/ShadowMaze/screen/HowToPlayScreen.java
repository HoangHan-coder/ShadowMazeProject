package com.ShadowMaze.screen;

import com.ShadowMaze.core.Main;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class HowToPlayScreen implements Screen {

    private final Game game; // class Main k? th?a Game
    private Texture background;
    private SpriteBatch batch;
    private Texture background1;
    private Texture background2;

    public HowToPlayScreen(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("menu/howtoplay/background.png"); // ?nh background m?i
        background1 = new Texture("menu/howtoplay/type1.png");
        background2 = new Texture("menu/howtoplay/type2.png");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        float scale = 0.6f;
        float newWidth = background1.getWidth() * scale;
        float newHeight = background1.getHeight() * scale;
        float x = (Gdx.graphics.getWidth() - newWidth) / 2f;
        float y = (Gdx.graphics.getHeight() - newHeight) / 2f;
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(background1, 20, y, newWidth, newHeight); // canh gi?a background1        
        batch.draw(background2, 600, y, newWidth, newHeight); // canh gi?a background1

        // V? thêm h??ng d?n t?i ?ây n?u c?n
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game)); // Quay l?i menu
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        background1.dispose();
        background2.dispose();
    }
}
