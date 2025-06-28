/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.core;

/**
 *
 * @author NgKaitou
 */
import java.time.LocalDateTime;

public class GameState {
    private int gameStateID;
    private int playerID;
    private int currentLevelID;
    private int mazeSeed;
    private LocalDateTime saveTime;

    public GameState(int gameStateID, int playerID, int currentLevelID, int mazeSeed, LocalDateTime saveTime) {
        this.gameStateID = gameStateID;
        this.playerID = playerID;
        this.currentLevelID = currentLevelID;
        this.mazeSeed = mazeSeed;
        this.saveTime = saveTime;
    }

    public int getGameStateID() {
        return gameStateID;
    }

    public void setGameStateID(int gameStateID) {
        this.gameStateID = gameStateID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getCurrentLevelID() {
        return currentLevelID;
    }

    public void setCurrentLevelID(int currentLevelID) {
        this.currentLevelID = currentLevelID;
    }

    public int getMazeSeed() {
        return mazeSeed;
    }

    public void setMazeSeed(int mazeSeed) {
        this.mazeSeed = mazeSeed;
    }

    public LocalDateTime getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(LocalDateTime saveTime) {
        this.saveTime = saveTime;
    }

    
}

