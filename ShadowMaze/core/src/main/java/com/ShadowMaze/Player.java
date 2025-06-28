/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze;

/**
 *
 * @author NgKaitou
 */
public class Player {
    private int playerID;
    private String name;
    private int hp;
    private int gold;
    private int positionX;
    private int positionY;
    private int currentLevelID;

    public Player() {}

    public Player(int playerID, String name, int hp, int gold, int positionX, int positionY, int currentLevelID) {
        this.playerID = playerID;
        this.name = name;
        this.hp = hp;
        this.gold = gold;
        this.positionX = positionX;
        this.positionY = positionY;
        this.currentLevelID = currentLevelID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getCurrentLevelID() {
        return currentLevelID;
    }

    public void setCurrentLevelID(int currentLevelID) {
        this.currentLevelID = currentLevelID;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerID=" + playerID +
                ", name='" + name + '\'' +
                ", hp=" + hp +
                ", gold=" + gold +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                ", currentLevelID=" + currentLevelID +
                '}';
    }
}

