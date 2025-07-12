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
 * The Knight class represents the main player character in the game. It handles
 * movement, collision detection, animation, stamina and HP logic, as well as
 * interactions with objects and map transitions.
 */
public class Knight extends Entity {

    // Position to render the character on screen (fixed to center)
    public int renderX;
    public int renderY;

    // Key possession state
    boolean hasKey;
    private Array<Sword> skills = new Array<>();
    public boolean isRunning = false;  // C? ki?m tra ?ang ch?y
    public int baseSpeed = 5;          // T?c ?? ?i b?
    private boolean hasFired = false; // ?ã b?n k? n?ng ch?a?
    // Movement and stamina management
    public final int runSpeed = 8;
    private float staminaDrainRate;
    private float staminaRegenRate;

    // UI elements
    private StaminaBar staminaBar;
    private HpBar hpBar;

    // Game context
    GameScreen gs;

    // Animation timing
    float stateTime;
    private Direction dir;
    private Direction lastMoveDirection = Direction.RIGHT;

    // Animations for each direction
    Animation<TextureRegion> moveUp, moveDown, moveLeft, moveRight;
    private Entity.Direction direction; // ? bi?n này ?ã t?n t?i

    /**
     * Constructs a Knight entity.
     *
     * @param gs the current GameScreen
     * @param staminaBar the player's stamina bar
     * @param hpBar the player's health bar
     */
    public Knight(GameScreen gs, StaminaBar staminaBar, HpBar hpBar) {
        this.gs = gs;
        this.staminaBar = staminaBar;
        this.hpBar = hpBar;
        this.speed = baseSpeed;
        this.hpBar = hpBar; // Gï¿½n HpBar
        this.hpBar = hpBar; // Gï¿½n HpBars
        setDefaultValue();
        dir = currentDirection;

    }

    /**
     * Set initial values for position, stats, animations, and hitbox.
     */
    private void setDefaultValue() {
        speed = 4;
        stateTime = 0f;
        hasKey = false;

        // Render at screen center
        renderX = GameScreen.SCREEN_WIDTH / 2 - (GameScreen.TILE_SIZE / 2);
        renderY = GameScreen.SCREEN_HEIGHT / 2 - (GameScreen.TILE_SIZE / 2);

        // Define collision area
        solidArea = new Rectangle();
        solidArea.x = 4;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        // Set start position
        positionX = 36 * GameScreen.TILE_SIZE;
        positionY = 28 * GameScreen.TILE_SIZE;

        // Stamina drain and regen rates
        staminaDrainRate = 30f;
        staminaRegenRate = 15f;

        // Load animations
        moveUp = loadUpAnimation();
        moveDown = loadDownAnimation();
        moveLeft = loadLeftAnimation();
        moveRight = loadRightAnimation();
    }

    /**
     * Sets the movement direction and resets animation timer if changed.
     *
     * @param direction
     */
    public void setDirection(Direction direction) {
        if (direction != currentDirection && direction != Direction.IDLE) {
            hasFired = false; // ??i h??ng ? cho phép b?n l?i
        }
        if (direction != Direction.IDLE) {
            lastMoveDirection = direction;
        }
        currentDirection = direction;
    }

    /**
     * Updates the animation state.
     *
     * @param delta frame delta time
     */
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

    /**
     * Set the knight's position on the map.
     *
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    /**
     * Renders the knight on screen based on current animation and direction.
     *
     * @param delta
     */
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
                moveDown;
        };

        TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);
        gs.batch.draw(frame, renderX, renderY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
    }

    /**
     * Handles input for movement, stamina, HP, collisions, and map switching.
     *
     * @param delta
     */
    public void inputHandle(float delta) {
        // Direction input
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

        // Get current stamina/HP
        float currentStamina = staminaBar.getCurrentStamina();
        float currentHp = hpBar.getCurrentHp();

        // Shift key affects HP/Stamina
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && currentStamina > 10f && currentHp > 1f) {
            isRunning = true;
            speed = runSpeed;
            staminaBar.setCurrentStamina(Math.max(0, currentStamina - staminaDrainRate * delta));
            hpBar.setCurrentHp(currentHp - 30 * delta);
        } else {
            isRunning = false;
            speed = baseSpeed;
            if (currentHp < hpBar.getMaxHp()) {
                hpBar.setCurrentHp(currentHp + 10 * delta);
            }
            staminaBar.regenerate(delta);
        }

        // Check tile for map transition
//        int tileX = (positionX + GameScreen.TILE_SIZE / 2) / GameScreen.TILE_SIZE;
//        int tileY = (positionY + GameScreen.TILE_SIZE / 2) / GameScreen.TILE_SIZE;
//        if (gs.map.tileNum[tileY][tileX] == 0) {
//            gs.map.changeMap("maps/map_03.txt");
//            positionX = 10 * GameScreen.TILE_SIZE;
//            positionY = 10 * GameScreen.TILE_SIZE;
//        }
        // Check collisions
        collisionOn = false;
        gs.cCheck.checkTile(this);
        int indexObject = gs.cCheck.checkObject(this, true);
        pickUpObject(indexObject);

        // Move if no collision
        if (!collisionOn) {
            switch (currentDirection) {
                case UP ->
                    positionY -= speed;
                case DOWN ->
                    positionY += speed;
                case LEFT ->
                    positionX -= speed;
                case RIGHT ->
                    positionX += speed;
            }

            // Reset speed after move
            speed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? runSpeed : baseSpeed;
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

    /**
     * Handles interaction with objects based on collision index.
     *
     * @param indexOfObject the index in the object array
     */
    public void pickUpObject(int indexOfObject) {
        if (indexOfObject != -1) {
            String objectName = gs.obj[indexOfObject].name;
            switch (objectName) {
                case "Key" -> {
                    hasKey = true;
                    gs.obj[indexOfObject] = null;
                }
                case "Gate" -> {
                    if (hasKey) {
                        collisionOn = false;
                        gs.obj[indexOfObject].image = new Texture("Object/gate_open.png");
                    }
                }
                case "Cave" -> {
                    if (hasKey) {
                        System.out.println("You win!");
                    }
                }
            }
        }
    }

    /**
     * Load animation for upward movement.
     */
    private Animation<TextureRegion> loadUpAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_up_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.6f, frames);
    }

    private Animation<TextureRegion> loadDownAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_down_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.6f, frames);
    }

    private Animation<TextureRegion> loadLeftAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_left_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.6f, frames);
    }

    private Animation<TextureRegion> loadRightAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_right_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.6f, frames);
    }

    /**
     * Disposes all loaded textures used in the knight.
     */
    public void dispose() {
        if (image != null) {
            image.dispose();
        }

        for (TextureRegion region : moveUp.getKeyFrames()) {
            region.getTexture().dispose();
        }
        for (TextureRegion region : moveDown.getKeyFrames()) {
            region.getTexture().dispose();
        }
        for (TextureRegion region : moveLeft.getKeyFrames()) {
            region.getTexture().dispose();
        }
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
