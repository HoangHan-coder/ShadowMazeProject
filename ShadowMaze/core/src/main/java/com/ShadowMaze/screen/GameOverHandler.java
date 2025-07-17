package com.ShadowMaze.screen;

import com.ShadowMaze.model.FadeTransitionScreen;
import com.ShadowMaze.model.ScoreBoard;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameOverHandler {

    private Texture gameOverImage;
    private Texture scorePanelImage;
    private BitmapFont font;
    private BitmapFont totalCore;
    private float gameOverTime;
    private boolean active;

    private final ImageButton btnReplay;
    private final ImageButton btnQuit;

    private final ScoreBoard scoreBoard;
    private final Game game;
    private GameScreen gs;
    public GameOverHandler(GameScreen gs, ScoreBoard scoreBoard) {
        gameOverImage = new Texture(Gdx.files.internal("menu/function/over.png"));
        scorePanelImage = new Texture(Gdx.files.internal("menu/function/score.png"));
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        scoreBoard = gs.scoreBoard;
        this.scoreBoard = scoreBoard;
        game = (Game) Gdx.app.getApplicationListener();

        // Buttons
        Texture btnReplayTex = new Texture(Gdx.files.internal("menu/function/replaynew.png"));
        Texture btnQuitTex = new Texture(Gdx.files.internal("menu/function/quitnew.png"));

        ImageButton.ImageButtonStyle styleReplay = new ImageButton.ImageButtonStyle();
        styleReplay.imageUp = new TextureRegionDrawable(new TextureRegion(btnReplayTex));

        ImageButton.ImageButtonStyle styleQuit = new ImageButton.ImageButtonStyle();
        styleQuit.imageUp = new TextureRegionDrawable(new TextureRegion(btnQuitTex));

        btnReplay = new ImageButton(styleReplay);
        btnReplay.setSize(150, 60);
        btnReplay.setPosition(440, 130);

        btnQuit = new ImageButton(styleQuit);
        btnQuit.setSize(150, 60);
        btnQuit.setPosition(620, 130);

        // Events
        btnReplay.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                reset();
                Screen current = game.getScreen();
                Screen next = new GameScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });

        btnQuit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Screen current = game.getScreen();
                Screen next = new MainMenuScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });
        this.gs = gs;
    }

    public void trigger() {
        this.active = true;
        this.gameOverTime = 0;
    }

    public void reset() {
        this.active = false;
        this.gameOverTime = 0;
        scoreBoard.reset();
    }

    public boolean isActive() {
        return active;
    }

    public void update(float delta) {
        if (active) {
            gameOverTime += delta;
        }
    }

    public void render(SpriteBatch batch, int screenWidth, int screenHeight, float delta) {
        float imageWidth = 300, imageHeight = 300;
        float scoreWidth = 400, scoreHeight = 400;
        float scoreX = (screenWidth - scoreWidth) / 2f;
        float scoreY = (screenHeight - scoreHeight) / 2f - 100;

        batch.draw(scorePanelImage, 405, 105, scoreWidth, scoreHeight);
        batch.draw(gameOverImage, 460, 300, imageWidth, imageHeight);
        String scoreText = String.format("Score: %01d", scoreBoard.scoreActual);
        font.draw(batch, scoreText, 550, 290);

        // Draw time (e.g. 00:10 format)
        int seconds = (int) gameOverTime;
        String timeText = String.format("Time: %02d:%02d", gs.ui.min,gs.ui.second);
        font.draw(batch, timeText, 530, 240);
    }

    public void addToStage(Stage stage) {
        stage.addActor(btnReplay);
        stage.addActor(btnQuit);
    }

    public void dispose() {
        gameOverImage.dispose();
        scorePanelImage.dispose();
        font.dispose();
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public ImageButton getReplayButton() {
        return btnReplay;
    }

    public ImageButton getQuitButton() {
        return btnQuit;
    }

}
