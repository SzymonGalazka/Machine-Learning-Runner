package com.pl.runner.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.pl.runner.entities.Enemy;
import com.pl.runner.entities.Ground;
import com.pl.runner.entities.Runner;
import com.pl.runner.entities.Sensor;
import com.pl.runner.utils.BodyUtils;
import com.pl.runner.utils.Constants;
import com.pl.runner.utils.WorldUtils;

public class GameStage extends Stage implements ContactListener{
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;
    private Rectangle screenRightSide, screenLeftSide;

    private Vector3 touchPoint;
    private World world;
    private Ground ground;
    private Sensor sensorUpClose, sensorUpFar, sensorDownClose, sensorDownFar;
    private Runner runner, runner2, runner3;
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    public GameStage() {
        initWorld();
        setupCamera();
        setupTouchControlAreas();
        renderer = new Box2DDebugRenderer();
    }

    private void initWorld() {
        world = WorldUtils.createWorld();
        world.setContactListener(this);
        initGround();
        initRunner();
        initEnemy();
        initSensor();
    }

    private void initGround() {
        ground = new Ground(WorldUtils.createGround(world));
        addActor(ground);
    }

    private void initSensor(){
        sensorUpClose = new Sensor(WorldUtils.createSensor(world, Constants.SENSOR_CLOSE_X,Constants.SENSOR_UP_Y));
        sensorUpFar = new Sensor(WorldUtils.createSensor(world, Constants.SENSOR_FAR_X,Constants.SENSOR_UP_Y));
        sensorDownClose = new Sensor(WorldUtils.createSensor(world, Constants.SENSOR_CLOSE_X,Constants.SENSOR_DOWN_Y));
        sensorDownFar = new Sensor(WorldUtils.createSensor(world, Constants.SENSOR_FAR_X,Constants.SENSOR_DOWN_Y));
        addActor(sensorUpClose);
        addActor(sensorUpFar);
        addActor(sensorDownClose);
        addActor(sensorDownFar);
    }
    private void initRunner() {
        runner = new Runner(WorldUtils.createRunner(world));
        //runner2 = new Runner(WorldUtils.createRunner(world));
        //runner3 = new Runner(WorldUtils.createRunner(world));
        addActor(runner);
        //addActor(runner2);
        //addActor(runner3);
    }

    private void initEnemy() {
        Enemy enemy = new Enemy(WorldUtils.createEnemy(world));
        addActor(enemy);
    }
    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    private void update(Body body) {
        if (!BodyUtils.bodyInBounds(body)) {
            if (BodyUtils.bodyIsEnemy(body) && !runner.isHit()) {
                initEnemy();
            }
            world.destroyBody(body);
        }
    }
    @Override
    public void act(float delta) {
        super.act(delta);

        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            update(body);
        }
        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

        //TODO: Implement interpolation

    }

    @Override
    public void draw() {
        super.draw();
        renderer.render(world, camera.combined);
    }
    private void setupTouchControlAreas() {
        touchPoint = new Vector3();
        screenLeftSide = new Rectangle(0, 0, getCamera().viewportWidth / 2, getCamera().viewportHeight);
        screenRightSide = new Rectangle(getCamera().viewportWidth / 2, 0, getCamera().viewportWidth / 2,
                getCamera().viewportHeight);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        translateScreenToWorldCoordinates(x, y);

        if (rightSideTouched(touchPoint.x, touchPoint.y)) {
            runner.jump();
        }else if (leftSideTouched(touchPoint.x, touchPoint.y)) {
            runner.dodge();
        }

        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (runner.isDodging()) {
            runner.stopDodge();
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }
    @Override
    public void beginContact(Contact contact) {

        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEnemy(b)) ||
                (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsRunner(b))) {
            runner.hit();
        } else if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {
            runner.landed();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void translateScreenToWorldCoordinates(int x, int y) {
        getCamera().unproject(touchPoint.set(x, y, 0));
    }

    private boolean leftSideTouched(float x, float y) {
        return screenLeftSide.contains(x, y);
    }
    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }
}
