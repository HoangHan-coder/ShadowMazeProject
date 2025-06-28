/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze;

/**
 *
 * @author NgKaitou
 */
public class ItemOnMap {
    private int mapItemID;
    private int levelID;
    private String itemType;
    private String name;
    private int value;
    private String icon;
    private int positionX;
    private int positionY;

    public ItemOnMap(int mapItemID, int levelID, String itemType, String name, int value, String icon, int positionX, int positionY) {
        this.mapItemID = mapItemID;
        this.levelID = levelID;
        this.itemType = itemType;
        this.name = name;
        this.value = value;
        this.icon = icon;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getMapItemID() {
        return mapItemID;
    }

    public void setMapItemID(int mapItemID) {
        this.mapItemID = mapItemID;
    }

    public int getLevelID() {
        return levelID;
    }

    public void setLevelID(int levelID) {
        this.levelID = levelID;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

