package com.busedc.flybirdfly;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

public class MainMenu extends ApplicationAdapter implements  GestureDetector.GestureListener {
    public static SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer Renderer;
    private Camera camera;
    private TitleScreen titleScreen;
    public static Preferences prefs;
    private static Bird Bird;
    private static Ground Ground;
    public static BG BG;
    public static SoundEngine Sound;
    private int width;
    private int height;

    private Random r = new Random();

    public MainMenu() {
    }

    @Override
    public void create() {
        Box2D.init();
        batch = new SpriteBatch();

        world = new World(new Vector2(0, Constants.WORLD_GRAVITY), true);

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(
                width / Constants.PPM,
                height / Constants.PPM);
        Renderer = new Box2DDebugRenderer();

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);

        world.setContactListener(new ListenerClass());

        prefs = Gdx.app.getPreferences("PreferenceName");// We store the value 10 with the key of "highScore"

        Sound = new SoundEngine();
        Sound.MUTE = prefs.getBoolean("mute");

        Bird = new Bird(world, new Vector2(0, Constants.BIRD_STARTING_Y), "bird.png", 1.5f);
        Bird.body.setGravityScale(0f);
        Bird.animate();
        Ground = new Ground(world,
                new Vector2(0, Constants.GROUND_OFFSET / Constants.PPM - height / (2 * Constants.PPM)),
                new Vector2(camera.viewportWidth, height / (4f * Constants.PPM)),
                new Vector2(width, Constants.GROUND_OFFSET + height / 4f),
                "bg/ground.png");
        BG = new BG(width, height);
        titleScreen = new TitleScreen(width ,height);
    }

    public void moveScene()
    {
        BG.update();
        Ground.update();
        //Update the bird sprite position and angle
        Bird.update((width - 34f * 4f) / 2,
                width - 1.5f * Constants.PPM + Bird.body.getPosition().y * Constants.PPM);
    }

    @Override
    public void render () {
        camera.update();
        Gdx.gl.glClearColor(BG.R, BG.G, BG.B, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Renderer.render(world, camera.combined);
        batch.begin();
        BG.draw(batch);
        Ground.draw(batch);
        Bird.draw(batch);
        titleScreen.draw(batch);
        batch.end();
        world.step(1 / 60f, 6, 2);
        moveScene();
    }

    @Override
    public void dispose () {
        Bird.spriteSheet.dispose();
        batch.dispose();
        world.dispose();
        Renderer.dispose();
        Sound.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        titleScreen.handleInput(x, y);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    public static class ListenerClass implements ContactListener {
        @Override
        public void endContact(Contact contact) {
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }

        @Override
        public void beginContact(Contact contact) {
        }
    }
}
