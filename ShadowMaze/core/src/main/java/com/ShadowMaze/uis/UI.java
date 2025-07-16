package com.ShadowMaze.uis;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 *
 * @author NgKaitou
 */
public class UI {
    
    GameScreen gs;
    private BitmapFont text;
    Texture key = new Texture("Object/key.png");

    public UI(GameScreen gs) {
        this.gs = gs;
        text = new BitmapFont();
        text.setColor(Color.WHITE);
        text.getData().setScale(2.2f);
    }
    
    public void render() {        
        gs.batch.draw(key, 22*GameScreen.TILE_SIZE, 12*GameScreen.TILE_SIZE, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        text.draw(gs.batch, " x " + gs.knight.hasKey, 23*GameScreen.TILE_SIZE, 13*GameScreen.TILE_SIZE - 8 );       
    }
        
}
