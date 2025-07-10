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
import com.ShadowMaze.uis.HpBar;
import com.ShadowMaze.uis.StaminaBar;
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
    
    public boolean isRunning = false;  // C? ki?m tra ?ang ch?y
    public int baseSpeed = 1;          // T?c ?? ?i b?

    private StaminaBar staminaBar; // Th�m thanh stamina+
    public int runSpeed = 8;   // t?c ?? khi ch?y (Shift)
    private float staminaDrainRate = 30f;   // gi?m m?i gi�y
    private float staminaRegenRate = 15f;   // h?i m?i gi�y
    private HpBar hpBar; // Th�m d�ng n�y v�o class Knight
    GameScreen gs;
    float stateTime;
    Animation<TextureRegion> moveUp, moveDown, moveLeft, moveRight;



    public Knight(GameScreen gs, StaminaBar staminaBar, HpBar hpBar) {
        this.gs = gs;
        this.staminaBar = staminaBar;
        this.speed = baseSpeed;
        this.hpBar = hpBar; // G�n HpBar
        this.hpBar = hpBar; // G�n HpBar
        setDefaultValue();
    }

    private void setDefaultValue() {
        speed = 4; // di chuyển 1 ô mỗi lần nhấn
        stateTime = 0f;
        
        renderX = GameScreen.SCREEN_WIDTH / 2 - (GameScreen.TILE_SIZE / 2);
        renderY = GameScreen.SCREEN_HEIGHT / 2 - (GameScreen.TILE_SIZE / 2);
        
         // set hitbox knight
        offsetX = 8;
        offsetY = 4;
        solidArea = new Rectangle();
        solidArea.x = offsetX;
        solidArea.y = offsetY;
        solidArea.width = 32;
        solidArea.height = 32;
        
        positionX = 60 * GameScreen.TILE_SIZE;
        positionY = 39 * GameScreen.TILE_SIZE;
        
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

    private void dicrectionHandle() {

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
        
    }   
        
    public void inputHandle(float delta) {
        dicrectionHandle();
        float currentStamina = staminaBar.getCurrentStamina();
        float currentHp = hpBar.getCurrentHp();

        // Gi?m HP khi gi? Shift
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            if (hpBar.getCurrentHp() > 0) {
                hpBar.setCurrentHp(hpBar.getCurrentHp() - 30 * delta);
            }
        } else {
            // H?i HP khi kh�ng gi? Shift
            if (hpBar.getCurrentHp() < hpBar.getMaxHp()) {
                hpBar.setCurrentHp(hpBar.getCurrentHp() + 10 * delta);
            }
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && currentStamina > 10f && currentHp > 1f) {
            isRunning = true;
            speed = runSpeed;

            float newStamina = currentStamina - staminaDrainRate * delta;
            if (newStamina < 0) {
                newStamina = 0;
            }
            staminaBar.setCurrentStamina(newStamina);

            // Tr? m�u ? ?�y n?u mu?n
            hpBar.setCurrentHp(currentHp - 30 * delta);
        } else {
            isRunning = false;
            speed = baseSpeed;

            // H?i l?i m�u v� stamina
            if (currentHp < hpBar.getMaxHp()) {
                hpBar.setCurrentHp(currentHp + 10 * delta);
            }
            staminaBar.regenerate(delta);
        }
        // Sau khi ?� di chuy?n
        int tileX = (positionX + GameScreen.TILE_SIZE / 2) / GameScreen.TILE_SIZE;
        int tileY = (positionY + GameScreen.TILE_SIZE / 2) / GameScreen.TILE_SIZE;
        // Gi? s? tile c� ID = 3 l� c?ng chuy?n m�n
        if (gs.map.tileNum[tileY][tileX] == 0) {
            // Chuy?n sang map m?i
            gs.map.changeMap("maps/map_03.txt");

            // ??t l?i v? tr� ng??i ch?i
            positionX = 10 * GameScreen.TILE_SIZE;
            positionY = 10 * GameScreen.TILE_SIZE;
        }
       

        // check tile collision
        collisionOn = false;
//        gs.cCheck.checkTile(this);
//        if (gs.map.tileNum[tileY][tileX] == 0) {
//            // Chuy?n sang map m?i
////            gs.map.changeMap("maps/map_03.txt");
//
//            // ??t l?i v? tr� ng??i ch?i
//            positionX = 10 * GameScreen.TILE_SIZE;
//            positionY = 10 * GameScreen.TILE_SIZE;
//        }
//       

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

        }
    }

    private Animation<TextureRegion> loadUpAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_up_" + (i+1) + ".png"));
            frames[i] = new TextureRegion(new Texture("knight/knight_up_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    private Animation<TextureRegion> loadDownAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_down_" + (i+1) + ".png"));
            frames[i] = new TextureRegion(new Texture("knight/knight_down_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    private Animation<TextureRegion> loadLeftAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_left_" + (i+1) + ".png"));
            frames[i] = new TextureRegion(new Texture("knight/knight_left_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    private Animation<TextureRegion> loadRightAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_right_" + (i+1) + ".png"));
            frames[i] = new TextureRegion(new Texture("knight/knight_right_" + (i + 1) + ".png"));
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

