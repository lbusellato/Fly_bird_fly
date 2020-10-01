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

public class Score {
    public Sprite h;
    public Sprite d;
    public Sprite u;
    public int width;
    public int height;
    public int score = 0;

    public Score(int width, int height)
    {
        u = new Sprite(new Texture("score_nos/0.png"));
        d = new Sprite(new Texture("score_nos/0.png"));
        h = new Sprite(new Texture("score_nos/0.png"));
        u.setPosition((float)width / 2, 5 * (float)height / 6);
        d.setPosition(-100, -100);
        h.setPosition(-100, -100);
        u.setScale(4.0f);
        d.setScale(4.0f);
        h.setScale(4.0f);
        this.width = width;
        this.height = height;
    }

    public void update()
    {
        score++;
        u.setTexture(new Texture("score_nos/" + score % 10 + ".png"));
        if(score > 9 && score < 100) {
            d.setTexture(new Texture("score_nos/" + ((score / 10) % 10) + ".png"));
            u.setPosition((float)width / 2 + 2 * u.getWidth(), 5 * (float)height / 6);
            d.setPosition((float)width / 2 - 2 * u.getWidth(), 5 * (float)height / 6);
        }
        else if(score > 99) {
            u.setPosition((float)width / 2 + 4 * u.getWidth(), 5 * (float)height / 6);
            d.setPosition((float)width / 2 , 5 * (float)height / 6);
            h.setPosition((float)width / 2 - 4 * u.getWidth(), 5 * (float)height / 6);
            d.setTexture(new Texture("score_nos/" + ((score / 10) % 10) + ".png"));
            h.setTexture(new Texture("score_nos/" + ((score / 100) % 100) + ".png"));
        }
    }

    public void draw(SpriteBatch batch)
    {
        this.u.draw(batch);
        if(score > 9)
            this.d.draw(batch);
        if(score > 99)
            this.h.draw(batch);
    }
}
