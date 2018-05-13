package com.pl.runner.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.pl.runner.box2d.RunnerUserData;

public class Runner extends GameActor {

    private boolean isJumping, isDodging, isHit;

    public Runner(Body body) {
        super(body);
    }

    @Override
    public RunnerUserData getUserData() {
        return (RunnerUserData) userData;
    }

    public void jump(){
        if(!(isJumping || isDodging) || isHit){
            body.applyLinearImpulse(getUserData().getJumpingLinearImpulse(),body.getWorldCenter(),true);
            isJumping = true;
        }
    }

    public void landed(){
        isJumping = false;
    }

    public void dodge() {
        if (!(isJumping || isHit)) {
            body.setTransform(getUserData().getDodgePosition(), getUserData().getDodgeAngle());
            isDodging = true;
        }
    }

    public void stopDodge() {
        isDodging = false;
        if (!isHit) {
            body.setTransform(getUserData().getRunningPosition(), 0f);
        }
    }

    public void hit() {
        body.applyAngularImpulse(getUserData().getHitAngularImpulse(), true);
        isHit = true;
    }

    public boolean isHit() {
        return isHit;
    }

    public boolean isDodging() {
        return isDodging;
    }
}
