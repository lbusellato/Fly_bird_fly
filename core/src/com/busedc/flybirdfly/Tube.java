package com.busedc.flybirdfly;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Tube {
    public Body body;

    public Tube(World world, float tubeHeight, int index, int upper)
    {
        //Tube body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                Constants.INITIAL_TUBE_OFFSET + Constants.TUBE_X_DIST * index,
                tubeHeight + upper * (Constants.TUBE_Y_DIST + 2 * Constants.TUBE_HHEIGHT));
        bodyDef.gravityScale = 0f;
        body = world.createBody(bodyDef);
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(Constants.TUBE_HWIDTH, Constants.TUBE_HHEIGHT);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rect;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.groupIndex = -1;
        body.createFixture(fixtureDef);
        rect.dispose();
        body.setLinearVelocity(Constants.TUBE_VELOCITY, 0f);
    }
}
