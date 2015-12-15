package com.example.lazcarate.minions2;

import android.content.Context;
import android.widget.Button;

/**
 * Created by lazcarate on 14/12/15.
 */
public class Celdas extends Button {

    private int posX = 0;
    private int posY = 0;
    private boolean inUse = false;;
    private boolean isClicked = false;
    private boolean isFlagged = false;
    private int surrounding = 0;

    public Celdas(Context context) {
        super(context);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public boolean getClicked() {
        return isClicked;
    }

    public void setClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setIsFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public int getSurrounding() {
        return surrounding;
    }

    public void setSurrounding(int surrounding) {
        this.surrounding = surrounding;
    }
}
