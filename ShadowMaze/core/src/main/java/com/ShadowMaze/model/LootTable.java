/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

/**
 *
 * @author NgKaitou
 */
public class LootTable {
    private int lootID;
    private int monsterID;
    private String itemType;
    private String name;
    private float dropRate;
    private int value;
    private String icon;

    public LootTable(int lootID, int monsterID, String itemType, String name, float dropRate, int value, String icon) {
        this.lootID = lootID;
        this.monsterID = monsterID;
        this.itemType = itemType;
        this.name = name;
        this.dropRate = dropRate;
        this.value = value;
        this.icon = icon;
    }


    public int getLootID() {
        return lootID;
    }

    public void setLootID(int lootID) {
        this.lootID = lootID;
    }

    public int getMonsterID() {
        return monsterID;
    }

    public void setMonsterID(int monsterID) {
        this.monsterID = monsterID;
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

    public float getDropRate() {
        return dropRate;
    }

    public void setDropRate(float dropRate) {
        this.dropRate = dropRate;
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
}
