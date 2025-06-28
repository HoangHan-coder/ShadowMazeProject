/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze;

/**
 *
 * @author NgKaitou
 */
public class InventoryItem {
    private int itemID;
    private int playerID;
    private String itemType;
    private String name;
    private int value;
    private String icon;
    private int quantity;

    public InventoryItem() {}

    public InventoryItem(int itemID, int playerID, String itemType, String name, int value, String icon, int quantity) {
        this.itemID = itemID;
        this.playerID = playerID;
        this.itemType = itemType;
        this.name = name;
        this.value = value;
        this.icon = icon;
        this.quantity = quantity;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
