/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import static com.ShadowMaze.model.Entity.Direction.DOWN;
import static com.ShadowMaze.model.Entity.Direction.LEFT;
import static com.ShadowMaze.model.Entity.Direction.RIGHT;
import static com.ShadowMaze.model.Entity.Direction.UP;
import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author NgKaitou
 */
public class Knight extends Entity {
    int renderX;
    int renderY;
    
    int offsetX;
    int offsetY;
    
    GameScreen gs;
    float stateTime;
    Animation<TextureRegion> moveUp, moveDown, moveLeft, moveRight;

    public Knight(GameScreen gs) {
        this.gs = gs;

        setDefaultValue();
        
    }
    
    private void setDefaultValue() {
        speed = 4; // di chuyển 1 ô mỗi lần nhấn
        stateTime = 0f;
        offsetX = 8;
        offsetY = 4;
        solidArea = new Rectangle();
        solidArea.x = offsetX;
        solidArea.y = offsetY;
        solidArea.width = 32;
        solidArea.height = 32;
        
        positionX = 36 * GameScreen.TILE_SIZE;
        positionY = 24 * GameScreen.TILE_SIZE;
        
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
        update(delta);
        Animation<TextureRegion> currentAnim = switch (currentDirection) {
        case UP -> moveDown;
        case DOWN -> moveUp;
        case LEFT -> moveLeft;
        case RIGHT -> moveRight;
        default -> moveDown; // fallback frame
        };
        
        TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);
        gs.batch.draw(frame, renderX, renderY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        
         //debug hit box
//        ShapeRenderer shapeRenderer = new ShapeRenderer();
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.rect(
//                renderX + solidArea.x,
//                renderY + solidArea.y,
//                solidArea.width,
//                solidArea.height
//        );
//       
//        shapeRenderer.end();
    }

    public void inputHandle() { 

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            setDirection(Direction.UP);           
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            setDirection(Direction.DOWN);            
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            setDirection(Direction.LEFT);            
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            setDirection(Direction.RIGHT);            
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
        System.out.println("move: " + currentDirection + ", collision: " + collisionOn);
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

