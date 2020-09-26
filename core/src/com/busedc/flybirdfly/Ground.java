package com.busedc.flybirdfly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;

public class Ground {
    public Sprite fill;
    public Texture top;
    public Body body;
    public int topX = 0;
    public Vector2 topPosition;

    public Ground(World world, Vector2 position, Vector2 boxDim, Vector2 bbOrigin, Vector2 bbFinal, String fillTexturePath, String topTexturePath)
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
        this.fill = new Sprite(new Texture(fillTexturePath));
        this.fill.setBounds(bbOrigin.x, bbOrigin.y, bbFinal.x, bbFinal.y);
        this.top = new Texture(topTexturePath);
        this.top.setWrap(Repeat, Repeat);
        this.topPosition = new Vector2();
        this.topPosition.x = 0f;
        this.topPosition.y = bbFinal.y;
    }

    public void update()
    {
        topX += Constants.GROUND_TOP_VELOCITY;
    }

    public void draw(SpriteBatch batch)
    {
        this.fill.draw(batch);
        batch.draw(top, topPosition.x, topPosition.y - top.getHeight(), topX, 0, (int)fill.getWidth(), top.getHeight());
    }
}
