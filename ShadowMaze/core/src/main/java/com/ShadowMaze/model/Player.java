/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.ShadowMaze.screen.GameScreen;

/**
 * Lớp Player đại diện cho người chơi trong game, bao gồm các thông tin cơ bản
 * như vị trí, trạng thái, tài sản và thông tin hiển thị để hỗ trợ vẽ mượt.
 * 
 * @author NgKaitou
 */
public class Player {

    // ID duy nhất của người chơi (có thể dùng để lưu vào database)
    private int playerID;

    // Tên người chơi
    private String name;

    // Máu hiện tại của người chơi
    private int hp;

    // Số vàng người chơi đang sở hữu
    private int gold;

    // Vị trí logic theo trục X (theo ô tile trong bản đồ)
    private int positionX;

    // Vị trí logic theo trục Y (theo ô tile trong bản đồ)
    private int positionY;

    // ID của màn chơi hiện tại mà người chơi đang ở
    private int currentLevelID;

    // Tốc độ rơi/nhảy theo phương Y (dùng cho trọng lực và nhảy)
    private float velocityY = 0;

    // Cờ kiểm tra người chơi đang trong trạng thái nhảy hay không
    private boolean isJumping = false;

    // Tọa độ vẽ theo trục X (giúp di chuyển mượt hơn giữa các ô)
    private float renderX;

    // Tọa độ vẽ theo trục Y (giúp vẽ mượt hơn khi rơi/nhảy)
    private float renderY;

    /**
     * Constructor mặc định không truyền tham số.
     */
    public Player() {
    }

    /**
     * Constructor khởi tạo player đầy đủ thông tin.
     */
    public Player(int playerID, String name, int hp, int gold, int positionX, int positionY, int currentLevelID) {
        this.playerID = playerID;
        this.name = name;
        this.hp = hp;
        this.gold = gold;
        this.positionX = positionX;
        this.positionY = positionY;
        this.currentLevelID = currentLevelID;
    }

    // Getter và Setter cho playerID
    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho hp (máu)
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    // Getter và Setter cho gold (vàng)
    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    // Getter và Setter cho vị trí X (logic - tile)
    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    // Getter và Setter cho vị trí Y (logic - tile)
    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    // Getter và Setter cho ID màn chơi hiện tại
    public int getCurrentLevelID() {
        return currentLevelID;
    }

    public void setCurrentLevelID(int currentLevelID) {
        this.currentLevelID = currentLevelID;
    }

    // Getter và Setter cho velocityY (tốc độ nhảy/rơi)
    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    // Getter và Setter cho trạng thái nhảy
    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    // Getter và Setter cho tọa độ vẽ X
    public float getRenderX() {
        return renderX;
    }

    public void setRenderX(float renderX) {
        this.renderX = renderX;
    }

    // Getter và Setter cho tọa độ vẽ Y
    public float getRenderY() {
        return renderY;
    }

    public void setRenderY(float renderY) {
        this.renderY = renderY;
    }

    /**
     * Trả về chuỗi mô tả thông tin chính của player.
     */
    @Override
    public String toString() {
        return "Player{"
                + "playerID=" + playerID
                + ", name='" + name + '\''
                + ", hp=" + hp
                + ", gold=" + gold
                + ", positionX=" + positionX
                + ", positionY=" + positionY
                + ", currentLevelID=" + currentLevelID
                + '}';
    }
}
