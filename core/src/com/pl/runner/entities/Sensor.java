package com.pl.runner.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.pl.runner.box2d.SensorUserData;
import com.pl.runner.box2d.UserData;

public class Sensor extends GameActor {

    private boolean sensorEnabled;

    public Sensor(Body body) {
        super(body);
    }

    @Override
    public UserData getUserData() {
        return (SensorUserData) userData;
    }

    public void changeState(){
        sensorEnabled = !sensorEnabled;
    }

    public boolean isSensorEnabled() {
        return sensorEnabled;
    }
}
