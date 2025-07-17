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

/**
 * Handles the Victory screen display and interactions when the player wins the game.
 */
public class GameVictoryHandler {

    private Texture victoryImage;          // Victory banner image
    private Texture scorePanelImage;       // Scoreboard panel image
    private BitmapFont font;               // Font for text display
    private float victoryTime;             // Tracks elapsed time since victory screen triggered
    private boolean active;                // Indicates if the victory screen is active

    private final ImageButton btnReplay;   // Replay button
    private final ImageButton btnQuit;     // Quit button

    private final ScoreBoard scoreBoard;   // Scoreboard reference
    private final Game game;               // Main game instance
    private final GameScreen gs;           // Reference to GameScreen for UI and data

    public GameVictoryHandler(GameScreen gs) {
        // Load images
        victoryImage = new Texture(Gdx.files.internal("menu/function/you_win.png"));
        scorePanelImage = new Texture(Gdx.files.internal("menu/function/score.png"));

        // Initialize font
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        // Reference main components
        scoreBoard = gs.scoreBoard;
        game = (Game) Gdx.app.getApplicationListener();
        this.gs = gs;

        // Buttons
        Texture btnReplayTex = new Texture(Gdx.files.internal("menu/function/new.png"));
        Texture btnQuitTex = new Texture(Gdx.files.internal("menu/function/quitnew.png"));

        // Replay button style
        ImageButton.ImageButtonStyle styleReplay = new ImageButton.ImageButtonStyle();
        styleReplay.imageUp = new TextureRegionDrawable(new TextureRegion(btnReplayTex));

        // Quit button style
        ImageButton.ImageButtonStyle styleQuit = new ImageButton.ImageButtonStyle();
        styleQuit.imageUp = new TextureRegionDrawable(new TextureRegion(btnQuitTex));

        // Create buttons
        btnReplay = new ImageButton(styleReplay);
        btnReplay.setSize(150, 60);
        btnReplay.setPosition(440, 130);

        btnQuit = new ImageButton(styleQuit);
        btnQuit.setSize(150, 60);
        btnQuit.setPosition(620, 130);

        // Button events
        btnReplay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                reset();
                Screen current = game.getScreen();
                Screen next = new GameScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });

        btnQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Screen current = game.getScreen();
                Screen next = new MainMenuScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });
    }

    /** Activates the victory screen */
    public void trigger() {
        this.active = true;
        this.victoryTime = 0;
    }

    /** Resets the victory state and scoreboard */
    public void reset() {
        this.active = false;
        this.victoryTime = 0;
        scoreBoard.reset();
    }

    /** Returns whether the victory screen is active
     * @return  */
    public boolean isActive() {
        return active;
    }

    /** Updates internal timer when active
     * @param delta */
    public void update(float delta) {
        if (active) {
            victoryTime += delta;
        }
    }

    /**
     * Renders the victory screen components
     * @param batch SpriteBatch for drawing
     * @param screenWidth Width of the screen
     * @param screenHeight Height of the screen
     * @param delta Frame delta time
     */
    public void render(SpriteBatch batch, int screenWidth, int screenHeight, float delta) {
        float imageWidth = 300, imageHeight = 300;
        float scoreWidth = 400, scoreHeight = 400;
        float scoreX = (screenWidth - scoreWidth) / 2f;
        float scoreY = (screenHeight - scoreHeight) / 2f - 100;

        // Draw panel and victory image
        batch.draw(scorePanelImage, 405, 105, scoreWidth, scoreHeight);
        batch.draw(victoryImage, 440, 230, imageWidth, imageHeight);

        // Render score board
        scoreBoard.render(batch, (int) scoreX + 50, (int) scoreY + 90, delta);

        // Display time in format mm:ss
        String timeText = String.format("Time: %02d:%02d", gs.ui.min, gs.ui.second);
        font.draw(batch, timeText, 530, 240);
    }

    /** Adds buttons to the stage for input handling
     * @param stage */
    public void addToStage(Stage stage) {
        stage.addActor(btnReplay);
        stage.addActor(btnQuit);
    }

    /** Disposes resources */
    public void dispose() {
        victoryImage.dispose();
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
