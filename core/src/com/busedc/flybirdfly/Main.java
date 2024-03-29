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

public class Main extends ApplicationAdapter implements  GestureDetector.GestureListener {
	public static SpriteBatch batch;
	private static World world;
	private Box2DDebugRenderer Renderer;
	private Camera camera;

	public static Preferences prefs;
	public static Bird Bird;
	private static GameUI GameUI;
	private static Ground Ground;
	public static BG BG;
	public static Score Score;
	private Sprite starting;
	private static Tube[] lowerTubes;
	private static Tube[] upperTubes;
	public static SoundEngine Sound;
	public static TitleScreen titleScreen;
	private static float[] tubeHeights = {-20f, -20f, -20f};

	private static int width;
	private static int height;
	public static int activeTube = 0;

	public static int highscore;

	public static boolean TITLE = true;
	public static boolean GAME_OVER = false;
	public static boolean DEAD = false;
	public static boolean WAITING = true;
	public static boolean updateScore = false;

	private static Random r = new Random();

	public Main() {
	}

	@Override
	public void create() {
		Box2D.init();
		prefs = Gdx.app.getPreferences("PreferenceName");// We store the value 10 with the key of "highScore"
		// Provide default high score of 0
		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}
		// Provide default mute button state
		if (!prefs.contains("mute")) {
			prefs.putBoolean("mute", false);
		}
		prefs.flush(); // This saves the preferences file.
		highscore = prefs.getInteger("highScore");
		batch = new SpriteBatch();

		world = new World(new Vector2(0, Constants.WORLD_GRAVITY), true);

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(
				 width / Constants.PPM,
				height / Constants.PPM);
		Renderer = new Box2DDebugRenderer();

		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);

		world.setContactListener(new ListenerClass());


		Sound = new SoundEngine();
		Sound.MUTE = prefs.getBoolean("mute");
		GameUI = new GameUI(width, height);
		Bird = new Bird(world, new Vector2(0, Constants.BIRD_STARTING_Y), "bird.png", 1.5f);
		Bird.body.setGravityScale(0f);
		Ground = new Ground(world,
				new Vector2(0, Constants.GROUND_OFFSET / Constants.PPM - height / (2 * Constants.PPM)),
				new Vector2(camera.viewportWidth, height / (4f * Constants.PPM)),
				new Vector2(width, Constants.GROUND_OFFSET + height / 4f),
				"bg/ground.png");
		BG = new BG(width, height);
		Score = new Score(width, height);
		starting = new Sprite(new Texture("starting/getready.png"));
		starting.setPosition((float)width / 2 - starting.getWidth() / 2, (float)height / 2 + starting.getHeight() );
		starting.setScale(Constants.BIRD_SPRITE_SCALE);
		randomizeTubeHeights();

		lowerTubes = new Tube[3];
		for (int i = 0; i < 3; ++i) {
			lowerTubes[i] = new Tube(world, tubeHeights[i], i, 0, "bg/tube.png", width, height);
		}

		upperTubes = new Tube[3];
		for (int i = 0; i < 3; ++i) {
			upperTubes[i] = new Tube(world, tubeHeights[i], i, 1, "bg/tube.png", width, height);
		}

		titleScreen = new TitleScreen(width, height);
		Bird.animate();
	}

	public static void resetObjects()
	{
		TITLE = true;
		WAITING = true;
		GAME_OVER = false;
		DEAD = false;
		activeTube = 0;
		updateScore = false;
		batch.setShader(null);
		Score.score = -1;
		Score.update();
		Bird.body.setTransform(new Vector2(0, Constants.BIRD_STARTING_Y), 0f);
		Bird.body.setGravityScale(0.0f);
		Bird.body.setLinearVelocity(0,0);
		Bird.STOPPED = false;
		Bird.previousCol = -1;
		Bird.animate();
		Bird.revive();
		randomizeTubeHeights();
		for(int i = 0; i < 3; ++i)
		{
			lowerTubes[i].body.setTransform(new Vector2(Constants.INITIAL_TUBE_OFFSET + Constants.TUBE_X_DIST * i,
					tubeHeights[i]), 0f);
			upperTubes[i].body.setTransform(new Vector2(Constants.INITIAL_TUBE_OFFSET + Constants.TUBE_X_DIST * i,
					tubeHeights[i] + (Constants.TUBE_Y_DIST + 2 * Constants.TUBE_HHEIGHT)), 0f);
		}
		moveScene();
	}

	public static void randomizeTubeHeights() {
		tubeHeights[0] = Constants.RND_MIN_TUBE_HEIGHT + r.nextFloat() * (Constants.RND_MAX_TUBE_HEIGHT - Constants.RND_MIN_TUBE_HEIGHT);
		tubeHeights[1] = Constants.RND_MIN_TUBE_HEIGHT + r.nextFloat() * (Constants.RND_MAX_TUBE_HEIGHT - Constants.RND_MIN_TUBE_HEIGHT);
		tubeHeights[2] = Constants.RND_MIN_TUBE_HEIGHT + r.nextFloat() * (Constants.RND_MAX_TUBE_HEIGHT - Constants.RND_MIN_TUBE_HEIGHT);
	}

	public void randomizeTubeHeights(int i) {
		tubeHeights[i] = Constants.RND_MIN_TUBE_HEIGHT + r.nextFloat() * (Constants.RND_MAX_TUBE_HEIGHT - Constants.RND_MIN_TUBE_HEIGHT);
	}

	public void recycleTubes() {
		//Reset the tubes positions once they travel through the screen
		for (int i = 0; i < 3; ++i) {
			if (lowerTubes[i].body.getPosition().x < -width / Constants.PPM) {
				randomizeTubeHeights(i);
				if (i == 0) {
					upperTubes[i].body.setTransform(lowerTubes[2].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i] + 2 * Constants.TUBE_HHEIGHT + Constants.TUBE_Y_DIST, 0);
					lowerTubes[i].body.setTransform(lowerTubes[2].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i], 0);
				} else if (i == 1) {
					upperTubes[i].body.setTransform(lowerTubes[0].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i] + 2 * Constants.TUBE_HHEIGHT + Constants.TUBE_Y_DIST, 0);
					lowerTubes[i].body.setTransform(lowerTubes[0].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i], 0);
				} else {
					upperTubes[i].body.setTransform(lowerTubes[1].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i] + 2 * Constants.TUBE_HHEIGHT + Constants.TUBE_Y_DIST, 0);
					lowerTubes[i].body.setTransform(lowerTubes[1].body.getPosition().x + Constants.TUBE_X_DIST, tubeHeights[i], 0);
				}
				break;
			} else if (lowerTubes[i].body.getPosition().x + Constants.TUBE_HWIDTH < 0) {
				updateScore = true;
			}
		}
	}

	public static void moveScene()
	{
		if (!GAME_OVER) {
			Ground.update();
			BG.update();
			for (int i = 0; i < 3; ++i) {
				lowerTubes[i].update();
				upperTubes[i].update();
			}
		}
		//Update the bird sprite position and angle
		Bird.update((width - 34f * 4f) / 2,
				width - 1.5f * Constants.PPM + Bird.body.getPosition().y * Constants.PPM);
		//While we're at it update the score if the bird has passed over a tube (but only the central one)
		if (Bird.body.getPosition().x > lowerTubes[activeTube].body.getPosition().x + Constants.TUBE_HWIDTH && updateScore) {
			Score.update();
			Sound.play(SoundEngine.SFX.SCORE);
			updateScore = false;
			//Keep track of which tube is the central one
			activeTube = (activeTube == 0) ? 1 : (activeTube == 1) ? 2 : 0;
		}
		//Update the bird's angle
		float angle = (Bird.body.getLinearVelocity().y > 5) ? 30f : (Bird.body.getLinearVelocity().y < -5) ? -30f : 0f;
		Bird.update(angle);
	}


	@Override
	public void render () {
		camera.update();
		Gdx.gl.glClearColor(BG.R, BG.G, BG.B, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Renderer.render(world, camera.combined);
		recycleTubes();
		batch.begin();
		BG.draw(batch);
		if (!TITLE)
			for (int i = 0; i < 3; ++i) {
				lowerTubes[i].draw(batch);
				upperTubes[i].draw(batch);
			}
		Ground.draw(batch);
		if (!GameUI.PAUSE && !WAITING && !DEAD && !TITLE)
			Score.draw(batch);
		Bird.draw(batch);
		if (WAITING && !TITLE)
			starting.draw(batch);
		if (DEAD)
			batch.setShader(null);
		if (!WAITING && !TITLE)
			GameUI.draw(batch);
		if (DEAD)
			batch.setShader(GrayscaleShader.grayscaleShader);
		if(TITLE)
			titleScreen.draw(batch);
		batch.end();
		if(!GameUI.PAUSE && !DEAD) {
			world.step(1 / 60f, 6, 2);
			moveScene();
		}
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
		if(!GAME_OVER && !TITLE) {
			if(WAITING)
			{
				WAITING = false;
				Bird.animate();
				Bird.body.setGravityScale(1.0f);
			}
			GameUI.handleInput(x, y);
			if (!GameUI.PAUSE && !GameUI.RESUMED) {
				if (Bird.body.getPosition().y < (float) Gdx.graphics.getHeight() / 64f) {
					Sound.play(SoundEngine.SFX.FLAP);
					Bird.body.setLinearVelocity(0f, Constants.BIRD_VERTICAL_VELOCITY);
				}
			}
			GameUI.RESUMED = false;
		}
		else if(TITLE)
		{
			titleScreen.handleInput(x, y);
		}
		else if (DEAD)
		{
			GameUI.handleInput(x, y);
		}
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
			if(!DEAD && contact.getFixtureA() == Bird.fixture || contact.getFixtureB() == Bird.fixture) {
				Bird.kill();
				if(contact.getFixtureB() == Ground.fixture || contact.getFixtureA() == Ground.fixture) {
					Sound.play(SoundEngine.SFX.DEATH);
					DEAD = true;
					GameUI.updateScore();
				}
				if(!GAME_OVER)
				{
					GAME_OVER = true;
				}
				Bird.STOPPED = true;
				for (int i = 0; i < 3; ++i) {
					lowerTubes[i].body.setLinearVelocity(0, 0);
					upperTubes[i].body.setLinearVelocity(0, 0);
				}
				batch.setShader(GrayscaleShader.grayscaleShader);
			}
		}
	}
}
