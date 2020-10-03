package com.busedc.flybirdfly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TitleScreen {
    private Sprite title;
    private Sprite start;
    private Sprite rate;
    private Sprite ads;

    private int width;
    private int height;

    public TitleScreen(int width, int height)
    {
        this.width = width;
        this.height = height;
        title = new Sprite(new Texture("title/title.png"));
        start = new Sprite(new Texture("title/begin.png"));
        rate = new Sprite(new Texture("title/rate_btn.png"));
        ads = new Sprite(new Texture("title/ads_btn.png"));
        title.setScale(5f);
        start.setScale(4f);
        rate.setScale(4f);
        ads.setScale(4f);
        title.setPosition((float)width / 2 - title.getWidth() / 2, 6 * (float)height / 7 );
        start.setPosition((float)width / 2 - start.getWidth() / 2, 3 * (float)height / 7);
        rate.setPosition((float)width / 2 - 3 * rate.getWidth(), 3 * (float)height / 7 - 3 * start.getHeight());
        ads.setPosition((float)width / 2 + 2 * rate.getWidth(), 3 * (float)height / 7 - 3 * start.getHeight());
    }

    public void handleInput(float x, float y)
    {
        if(x > start.getX() - start.getWidth() - ((float)width / 2 - start.getX()) && x < start.getX() + 2 * start.getWidth() + ((float)width / 2 - start.getX())
        && y > start.getY() + 2.75 * start.getHeight() && y < start.getY() + 6 * start.getHeight())
        {
            Main.TITLE = false;
            Main.Bird.deanimate();
        }
    }

    public void draw(SpriteBatch batch)
    {
        title.draw(batch);
        start.draw(batch);
        rate.draw(batch);
        ads.draw(batch);
    }
}
