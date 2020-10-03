package com.busedc.flybirdfly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Tube {
    public Sprite sprite;
    public Body body;
    public int width;
    public int height;
    public Fixture fixture;

    public Tube(World world, float tubeHeight, int index, int upper, String texturePath, int width, int height)
    {
        this.width = width;
        this.height = height;
        //Tube body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                Constants.INITIAL_TUBE_OFFSET + Constants.TUBE_X_DIST * index,
                tubeHeight + upper * (Constants.TUBE_Y_DIST + 2 * Constants.TUBE_HHEIGHT));
        bodyDef.gravityScale = 0f;
        this.body = world.createBody(bodyDef);
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(Constants.TUBE_HWIDTH, Constants.TUBE_HHEIGHT);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rect;
        fixtureDef.density = 100.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_TUBE;
        fixtureDef.filter.maskBits = Constants.MASK_TUBE;
        //fixtureDef.filter.groupIndex = -1;
        this.body.createFixture(fixtureDef);
        rect.dispose();
        this.sprite = new Sprite(new Texture(texturePath));
        this.sprite.setFlip(false, upper == 1);
        //Get the fixture id for collision checks
        Array<Fixture> fixtures = this.body.getFixtureList();
        this.fixture = fixtures.first();
        this.update();
    }

    public void update()
    {
        this.body.setLinearVelocity((Game.WAITING) ? 0f : Constants.TUBE_VELOCITY, 0f);
        this.sprite.setPosition(
                (float)width / 2 + this.body.getPosition().x * Constants.PPM - Constants.TUBE_HWIDTH * Constants.PPM,
                (float)height / 2 + this.body.getPosition().y * Constants.PPM - Constants.TUBE_HHEIGHT * Constants.PPM);
        this.sprite.setSize(
                Constants.TUBE_HWIDTH * 2 * Constants.PPM * Constants.TUBE_X_SCALE_CORRECTION,
                Constants.TUBE_HHEIGHT * 2 * Constants.PPM);
    }

    public void draw(SpriteBatch batch)
    {
        this.sprite.draw(batch);
    }
}
