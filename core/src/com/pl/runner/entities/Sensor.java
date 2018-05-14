package com.pl.runner.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pl.runner.box2d.SensorUserData;
import com.pl.runner.box2d.UserData;
import com.pl.runner.utils.BodyUtils;

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


    public boolean isSensorEnabled() {
        return sensorEnabled;
    }

    public int getSensorNumber() {
        return sensorNumber;
    }
}
