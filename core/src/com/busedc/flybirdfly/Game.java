package com.busedc.flybirdfly;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

public class Game extends ApplicationAdapter implements  GestureDetector.GestureListener  {
	private SpriteBatch batch;
	private World world;
	private Box2DDebugRenderer Renderer;
	private Camera camera;

	private static Bird Bird;
	private Ground Ground;
	private BG BG;
	private Tube[] lowerTubes;
	private Tube[] upperTubes;
	private Music bg_music;
	private float[] tubeHeights = {-20f, -20f, -20f};

	private int width;
	private int height;
	private int activeTube = 0;
	private int SCORE = 0;

	private static boolean GAME_OVER = false;
	private boolean updateScore = false;

	private Random r = new Random();

	public Game()
	{
	}

		@Override
	public void create () {
		Box2D.init();

		batch = new SpriteBatch();

		world = new World(new Vector2(0, Constants.WORLD_GRAVITY), true);

		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(
				(float) Math.floor((float)width / Constants.PPM),
				(float) Math.floor((float)height / Constants.PPM));
				Renderer = new Box2DDebugRenderer();

		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);

		world.setContactListener(new ListenerClass());

		Bird = new Bird(world, new Vector2(0,Constants.BIRD_STARTING_Y), "bird.png", 1.5f);
		Ground = new Ground(world,
				new Vector2(0, Constants.GROUND_OFFSET / Constants.PPM - height / (2 * Constants.PPM)),
				new Vector2(camera.viewportWidth, height / (4f * Constants.PPM)),
				new Vector2( width,Constants.GROUND_OFFSET + height / 4f),
				"ground.png");
		BG = new BG(width, height);

		randomizeTubeHeights();

		lowerTubes = new Tube[3];
		for(int i = 0; i < 3; ++i)
		{
			lowerTubes[i] = new Tube(world, tubeHeights[i], i, 0, "tube.png", width, height);
		}

		upperTubes = new Tube[3];
		for(int i = 0; i < 3; ++i)
		{
			upperTubes[i] = new Tube(world, tubeHeights[i], i, 1,"tube.png", width, height);
		}
	}

	public void randomizeTubeHeights() {
		tubeHeights[0] = Constants.RND_MIN_TUBE_HEIGHT + r.nextFloat() * (Constants.RND_MAX_TUBE_HEIGHT - Constants.RND_MIN_TUBE_HEIGHT);
		tubeHeights[1] = Constants.RND_MIN_TUBE_HEIGHT + r.nextFloat() * (Constants.RND_MAX_TUBE_HEIGHT - Constants.RND_MIN_TUBE_HEIGHT);
		tubeHeights[2] = Constants.RND_MIN_TUBE_HEIGHT + r.nextFloat() * (Constants.RND_MAX_TUBE_HEIGHT - Constants.RND_MIN_TUBE_HEIGHT);
	}

	public void randomizeTubeHeights(int i)
	{
		tubeHeights[i] = Constants.RND_MIN_TUBE_HEIGHT + r.nextFloat() * (Constants.RND_MAX_TUBE_HEIGHT - Constants.RND_MIN_TUBE_HEIGHT);
	}

	public void recycleTubes()
	{
		//Reset the tubes positions once they travel through the screen
		for(int i = 0; i < 3; ++i)
		{
			if(lowerTubes[i].body.getPosition().x < -width / Constants.PPM)
			{
				randomizeTubeHeights(i);
				if(i == 0)
				{
					upperTubes[i].body.setTransform(lowerTubes[2].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i] + 2 * Constants.TUBE_HHEIGHT + Constants.TUBE_Y_DIST, 0);
					lowerTubes[i].body.setTransform(lowerTubes[2].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i], 0);
				}
				else if(i == 1)
				{
					upperTubes[i].body.setTransform(lowerTubes[0].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i] + 2 * Constants.TUBE_HHEIGHT + Constants.TUBE_Y_DIST, 0);
					lowerTubes[i].body.setTransform(lowerTubes[0].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i], 0);
				}
				else
				{
					upperTubes[i].body.setTransform(lowerTubes[1].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i] + 2 * Constants.TUBE_HHEIGHT + Constants.TUBE_Y_DIST, 0);
					lowerTubes[i].body.setTransform(lowerTubes[1].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i], 0);
				}
				break;
			}
			else if(lowerTubes[i].body.getPosition().x + Constants.TUBE_HWIDTH < 0)
			{
				updateScore = true;
			}
		}
	}

	public void moveScene()
	{
		Ground.update();
		BG.update();
		for(int i = 0; i < 3; ++i)
		{
			lowerTubes[i].update();
			upperTubes[i].update();
		}
		//Update the bird sprite position and angle
		Bird.update((width - Bird.sprite.getWidth()) / 2,
				width - 1.5f * Constants.PPM + Bird.body.getPosition().y * Constants.PPM);
		//While we're at it update the score if the bird has passed over a tube (but only the central one)
		if(Bird.body.getPosition().x > lowerTubes[activeTube].body.getPosition().x + Constants.TUBE_HWIDTH && updateScore)
		{
			SCORE++;
			updateScore = false;
			//Keep track of which tube is the central one
			activeTube = (activeTube == 0) ? 1 : (activeTube == 1) ? 2 : 0;
		}
		//Update the bird's angle
		float angle = (Bird.body.getLinearVelocity().y > 0) ? 30f : -30f;
		Bird.update(angle);
	}

	@Override
	public void render () {
		camera.update();
		Gdx.gl.glClearColor(BG.R, BG.G, BG.B, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		recycleTubes();
		batch.begin();
		BG.draw(batch);
		for(int i = 0; i < 3; ++i)
		{
			lowerTubes[i].draw(batch);
			upperTubes[i].draw(batch);
		}
		Ground.draw(batch);
		Bird.draw(batch);
		batch.end();
		if(!GAME_OVER) {
			world.step(1 / 60f, 6, 2);
			moveScene();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
		Renderer.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if(Bird.body.getPosition().y < (float)Gdx.graphics.getHeight() / 64f)
			Bird.body.setLinearVelocity(0f, Constants.BIRD_VERTICAL_VELOCITY);
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
			if(contact.getFixtureA() == Bird.fixture || contact.getFixtureB() == Bird.fixture)
				GAME_OVER = true;
		}
	}
}
