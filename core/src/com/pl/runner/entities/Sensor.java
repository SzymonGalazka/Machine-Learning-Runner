package com.pl.runner.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.pl.runner.box2d.SensorUserData;

public class Sensor extends GameActor {

    private boolean sensorEnabled;
    public static int sensorNumber;
    public Sensor(Body body) {
        super(body);
        sensorNumber++;
    }

    @Override
    public SensorUserData getUserData() {
        return (SensorUserData) userData;
    }

    public void changeState(boolean state){
        sensorEnabled = state;
    }


    public int isSensorEnabled() {
        return sensorEnabled ? 1 : 0;
    }

}
