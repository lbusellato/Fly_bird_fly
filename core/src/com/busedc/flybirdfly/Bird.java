package com.busedc.flybirdfly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Bird {
    public boolean STOPPED = false;
    public Body body;
    public Vector2 position;
    public Fixture fixture;
    public Animation<TextureRegion> animation;
    public Texture spriteSheet;
    public float rotation;
    // A variable for tracking elapsed time for the animation
    float stateTime;
    public int previousCol = -1;

    public Bird(World world, Vector2 position, String texturePath, float bodyRadius)
    {
        //Bird body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        this.body = world.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(bodyRadius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BIRD;
        fixtureDef.filter.maskBits = Constants.MASK_BIRD;
        this.body.createFixture(fixtureDef);
        circle.dispose();
        //Get the fixture id for collision checks
        Array<Fixture> fixtures = this.body.getFixtureList();
        this.fixture = fixtures.first();
        //Set up vector position
        this.position = new Vector2();
        this.rotation = 0f;
        this.body.setTransform(this.body.getPosition(), this.rotation);

        // Load the sprite sheet as a Texture
        spriteSheet = new Texture(Gdx.files.internal("bird/bw_sprite_sheet.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / 3,
                spriteSheet.getHeight());

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        Array<TextureRegion> frames = new Array<TextureRegion>(3);
        for (int i = 0; i < 3; i++)
            frames.add(tmp[0][i]);

        animation =
                new Animation<TextureRegion>(0.1f, frames, Animation.PlayMode.LOOP_PINGPONG);
        stateTime = 0f;

    }

    public void deanimate()
    {
        // Load the sprite sheet as a Texture
        spriteSheet = new Texture(Gdx.files.internal("bird/bw_sprite_sheet.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / 3,
                spriteSheet.getHeight());

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        Array<TextureRegion> frames = new Array<TextureRegion>(3);
        for (int i = 0; i < 3; i++)
            frames.add(tmp[0][i]);

        animation =
                new Animation<TextureRegion>(0.1f, frames, Animation.PlayMode.LOOP_PINGPONG);
        stateTime = 0f;
    }

    public void animate()
    {
        if(previousCol == -1) {
            Random r = new Random();
            int col = r.nextInt(3) + 1;
            previousCol = col;
            // Load the sprite sheet as a Texture
        }
        switch(previousCol)
        {
            case 1:
            default:
                spriteSheet = new Texture(Gdx.files.internal("bird/y_sprite_sheet.png"));
                break;
            case 2:
                spriteSheet = new Texture(Gdx.files.internal("bird/r_sprite_sheet.png"));
                break;
            case 3:
                spriteSheet = new Texture(Gdx.files.internal("bird/b_sprite_sheet.png"));
                break;
        }

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / 3,
                spriteSheet.getHeight());

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        Array<TextureRegion> frames = new Array<TextureRegion>(3);
        for (int i = 0; i < 3; i++)
            frames.add(tmp[0][i]);

        animation =
                new Animation<TextureRegion>(0.1f, frames, Animation.PlayMode.LOOP_PINGPONG);
        stateTime = 0f;
    }

    public void kill()
    {
        Array<Fixture> fixtures = this.body.getFixtureList();
        Filter filter = fixtures.first().getFilterData();
        filter.maskBits = Constants.MASK_DEAD_BIRD;
        fixtures.first().setFilterData(filter);
    }

    public void revive()
    {
        Array<Fixture> fixtures = this.body.getFixtureList();
        Filter filter = fixtures.first().getFilterData();
        filter.maskBits = Constants.MASK_BIRD;
        fixtures.first().setFilterData(filter);
    }

    public void update()
    {
        this.body.setTransform(this.body.getPosition(), this.rotation);
    }

    public void update(float x, float y)
    {
        this.position = new Vector2(x, y);
    }

    public void update(float rot)
    {
        this.rotation = rot;
    }

    public void draw(SpriteBatch batch)
    {
        update();
        if(!STOPPED)
            stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(
                currentFrame,
                position.x + 3 * (float)currentFrame.getRegionWidth() / 2,
                position.y +  (float)currentFrame.getRegionHeight(),
                (float)currentFrame.getRegionWidth() / 2,
                (float)currentFrame.getRegionHeight() / 2,
                currentFrame.getRegionWidth(),
                currentFrame.getRegionHeight(),
                Constants.BIRD_SPRITE_SCALE,
                Constants.BIRD_SPRITE_SCALE,
                this.rotation);
    }
}
