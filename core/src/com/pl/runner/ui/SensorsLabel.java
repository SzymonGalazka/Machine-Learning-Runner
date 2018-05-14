package com.pl.runner.ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.pl.runner.utils.Constants;

public class SensorsLabel extends Label {

    public SensorsLabel() {
        super("Sensors: ", prepareLabelStyle());
        init();
    }

    private void init() {
        this.setX(10);
        this.setY(Constants.GAME_HEIGHT-40);
    }

    private static LabelStyle prepareLabelStyle() {
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.ORANGE;

        return labelStyle;
    }
}
