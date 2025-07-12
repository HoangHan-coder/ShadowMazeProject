/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.ShadowMaze.skill.Sword;
import static com.ShadowMaze.model.Entity.Direction.DOWN;
import static com.ShadowMaze.model.Entity.Direction.LEFT;
import static com.ShadowMaze.model.Entity.Direction.RIGHT;
import static com.ShadowMaze.model.Entity.Direction.UP;
import com.ShadowMaze.screen.GameScreen;
import com.ShadowMaze.uis.HpBar;
import com.ShadowMaze.uis.StaminaBar;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author NgKaitou
 */
public class Knight extends Entity {

    public int renderX;
    public int renderY;
    boolean hasKey;
    private Array<Sword> skills = new Array<>();

    int offsetX;
    int offsetY;

    public boolean isRunning = false;  // C? ki?m tra ?ang ch?y
    public int baseSpeed = 4;          // T?c ?? ?i b?
    private boolean hasFired = false; // ?ã b?n k? n?ng ch?a?

    private StaminaBar staminaBar; // Thï¿½m thanh stamina+
    public int runSpeed = 8;   // t?c ?? khi ch?y (Shift)
    private float staminaDrainRate;   // gi?m m?i giï¿½y
    private float staminaRegenRate;   // h?i m?i giï¿½y
    private HpBar hpBar; // Thï¿½m dï¿½ng nï¿½y vï¿½o class Knight
    GameScreen gs;
    float stateTime;
    private Direction dir;
    private Direction lastMoveDirection = Direction.RIGHT;
    Animation<TextureRegion> moveUp, moveDown, moveLeft, moveRight;
    private Entity.Direction direction; // ? bi?n này ?ã t?n t?i

    public Knight(GameScreen gs, StaminaBar staminaBar, HpBar hpBar) {
        this.gs = gs;
        this.staminaBar = staminaBar;
        this.speed = baseSpeed;
        this.hpBar = hpBar; // Gï¿½n HpBar
        this.hpBar = hpBar; // Gï¿½n HpBars
        setDefaultValue();
        dir = currentDirection;

    }

    private void setDefaultValue() {
        speed = 4; // di chuyá»ƒn 1 Ã´ má»—i láº§n nháº¥n
        stateTime = 0f;
        hasKey = false;

        renderX = GameScreen.SCREEN_WIDTH / 2 - (GameScreen.TILE_SIZE / 2);
        renderY = GameScreen.SCREEN_HEIGHT / 2 - (GameScreen.TILE_SIZE / 2);

        // set hitbox knight
        offsetX = 8;
        offsetY = 4;
        solidArea = new Rectangle();
        solidArea.x = 4;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
        // set position default of knight default
//        positionX = 60 * GameScreen.TILE_SIZE;
//        positionY = 39 * GameScreen.TILE_SIZE;
        positionX = 87 * GameScreen.TILE_SIZE;
        positionY = 16 * GameScreen.TILE_SIZE;

        //set stamina bar
        staminaDrainRate = 30f;
        staminaRegenRate = 15f;

        // set direction of knight 
        positionX = 36 * GameScreen.TILE_SIZE;
        positionY = 28 * GameScreen.TILE_SIZE;
        moveUp = loadUpAnimation();
        moveDown = loadDownAnimation();
        moveLeft = loadLeftAnimation();
        moveRight = loadRightAnimation();
    }

    public void setDirection(Direction direction) {
        if (direction != currentDirection && direction != Direction.IDLE) {
            hasFired = false; // ??i h??ng ? cho phép b?n l?i
        }
        if (direction != Direction.IDLE) {
            lastMoveDirection = direction;
        }
        currentDirection = direction;
    }

    public void update(float delta) {
        stateTime += delta;

        for (int i = 0; i < skills.size; i++) {
            Sword skill = skills.get(i);

            // N?u h??ng hi?n t?i khác v?i h??ng k? n?ng ?ang b?n -> t?t
//            if (skill.isActive() && currentDirection != Direction.IDLE && skill.getDirection() != currentDirection) {
//                skill.setActive(false);  // ?ánh d?u không ho?t ??ng
//            }
            skill.update(delta);

            if (!skill.isActive()) {
                skills.removeIndex(i);
                i--;
            }
        }
    }

    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    public void knightRender(float delta) {
        update(delta);
        for (Sword skill : skills) {
            skill.render(gs.batch, positionX, positionY, renderX, renderY);
        }
        Animation<TextureRegion> currentAnim = switch (currentDirection) {
            case UP ->
                moveDown;
            case DOWN ->
                moveUp;
            case LEFT ->
                moveLeft;
            case RIGHT ->
                moveRight;
            default ->
                moveDown; // fallback frame
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

    public void inputHandle(float delta) {
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
//        dicrectionHandle();
        float currentStamina = staminaBar.getCurrentStamina();
        float currentHp = hpBar.getCurrentHp();

        // Gi?m HP khi gi? Shift
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            if (hpBar.getCurrentHp() > 0) {
                hpBar.setCurrentHp(hpBar.getCurrentHp() - 30 * delta);
            }
        } else {
            // H?i HP khi khï¿½ng gi? Shift
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

            // Tr? mï¿½u ? ?ï¿½y n?u mu?n
            hpBar.setCurrentHp(currentHp - 30 * delta);
        } else {
            isRunning = false;
            speed = baseSpeed;

            // H?i l?i mï¿½u vï¿½ stamina
            if (currentHp < hpBar.getMaxHp()) {
                hpBar.setCurrentHp(currentHp + 10 * delta);
            }
            staminaBar.regenerate(delta);
        }
        // Sau khi ?ï¿½ di chuy?n
        int tileX = (positionX + GameScreen.TILE_SIZE / 2) / GameScreen.TILE_SIZE;
        int tileY = (positionY + GameScreen.TILE_SIZE / 2) / GameScreen.TILE_SIZE;
        // Gi? s? tile cï¿½ ID = 3 lï¿½ c?ng chuy?n mï¿½n
        if (gs.map.tileNum[tileY][tileX] == 0) {
            // Chuy?n sang map m?i
//            gs.map.changeMap("maps/map_03.txt");

            // ??t l?i v? trï¿½ ng??i ch?i
//            positionX = 10 * GameScreen.TILE_SIZE;
//            positionY = 10 * GameScreen.TILE_SIZE;
        }

//        if (gs.map.tileNum[tileY][tileX] == 0) {
//            // Chuy?n sang map m?i
        ////            gs.map.changeMap("maps/map_03.txt");
//
//            // ??t l?i v? trï¿½ ng??i ch?i
//            positionX = 10 * GameScreen.TILE_SIZE;
//            positionY = 10 * GameScreen.TILE_SIZE;
//        }
//       
        // check tile collision
        collisionOn = false;
        gs.cCheck.checkTile(this);
        // check object collision
        int indexObject = gs.cCheck.checkObject(this, true);
        pickUpObject(indexObject);
//        System.out.println("collisionOn: " + collisionOn);
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !hasFired) {
            Entity.Direction skillDir = (currentDirection == Direction.IDLE)
                    ? lastMoveDirection
                    : currentDirection;

            Sword skill = new Sword(skillDir);
            skill.activate(this, skillDir);
            hasFired = true; // ?ánh d?u ?ã b?n
            skills.add(skill);
        }

// Reset hasFired n?u không còn nh?n SPACE
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            hasFired = false;
        }
    }

    public void pickUpObject(int indexOfObject) {
        if (indexOfObject != -1) {
            String objectName = gs.obj[indexOfObject].name;
            switch (objectName) {
                case "Key" -> {
                    hasKey = true;
                    gs.obj[indexOfObject] = null;
                }
                case "Gate" -> {
                    if (hasKey == true) {
                        collisionOn = false;
                        gs.obj[indexOfObject].image = new Texture("Object/gate_open.png");

                    }
                }
                case "Cave" -> {
                    if (hasKey == true) {
                        System.out.println("You win!");
                    }
                }
            }
        }
    }

    private Animation<TextureRegion> loadUpAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_up_" + (i + 1) + ".png"));
            frames[i] = new TextureRegion(new Texture("knight/knight_up_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    private Animation<TextureRegion> loadDownAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_down_" + (i + 1) + ".png"));
            frames[i] = new TextureRegion(new Texture("knight/knight_down_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    private Animation<TextureRegion> loadLeftAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_left_" + (i + 1) + ".png"));
            frames[i] = new TextureRegion(new Texture("knight/knight_left_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    private Animation<TextureRegion> loadRightAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_right_" + (i + 1) + ".png"));
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

    public Direction getDirection() {
        return this.currentDirection != null ? this.currentDirection : Direction.IDLE;
    }

    public Direction getLastMoveDirection() {
        return lastMoveDirection;
    }
}
