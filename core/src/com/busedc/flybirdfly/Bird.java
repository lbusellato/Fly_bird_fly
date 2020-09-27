package com.busedc.flybirdfly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Bird {
    public Sprite sprite;
    public Body body;
    public Vector2 position;
    public Fixture fixture;
    public float rotation;

    public Bird(World world, Vector2 position, String texturePath, float bodyRadius)
    {
        //Load the sprite
        this.sprite = new Sprite(new Texture(texturePath));
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
        this.body.createFixture(fixtureDef);
        circle.dispose();
        //Get the fixture id for collision checks
        Array<Fixture> fixtures = this.body.getFixtureList();
        this.fixture = fixtures.first();
        //Set up vector position
        this.position = new Vector2();
        this.rotation = 0f;
        this.body.setTransform(this.body.getPosition(), this.rotation);
    }

    public void update()
    {
        this.sprite.setPosition(this.position.x, this.position.y);
        this.sprite.setRotation(this.rotation);
        this.body.setTransform(this.body.getPosition(), this.rotation);
    }

    public void update(Vector2 newPos)
    {
        this.position = newPos;
        update();
    }

    public void update(float x, float y)
    {
        this.position = new Vector2(x, y);
        update();
    }

    public void update(float rot)
    {
        this.rotation = rot;
        update();
    }

    public void draw(SpriteBatch batch)
    {
        this.sprite.draw(batch);
    }
}
