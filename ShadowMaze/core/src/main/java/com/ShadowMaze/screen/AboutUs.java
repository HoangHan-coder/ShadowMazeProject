package com.ShadowMaze.screen;

import com.ShadowMaze.core.Main;
import com.ShadowMaze.model.FadeTransitionScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class AboutUs implements Screen {

    private final Game game; // class Main k? th?a Game
    private Texture background;
    private SpriteBatch batch;
    private Texture background1;
    private Texture background3;
    private Stage stage;
    private ImageButton backButton;

    public AboutUs(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("menu/aboutus/background.png"); // ?nh background m?i
        background1 = new Texture("menu/aboutus/aboutus.png");
        background3 = new Texture("menu/howtoplay/back.png");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // G�n stage ?? nh?n input

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(background3));
        backButton = new ImageButton(drawable);

        backButton.setSize(150, 50);
        backButton.setPosition(20, 30); // V? tr� gi?ng nh? c? b?n d�ng v?i batch

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Back button clicked");
                Screen current = game.getScreen();
                Screen next = new MainMenuScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });

        stage.addActor(backButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        batch.draw(background1, 300, y, newWidth, newHeight); // canh gi?a background1      
        // V? th�m h??ng d?n t?i ?�y n?u c?n
        batch.end();
        stage.act(delta);
        stage.draw();
        System.out.println("InputProcessor: " + Gdx.input.getInputProcessor());

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
        stage.dispose();
    }
}
