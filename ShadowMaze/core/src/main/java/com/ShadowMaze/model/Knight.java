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

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, IDLE
    }
    private Direction currentDirection = Direction.IDLE;

    public Knight(GameScreen gs) {
        this.gs = gs;

        setDefaultValue();
        
    }
    
    private void setDefaultValue() {
        
        stateTime = 0f;
                
        positionX = 36 * GameScreen.TILE_SIZE;
        positionY = 28 * GameScreen.TILE_SIZE;
        
        renderX = GameScreen.SCREEN_WIDTH / 2 - (GameScreen.TILE_SIZE / 2);
        renderY = GameScreen.SCREEN_HEIGHT / 2 - (GameScreen.TILE_SIZE / 2);
        
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
    if (gs.map.isPaused()) {
        // N?u ?ang t?m d?ng thì không update animation
        TextureRegion frame = moveDown.getKeyFrame(0); // frame ??ng yên
        gs.batch.draw(frame, renderX, renderY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        return;
    }

    update(delta); // <- ch? ch?y khi KHÔNG pause

    Animation<TextureRegion> currentAnim = switch (currentDirection) {
        case UP -> moveUp;
        case DOWN -> moveDown;
        case LEFT -> moveLeft;
        case RIGHT -> moveRight;
        default -> moveDown;
    };

    TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);
    gs.batch.draw(frame, renderX, renderY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
}


    public void inputHandle() {
        int speed = 8; // di chuyá»ƒn 1 Ã´ má»—i láº§n nháº¥n
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
        
        
        
        // check tile collision
        collisionOn = false;
        gs.cCheck.checkTile(this);
        
        // if collision is false, knight can move
        if (collisionOn == false) {
            switch (currentDirection) {
                case UP -> {
                    positionY -= speed;
                }
                case DOWN -> {
                    positionY += speed;
                }
                case LEFT -> {
                    positionX -= speed;
                }
                case RIGHT -> {
                    positionX += speed;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                speed = 8;
            } else {
                speed = 4;
            }
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

