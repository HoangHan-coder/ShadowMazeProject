/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

/**
 *
 * @author NgKaitou
 */
public class Maze {
    private int levelID;
    private int mazeSeed;
    private int width;
    private int height;

    public Maze() {}

    public Maze(int levelID, int mazeSeed, int width, int height) {
        this.levelID = levelID;
        this.mazeSeed = mazeSeed;
        this.width = width;
        this.height = height;
    }

    public int getLevelID() {
        return levelID;
    }

    public void setLevelID(int levelID) {
        this.levelID = levelID;
    }

    public int getMazeSeed() {
        return mazeSeed;
    }

    public void setMazeSeed(int mazeSeed) {
        this.mazeSeed = mazeSeed;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Maze{" +
                "levelID=" + levelID +
                ", mazeSeed=" + mazeSeed +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
