/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a single tile on the game map.
 * Each tile has an image (texture) and a collision flag that determines
 * whether the player or entities can walk through it.
 * 
 * Example usage: grass tile (no collision), wall tile (with collision).
 */
public class Tile {

    /** The visual texture displayed for this tile */
    public Texture image;

    /**
     * Indicates if this tile is solid (true = blocks movement).
     * Default is false, meaning the tile is walkable.
     */
    public boolean collision = false;
}

