package com.pl.runner.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
import com.badlogic.gdx.utils.Timer;
import com.pl.runner.RunnerGame;
import com.pl.runner.box2d.RunnerUserData;
import com.pl.runner.entities.Enemy;
import com.pl.runner.entities.Ground;
import com.pl.runner.entities.Runner;
import com.pl.runner.entities.Sensor;
import com.pl.runner.genes.GeneController;
import com.pl.runner.ui.ScoreLabel;
import com.pl.runner.ui.SensorsLabel;
import com.pl.runner.utils.BodyUtils;
import com.pl.runner.utils.Constants;
import com.pl.runner.utils.WorldUtils;

import java.util.ArrayList;

public class GameStage extends Stage implements ContactListener{
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;
    private RunnerGame game;
    private Rectangle screenRightSide, screenLeftSide;
    private Vector3 touchPoint;
    private World world;
    private Ground ground;
    public static Sensor sensorUpClose, sensorUpFar, sensorDownClose, sensorDownFar;
    private ArrayList<Runner> runners = new ArrayList<Runner>();
    private SensorsLabel sensorsLabel;
    private ScoreLabel scoreLabel;
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;
    private long startTime = System.currentTimeMillis(), score;


    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    public GameStage(RunnerGame game) {
        this.game=game;
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
        initSensorsLabel();
        initScoreLabel();
    }

    private void initScoreLabel() {
        scoreLabel = new ScoreLabel();
        addActor(scoreLabel);
    }

    private void initSensorsLabel() {
        sensorsLabel = new SensorsLabel();
        addActor(sensorsLabel);
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
        for(int i=0;i<50;i++){
            runners.add(new Runner(WorldUtils.createRunner(world)));
            addActor(runners.get(i));
        }
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
            if (BodyUtils.bodyIsEnemy(body) && !runners.isEmpty()) {
                initEnemy();
            }
            world.destroyBody(body);
        }
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        updateLabels();
        isOver();
        for (Runner runner:runners) {
            GeneController.calculateOutput(runner);
        }

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
    }

    private void isOver() {
        if(runners.isEmpty()) {
            saveProgress();
            game.create();
        }
    }


    private void updateLabels() {
        sensorsLabel.setText("Stan sensorow: \n"+sensorUpClose.isSensorEnabled()+" "+sensorUpFar.isSensorEnabled()+"\n"+sensorDownClose.isSensorEnabled()+" "+sensorDownFar.isSensorEnabled());
        score=(System.currentTimeMillis()-startTime)/100;
        scoreLabel.setText("Najlepsza generacja: "+GeneController.getTopGeneration()+"    Najlepszy wynik: "+GeneController.getTopScore()+"\n Aktualna generacja: "+game.getGeneration()+"     Aktualny wynik: "+score+"\n Ilość żywych biegaczy: "+runners.size());
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
            runners.get(1).jump();
        }else if (leftSideTouched(touchPoint.x, touchPoint.y)) {
            runners.get(1).dodge();
        }

        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (runners.get(1).isDodging()) {
            runners.get(1).stopDodge();
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }
    @Override
    public void beginContact(Contact contact) {

        final Body a = contact.getFixtureA().getBody();
        final Body b = contact.getFixtureB().getBody();
        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEnemy(b)) ||
                (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsRunner(b))) {
            Body c;
            if(a.getUserData() instanceof RunnerUserData) c = a;
            else c =  b;
            for (final Runner r : runners){
                if(r.getUserData() == c.getUserData()){
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            runners.remove(r);
                        }
                    },1);
                    break;
                }
            }

        }else if ((BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsSensor(b)) ||
                (BodyUtils.bodyIsSensor(a) && BodyUtils.bodyIsEnemy(b))) {
            Body c;
            if(BodyUtils.bodyIsSensor(a)) c = contact.getFixtureA().getBody();
            else c = contact.getFixtureB().getBody();
            Vector2 whichSensor = c.getPosition();
            //System.out.println(c.getPosition()); wypisanie wektora kolizji
            if(whichSensor.x > 3.5 && whichSensor.y > 2) sensorUpFar.changeState(true);
            else if(whichSensor.x > 3.5 && whichSensor.y < 2) sensorDownFar.changeState(true);
            else if(whichSensor.x <= 3.5 && whichSensor.y > 2) sensorUpClose.changeState(true);
            else if(whichSensor.x <= 3.5 && whichSensor.y < 2) sensorDownClose.changeState(true);
        } else if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
        (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {
            Body c;
            if(a.getUserData() instanceof RunnerUserData) c = a;
            else c =  b;
            for (Runner r : runners){
                if(r.getUserData() == c.getUserData()){
                    r.landed();
                    break;
                }
            }
        }
    }

    private void saveProgress() {
        if(score>GeneController.getTopScore()) {
            GeneController.setTopGeneration(game.getGeneration());
            GeneController.setTopScore(score);
        }
    }


    @Override
    public void endContact(Contact contact) {
        sensorUpFar.changeState(false);
        sensorUpClose.changeState(false);
        sensorDownFar.changeState(false);
        sensorDownClose.changeState(false);
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
