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
import com.ShadowMaze.skill.Fireball;
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
import com.badlogic.gdx.math.Vector2;
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
    public int hasKey;
    private Array<Sword> skills = new Array<>();
    public boolean isRunning = false;  
    public int baseSpeed = 5;         
    private boolean hasFired = false; 
    // Movement and stamina management
    public final int runSpeed = 18;
    private float staminaDrainRate;

    // UI elements
    private HpBar hpBar;

    // Game context
    GameScreen gs;
    public boolean hasScrollFire;
    public boolean hasScrollIce;
    public boolean hasScrollThunder;
    public int countOpenChest = 0;
    //skill
    public Fireball currentFireball;

    // Animation timing
    float stateTime;
    private Direction dir;
    private Direction lastMoveDirection = Direction.RIGHT;

    // Animations for each direction
    Animation<TextureRegion> moveUp, moveDown, moveLeft, moveRight;
    private Entity.Direction direction; // ? bi?n n�y ?� t?n t?i

    /**
     * Constructs a Knight entity.
     *
     * @param gs the current GameScreen
     * @param hpBar the player's health bar
     */
    public Knight(GameScreen gs, HpBar hpBar) {
        this.gs = gs;
//        this.staminaBar = staminaBar;
        this.hpBar = hpBar;
        this.speed = baseSpeed;
        this.hpBar = hpBar; // G�n HpBar
        this.hpBar = hpBar; // G�n HpBars
        setDefaultValue();
        dir = currentDirection;

    }

    /**
     * Set initial values for position, stats, animations, and hitbox.
     */
    private void setDefaultValue() {
        speed = 4;
        stateTime = 0f;
        hasKey = 0;

        // Render at screen center
        renderX = GameScreen.SCREEN_WIDTH / 2 - (GameScreen.TILE_SIZE / 2) - GameScreen.TILE_SIZE;
        renderY = (GameScreen.SCREEN_HEIGHT / 2 - (GameScreen.TILE_SIZE / 2)) - GameScreen.TILE_SIZE * 2;

        // Define collision area
        solidArea = new Rectangle();
        solidArea.x = 4;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        // Set start map 1 position
        positionX = 60 * GameScreen.TILE_SIZE;
        positionY = 122 * GameScreen.TILE_SIZE;
        // set start map 2 position
//        positionX = 70 * GameScreen.TILE_SIZE;
//        positionY = 43 * GameScreen.TILE_SIZE;

        // Stamina drain and regen rates
        staminaDrainRate = 30f;

        hasScrollFire = false;
        hasScrollIce = false;
        hasScrollThunder = false;

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
            hasFired = false; // ??i h??ng ? cho ph�p b?n l?i
        }
        if (direction != Direction.IDLE) {
            lastMoveDirection = direction;
        }
        currentDirection = direction;
    }

    public void castFireball() {
        if (currentFireball == null || !currentFireball.isActive()) {
            currentFireball = new Fireball();
            currentFireball.setMapSize(gs.map.getMapWidthPixels(), gs.map.getMapHeightPixels());
            Direction fireDir = (currentDirection != Direction.IDLE) ? currentDirection : lastMoveDirection;
            currentFireball.activate(new Vector2(positionX, positionY), fireDir);
            currentFireball.shoot();
        }
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

//            if (skill.isActive() && currentDirection != Direction.IDLE && skill.getDirection() != currentDirection) {
//                skill.setActive(false);  
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
        if (isDead == false) {
            gs.batch.draw(frame, renderX, renderY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        } else {
            gs.batch.draw(image, renderX, renderY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        }

        if (currentFireball != null && currentFireball.isActive()) {
            currentFireball.update(Gdx.graphics.getDeltaTime());
            currentFireball.render(gs.batch, positionX + GameScreen.TILE_SIZE, positionY + GameScreen.TILE_SIZE * 2);
        }

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
        if (!isDead) {
            movementHandle(delta);
        }
    }

    /**
     * Handles input for movement, stamina, HP, collisions, and map switching.
     *
     * @param delta
     */
    public void movementHandle(float delta) {
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

        //use skill
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            castFireball();
        }

        // Get current stamina/HP
        float currentHp = hpBar.getCurrentHp();

        // If HP > 0 and SHIFT is being held down => run faster
        if (currentHp > 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                isRunning = true;
                speed = runSpeed;
                // Decrease HP while running
                hpBar.setCurrentHp(Math.max(0, currentHp - 30 * delta));
            } else {
                isRunning = false;
                speed = baseSpeed;
                // Regenerate HP when not running
                hpBar.setCurrentHp(Math.min(hpBar.getMaxHp(), currentHp + 10 * delta));
            }
        } else {
            // If HP is 0, running is disabled even if SHIFT is pressed
            isRunning = false;
            speed = baseSpeed;

            // Allow HP regeneration only when not holding SHIFT
            if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                hpBar.setCurrentHp(currentHp + 10 * delta);
            }
        }

        // Check collisions
        collisionOn = false;
        gs.cCheck.checkTile(this);

        int indexObject = gs.cCheck.checkObject(this, true);
        pickUpObject(indexObject);

        gs.cCheck.checkEnemyCollision(this);

        if (currentFireball != null && currentFireball.isActive()) {
            gs.cCheck.checkFireballCollision(currentFireball);
        }
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
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !hasFired) {
            Entity.Direction skillDir = (currentDirection == Direction.IDLE)
                    ? lastMoveDirection
                    : currentDirection;

            Sword skill = new Sword(skillDir);
            skill.activate(this, skillDir);
            hasFired = true; // ?�nh d?u ?� b?n
            skills.add(skill);
        }

// Reset hasFired n?u kh�ng c�n nh?n SPACE
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
            System.out.println("Knight collided with: " + gs.obj[indexOfObject].name);
            System.out.println("Knight collide : " + collisionOn);
            String objectName = gs.obj[indexOfObject].name;
            switch (objectName) {
                case "Key" -> {
                    hasKey += 1;
                    gs.obj[indexOfObject] = null;
                }
                case "Gate" -> {
                    if (hasKey > 0 && gs.obj[indexOfObject].collision) {
                        hasKey -= 1;
                        gs.obj[indexOfObject].collision = false;
                        gs.obj[indexOfObject].image = new Texture("Object/gate_open.png");
                    }
                }
                case "Coin" -> {
                    gs.scoreBoard.addScore(1);
                    gs.obj[indexOfObject] = null;
                }
                case "Cave" -> {
                    if (hasKey > 0) {
                        System.out.println("You win!");
                    }
                }
                case "Enemy" -> {
                    gs.isGameOver = true;
                    System.out.println("You die!");
                }
                case "ScollFireChest" -> {
                    if(!gs.obj[indexOfObject].isOpened) {
                        gs.scoreBoard.addScore(14);
                        countOpenChest++;
                    }
                    hasScrollFire = true;
                    System.out.println("You get a scoll-Fire!");
                    gs.obj[11].image = new Texture("Object/fire_gem.png");
                    gs.obj[14].image = new Texture("Object/fire_gem.png");
                    gs.obj[indexOfObject].isOpened = true;
                    gs.obj[indexOfObject].image = new Texture("Object/big_chest_open.png");
                }
                case "ScollIceChest" -> {
                    if(!gs.obj[indexOfObject].isOpened) {
                        gs.scoreBoard.addScore(14);
                        countOpenChest++;
                    }
                    hasScrollIce = true;
                    System.out.println("You get a scoll-Ice!");
                    gs.obj[12].image = new Texture("Object/aqua_gem.png");
                    gs.obj[15].image = new Texture("Object/aqua_gem.png");
                    gs.obj[indexOfObject].isOpened = true;
                    gs.obj[indexOfObject].image = new Texture("Object/big_chest_open.png");
                }
                case "ScollThunderChest" -> {
                    if(!gs.obj[indexOfObject].isOpened) {
                        gs.scoreBoard.addScore(14);
                        countOpenChest++;
                    }
                    hasScrollThunder = true;
                    System.out.println("You get a scoll-Thunder!");
                    gs.obj[13].image = new Texture("Object/thunder_gem.png");
                    gs.obj[16].image = new Texture("Object/thunder_gem.png");
                    gs.obj[indexOfObject].isOpened = true;
                    gs.obj[indexOfObject].image = new Texture("Object/big_chest_open.png");
                }
                case "Cave exit" -> {
                    if (countOpenChest == 3 || countOpenChest == 7) {
                        countOpenChest++;
                        gs.obj[indexOfObject].isOpened = true;
                        gs.obj[indexOfObject].image = new Texture("Object/cave_exit_open.png");
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

    public Vector2 getPosition() {
        return new Vector2(positionX, positionY);
    }

    public Direction getDirection() {
        return this.currentDirection != null ? this.currentDirection : Direction.IDLE;
    }

    public Direction getLastMoveDirection() {
        return lastMoveDirection;
    }
}
