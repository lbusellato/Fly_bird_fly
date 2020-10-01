package com.busedc.flybirdfly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.ClampToEdge;
import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;

public class Ground {
    public Sprite fill;
    public Texture texture;
    public Body body;
    public int textureX = 0;
    public Vector2 dim;
    public Fixture fixture;
    public Ground(World world, Vector2 position, Vector2 boxDim, Vector2 dim, String texturePath)
    {
        //Ground body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        this.body = world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox(boxDim.x, boxDim.y);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.groupIndex = -1;
        this.body.createFixture(fixtureDef);
        box.dispose();
        this.texture = new Texture(texturePath);
        this.texture.setWrap(Repeat, ClampToEdge);
        this.dim = dim;
        //Get the fixture id for collision checks
        Array<Fixture> fixtures = this.body.getFixtureList();
        this.fixture = fixtures.first();
    }

    public void update()
    {
        textureX += Constants.GROUND_TOP_VELOCITY;
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(texture, 0, 0, textureX, 0, (int)dim.x, (int)dim.y);
    }
}
