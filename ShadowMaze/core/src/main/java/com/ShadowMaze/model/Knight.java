/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author NgKaitou
 */
public class Knight extends Entity {
    int renderX;
    int renderY;
    GameScreen gs;
    float stateTime;
    Animation<TextureRegion> moveUp, moveDown, moveLeft, moveRight;

    public Knight(GameScreen gs) {
        this.gs = gs;
        solidArea = new Rectangle();
        setDefaultValue();
        
    }
    
    private void setDefaultValue() {
        
        stateTime = 0f;
                
        positionX = 36 * GameScreen.TILE_SIZE;
        positionY = 28 * GameScreen.TILE_SIZE;
        
        renderX = GameScreen.SCREEN_WIDTH / 2 - (GameScreen.TILE_SIZE / 2);
        renderY = GameScreen.SCREEN_HEIGHT / 2 - (GameScreen.TILE_SIZE / 2);
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        
        moveUp = loadUpAnimation();
        moveDown = loadDownAnimation();
        moveLeft = loadLeftAnimation();
        moveRight = loadRightAnimation();
    }

    public void setDirection(Direction direction) {
        if (direction != currentDirection) {
            stateTime = 0f;
        }
        currentDirection = direction;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void knightRender(float delta) {
        update(delta);
        Animation<TextureRegion> currentAnim = switch (currentDirection) {
        case UP -> moveUp;
        case DOWN -> moveDown;
        case LEFT -> moveLeft;
        case RIGHT -> moveRight;
        default -> moveDown; // fallback frame
        };
        
        TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);
        gs.batch.draw(frame, renderX, renderY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
    }

    public void inputHandle() {
        int speed = 8; // di chuyển 1 ô mỗi lần nhấn

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            setDirection(Direction.UP);
            positionY += speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            setDirection(Direction.DOWN);
            positionY -= speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            setDirection(Direction.LEFT);
            positionX -= speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            setDirection(Direction.RIGHT);
            positionX += speed;
        } else {
            setDirection(Direction.IDLE);
        }
    }

    private Animation<TextureRegion> loadUpAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_up_" + (i+1) + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    private Animation<TextureRegion> loadDownAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_down_" + (i+1) + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    private Animation<TextureRegion> loadLeftAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_left_" + (i+1) + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    private Animation<TextureRegion> loadRightAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_right_" + (i+1) + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    public void dispose() {
        // Dispose the default texture
        image.dispose();

        // Dispose all textures used in the "move up" animation
        for (TextureRegion region : moveUp.getKeyFrames()) {
            region.getTexture().dispose();
        }

        // Dispose all textures used in the "move down" animation
        for (TextureRegion region : moveDown.getKeyFrames()) {
            region.getTexture().dispose();
        }

        // Dispose all textures used in the "move left" animation
        for (TextureRegion region : moveLeft.getKeyFrames()) {
            region.getTexture().dispose();
        }

        // Dispose all textures used in the "move right" animation
        for (TextureRegion region : moveRight.getKeyFrames()) {
            region.getTexture().dispose();
        }
    }

}

