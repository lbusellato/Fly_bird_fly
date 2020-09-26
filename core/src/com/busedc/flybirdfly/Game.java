package com.busedc.flybirdfly;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.awt.geom.RectangularShape;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.ClampToEdge;
import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;

public class Game extends ApplicationAdapter implements  GestureDetector.GestureListener  {

	private SpriteBatch batch;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private Camera camera;
	private Body birdBody;
	private Body tubeBody;
	private Body[] lowerTubes;
	private Body[] upperTubes;
	private Sprite bird;
	private Sprite ground_fill;
	private Texture ground_top;
	private Texture bg;
	private Texture stars;
	private Sprite tube;
	private static Fixture birdFixture;

	private float[] tubeHeights = {-20f, -20f, -20f};
	private float tubeXdist = 20f;
	private float tubeYdist = 10f;

	private float tubeHWidth = 2.5f;
	private float tubeVelocity = -6.5f;
	private float angle = 45.0f;
	private float ground_topX = 0f;
	private float bg_X = 0f;
	private float stars_X = 0f;
	private float bg_col_r;
	private float bg_col_g;
	private float bg_col_b;

	private int width;
	private int height;
	private int activeTube = 0;
	private int SCORE = 0;

	private static boolean GAME_OVER = false;
	private boolean isNight = false;
	private boolean updateScore = false;
	private static boolean DEBUG = false;

	public Game(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

		@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.bird = new Sprite(new Texture("bird.png"));
		this.ground_fill = new Sprite(new Texture("ground_fill.png"));
		ground_fill.setPosition(0f,0f);
		Box2D.init();
		this.world = new World(new Vector2(0, -10), true);
		this.camera = new OrthographicCamera(
				(float)Gdx.graphics.getWidth() / 32f,
				(float)Gdx.graphics.getHeight() / 32f);
		this.debugRenderer = new Box2DDebugRenderer();

		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);

		world.setContactListener(new ListenerClass());

		//Bird body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(0, 0);
		birdBody = world.createBody(bodyDef);
		CircleShape circle = new CircleShape();
		circle.setRadius(1.5f);
		FixtureDef birdFixtureDef = new FixtureDef();
		birdFixtureDef.shape = circle;
		birdFixtureDef.density = 4f;
		birdFixtureDef.friction = 0.0f;
		birdFixtureDef.restitution = 0f;
		Fixture fixture = birdBody.createFixture(birdFixtureDef);
		circle.dispose();

		//Ground body definition
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vector2(0, (20f - (float)Gdx.graphics.getHeight()) / 64f));
		Body groundBody = world.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(camera.viewportWidth, (float)Gdx.graphics.getHeight() / 128f);
		FixtureDef groundFixtureDef = new FixtureDef();
		groundFixtureDef.shape = groundBox;
		groundFixtureDef.density = 1f;
		groundFixtureDef.friction = 0.0f;
		groundFixtureDef.restitution = 0f;
		groundFixtureDef.filter.groupIndex = -1;
		groundBody.createFixture(groundFixtureDef);
		groundBox.dispose();

		randomizeTubeHeights(-1);

		lowerTubes = new Body[3];
		for(int i = 0; i < 3; ++i)
		{
			//Tube body definition
			BodyDef tubeBodyDef = new BodyDef();
			tubeBodyDef.type = BodyDef.BodyType.DynamicBody;
			tubeBodyDef.position.set(30 + tubeXdist * i, tubeHeights[i]);
			tubeBodyDef.gravityScale = 0f;
			tubeBody = world.createBody(tubeBodyDef);
			PolygonShape tubeRect = new PolygonShape();
			tubeRect.setAsBox(tubeHWidth, 20);
			FixtureDef tubeFixtureDef = new FixtureDef();
			tubeFixtureDef.shape = tubeRect;
			tubeFixtureDef.density = 0.0f;
			tubeFixtureDef.friction = 0.0f;
			tubeFixtureDef.restitution = 0f;
			tubeFixtureDef.filter.groupIndex = -1;
			tubeBody.createFixture(tubeFixtureDef);
			tubeRect.dispose();
			tubeBody.setLinearVelocity(tubeVelocity, 0f);
			lowerTubes[i] = tubeBody;
		}

		upperTubes = new Body[3];
		for(int i = 0; i < 3; ++i)
		{
			//Tube body definition
			BodyDef tubeBodyDef = new BodyDef();
			tubeBodyDef.type = BodyDef.BodyType.DynamicBody;
			tubeBodyDef.position.set(30 + tubeXdist * i, tubeHeights[i] + tubeYdist + 40);
			tubeBodyDef.gravityScale = 0f;
			tubeBody = world.createBody(tubeBodyDef);
			PolygonShape tubeRect = new PolygonShape();
			tubeRect.setAsBox(tubeHWidth, 20);
			FixtureDef tubeFixtureDef = new FixtureDef();
			tubeFixtureDef.shape = tubeRect;
			tubeFixtureDef.density = 0.0f;
			tubeFixtureDef.friction = 0.0f;
			tubeFixtureDef.restitution = 0f;
			tubeFixtureDef.filter.groupIndex = -1;
			tubeBody.createFixture(tubeFixtureDef);
			tubeRect.dispose();
			tubeBody.setLinearVelocity(tubeVelocity, 0f);
			upperTubes[i] = tubeBody;
		}

		//Scale the brown texture to fill the floor
		float scaleGroundFill = (float)Gdx.graphics.getHeight() / (3.2f * ground_fill.getHeight());
		ground_fill.setScale(1.0f, scaleGroundFill);

		//Checking what hour it is to switch theme to night/day theme
		Date date = new Date();   // given date
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(date);   // assigns calendar to given date
		int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
		if(hour >= 18)
		{
			bg_col_r = (float)0;
			bg_col_g = (float)(126/255.0);
			bg_col_b = (float)(166/255.0);
			bg = new Texture("bg_dark.png");
			stars = new Texture("stars.png");
			stars.setWrap(Repeat, Repeat);
			isNight = true;
		}
		else
		{
			bg_col_r = (float)(106/255.0);
			bg_col_g = (float)(220/255.0);
			bg_col_b = (float)(220/255.0);
			bg = new Texture("bg_light.png");
			isNight = false;
		}
		bg.setWrap(Repeat, Repeat);
		ground_top = new Texture("ground_top.png");
		ground_top.setWrap(Repeat, Repeat);

		Array<Fixture> birdFixtures = birdBody.getFixtureList();
		birdFixture = birdFixtures.first();

		tube = new Sprite(new Texture("tube.png"));
	}

	public void randomizeTubeHeights(int which)
	{
		float min = -15f;
		float max = -5f;
		Random r = new Random();
		float random = min + r.nextFloat() * (max - min);
		switch(which)
		{
			case 0:
				tubeHeights[0] = min + r.nextFloat() * (max - min);
				break;
			case 1:
				tubeHeights[1] = min + r.nextFloat() * (max - min);
				break;
			case 2:
				tubeHeights[2] = min + r.nextFloat() * (max - min);
				break;
			case -1:
			default:
				tubeHeights[0] = min + r.nextFloat() * (max - min);
				tubeHeights[1] = min + r.nextFloat() * (max - min);
				tubeHeights[2] = min + r.nextFloat() * (max - min);
				break;
		}
	}

	public void recycleTubes()
	{
		//Reset the tubes positions once they travel through the screen
		for(int i = 0; i < 3; ++i)
		{
			if(lowerTubes[i].getPosition().x < -(float)Gdx.graphics.getWidth() / 32f)
			{
				randomizeTubeHeights(i);
				if(i == 0)
				{
					upperTubes[i].setTransform(lowerTubes[2].getPosition().x + tubeXdist, tubeHeights[i] + 40 + tubeYdist, 0);
					lowerTubes[i].setTransform(lowerTubes[2].getPosition().x + tubeXdist, tubeHeights[i], 0);
				}
				else if(i == 1)
				{
					upperTubes[i].setTransform(lowerTubes[0].getPosition().x + tubeXdist, tubeHeights[i] + 40 + tubeYdist, 0);
					lowerTubes[i].setTransform(lowerTubes[0].getPosition().x + tubeXdist, tubeHeights[i], 0);
				}
				else
				{
					upperTubes[i].setTransform(lowerTubes[1].getPosition().x + tubeXdist, tubeHeights[i] + 40 + tubeYdist, 0);
					lowerTubes[i].setTransform(lowerTubes[1].getPosition().x + tubeXdist, tubeHeights[i], 0);
				}
				break;
			}
			else if(lowerTubes[i].getPosition().x + tubeHWidth < 0)
			{
				updateScore = true;
			}
		}
	}

	public void moveScene()
	{
		//Move the bg
		ground_topX += 7.5f;
		bg_X += 0.25f;
		stars_X += 0.075f;
		//Update the bird sprite position and angle
		bird.setPosition(((float)Gdx.graphics.getWidth() - bird.getWidth()) / 2,
				(float)Gdx.graphics.getWidth() - 1.5f * 32f + birdBody.getPosition().y * 32f);
		//While we're at it update the score if the bird has passed over a tube (but only the central one)
		if(birdBody.getPosition().x > lowerTubes[activeTube].getPosition().x + tubeHWidth && updateScore)
		{
			SCORE++;
			updateScore = false;
			activeTube = (activeTube == 0) ? 1 : (activeTube == 1) ? 2 : 0;
			Gdx.app.log("testscore", Integer.toString(SCORE));
		}
		bird.setRotation(angle);
	}

	@Override
	public void render () {
		camera.update();
		Gdx.gl.glClearColor(bg_col_r, bg_col_g, bg_col_b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Update the bird's angle
		angle = (birdBody.getLinearVelocity().y > 0) ? 30f : -30f;
		birdBody.setTransform(birdBody.getPosition(), angle);
		debugRenderer.render(world, camera.combined);
		recycleTubes();
		if(!DEBUG)
		{
			batch.begin();
			batch.draw(bg, 0, 555, (int) bg_X, 0, this.width, 462);
			if(isNight)
				batch.draw(stars, 0, 1400, (int) stars_X, 0, this.width, 753);
			tube.draw(batch);
			//ground_fill.draw(batch);
			//batch.draw(ground_top, 0, 455, (int) ground_topX, 0, this.width, 100);
			bird.draw(batch);
			batch.end();
		}
		if(!GAME_OVER) {
			world.step(1 / 60f, 6, 2);
			moveScene();
			tube.setPosition(lowerTubes[0].getPosition().x, lowerTubes[0].getPosition().y);
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
		debugRenderer.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if(birdBody.getPosition().y < (float)Gdx.graphics.getHeight() / 64f)
			birdBody.setLinearVelocity(0f, 8.5f);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		DEBUG = !DEBUG;
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
			Fixture fixtureA = contact.getFixtureA();
			Fixture fixtureB = contact.getFixtureB();
			if(DEBUG)
				Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
			if(fixtureA == birdFixture || fixtureB == birdFixture)
				GAME_OVER = true;
		}
	}
}
