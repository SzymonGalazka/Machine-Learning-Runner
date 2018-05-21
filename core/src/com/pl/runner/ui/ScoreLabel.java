package com.pl.runner.ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.pl.runner.utils.Constants;

public class ScoreLabel extends Label {

    public ScoreLabel() {
        super("Score: ", prepareLabelStyle());
        init();
    }

    private void init() {
        this.setX(Constants.GAME_WIDTH-180);
        this.setY(Constants.GAME_HEIGHT-40);
    }

    private static LabelStyle prepareLabelStyle() {
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.CHARTREUSE;

        return labelStyle;
    }
}
