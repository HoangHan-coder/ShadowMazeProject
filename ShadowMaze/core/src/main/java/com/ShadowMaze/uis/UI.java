package com.ShadowMaze.uis;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 *
 * @author NgKaitou
 */
public class UI {
    
    GameScreen gs;
    private BitmapFont text = new BitmapFont();
    private BitmapFont timePlay = new BitmapFont();
    float timeElapsed;
    public int second, min;
    
    Texture key = new Texture("Object/key.png");
    Texture scrollFire = new Texture("items/ScrollFire.png");
    Texture scrollIce = new Texture("items/ScrollIce.png");
    Texture scrollThunder = new Texture("items/ScrollThunder.png");
    Texture scrollEmpty = new Texture("items/ScrollEmpty.png");
    public UI(GameScreen gs) {
        this.gs = gs;
        timeElapsed = 0f;
        second = 0;
        min = 0;
        text.setColor(Color.WHITE);
        text.getData().setScale(2.2f);
        timePlay.setColor(Color.WHITE);
        timePlay.getData().setScale(2.2f);
    }
    
    public void render(float delta) { 
        timeElapsed += delta;
        if (timeElapsed >= 1f) {
            second++;
            timeElapsed = 0f;
            if (second >=60) {
                min++;
                second = 0;
            }
        }
//        System.out.printf("%02d:%02d\n",min,second);
        
        timePlay.draw(gs.batch, String.format("%02d:%02d\n",min,second), 12*GameScreen.TILE_SIZE, 13*GameScreen.TILE_SIZE);
        gs.batch.draw(key, 22*GameScreen.TILE_SIZE, 12*GameScreen.TILE_SIZE, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        text.draw(gs.batch, " x " + gs.knight.hasKey, 23*GameScreen.TILE_SIZE, 13*GameScreen.TILE_SIZE - 8 );    
        
        if (gs.knight.hasScrollFire) {
            gs.batch.draw(scrollFire, 21*GameScreen.TILE_SIZE - (GameScreen.TILE_SIZE), 1*GameScreen.TILE_SIZE, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        } else {
            gs.batch.draw(scrollEmpty, 21*GameScreen.TILE_SIZE - (GameScreen.TILE_SIZE), 1*GameScreen.TILE_SIZE, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        }
        
        if (gs.knight.hasScrollIce) {
            gs.batch.draw(scrollIce, 22*GameScreen.TILE_SIZE - (GameScreen.TILE_SIZE/2), 1*GameScreen.TILE_SIZE, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        } else {
            gs.batch.draw(scrollEmpty, 22*GameScreen.TILE_SIZE - (GameScreen.TILE_SIZE/2), 1*GameScreen.TILE_SIZE, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        }
        
        if (gs.knight.hasScrollThunder) {
            gs.batch.draw(scrollThunder, 23*GameScreen.TILE_SIZE, 1*GameScreen.TILE_SIZE, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        } else {
            gs.batch.draw(scrollEmpty, 23*GameScreen.TILE_SIZE, 1*GameScreen.TILE_SIZE, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        }
        
       
    }
        
}
