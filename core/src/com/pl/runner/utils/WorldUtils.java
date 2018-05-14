package com.pl.runner.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pl.runner.box2d.EnemyUserData;
import com.pl.runner.box2d.GroundUserData;
import com.pl.runner.box2d.RunnerUserData;
import com.pl.runner.box2d.SensorUserData;
import com.pl.runner.enums.EnemyType;

public class WorldUtils {

    public static Body createRunner(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.RUNNER_WIDTH / 2, Constants.RUNNER_HEIGHT / 2);
        Body body = world.createBody(bodyDef);
        body.setGravityScale(Constants.RUNNER_GRAVITY_SCALE);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.RUNNER_DENSITY;
        fixtureDef.filter.categoryBits  = Constants.BIT_PLAYERSENSORS;
        fixtureDef.filter.maskBits = Constants.BIT_GROUNDENEMY;
        body.createFixture(fixtureDef);
        body.resetMassData();
        body.setUserData(new RunnerUserData(Constants.RUNNER_WIDTH, Constants.RUNNER_HEIGHT));
        shape.dispose();
        return body;
    }

    public static Body createEnemy(World world) {
        EnemyType enemyType = RandomUtils.getRandomEnemyType();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(new Vector2(enemyType.getX(), enemyType.getY()));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(enemyType.getWidth() / 2, enemyType.getHeight() / 2);
        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.ENEMY_DENSITY;
        fixtureDef.filter.categoryBits  = Constants.BIT_GROUNDENEMY;
        fixtureDef.filter.maskBits = Constants.BIT_PLAYERSENSORS;
        body.createFixture(fixtureDef);
        body.resetMassData();
        EnemyUserData userData = new EnemyUserData(enemyType.getWidth(), enemyType.getHeight());
        body.setUserData(userData);
        shape.dispose();
        return body;
    }

    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

    public static Body createGround(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(Constants.GROUND_X, Constants.GROUND_Y));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.GROUND_WIDTH / 2, Constants.GROUND_HEIGHT / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.GROUND_DENSITY;
        fixtureDef.filter.categoryBits  = Constants.BIT_GROUNDENEMY;
        fixtureDef.filter.maskBits = Constants.BIT_PLAYERSENSORS;
        body.createFixture(fixtureDef);
        body.setUserData(new GroundUserData());
        shape.dispose();
        return body;
    }

    public static Body createSensor(World world, float x,float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x,y));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.SENSOR_WIDTH/2, Constants.SENSOR_HEIGHT/2);
        body.setGravityScale(Constants.SENSOR_GRAVITY_SCALE);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.SENSOR_DENSITY;
        fixtureDef.filter.categoryBits  = Constants.BIT_PLAYERSENSORS;
        fixtureDef.filter.maskBits = Constants.BIT_GROUNDENEMY;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);
        body.setUserData(new SensorUserData());
        shape.dispose();
        return body;
    }
}
