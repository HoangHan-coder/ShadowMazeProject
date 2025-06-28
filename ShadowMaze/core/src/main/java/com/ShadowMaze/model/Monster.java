/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

/**
 *
 * @author NgKaitou
 */
public class Monster {
    private int monsterID;
    private int levelID;
    private int hp;
    private int positionX;
    private int positionY;

    public Monster() {}

    public Monster(int monsterID, int levelID, int hp, int positionX, int positionY) {
        this.monsterID = monsterID;
        this.levelID = levelID;
        this.hp = hp;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getMonsterID() {
        return monsterID;
    }

    public void setMonsterID(int monsterID) {
        this.monsterID = monsterID;
    }

    public int getLevelID() {
        return levelID;
    }

    public void setLevelID(int levelID) {
        this.levelID = levelID;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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
}

