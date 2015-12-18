package com.example.lazcarate.minions2;

import android.content.Context;
import android.widget.Button;

/**
 * Created by lazcarate on 14/12/15.
 */
public class Celdas extends Button {

    private int posX = 0;
    private int posY = 0;
    private boolean isClicked = false;


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
    public boolean getClicked() {
        return isClicked;
    }
    public void setClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }
}
