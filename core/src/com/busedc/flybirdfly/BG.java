package com.busedc.flybirdfly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;

public class BG {
    public float R;
    public float G;
    public float B;
    private int width;
    private int height;
    private float bgX;
    private float starsX;
    public boolean isNight;
    private Texture sky;
    private Texture bg;
    private Texture stars;

    public BG(int width, int height)
    {
        this.width = width;
        this.height = height;
        //Checking what hour it is to switch theme to night/day theme
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        this.isNight = (hour >= Constants.NIGHT_START_HOUR);
        this.bg = (isNight) ? new Texture("bg/bg_dark.png") : new Texture("bg/bg_light.png");
        this.bg.setWrap(Repeat, Repeat);
        this.stars = (isNight) ? new Texture("bg/stars.png") : new Texture("bg/stars_day.png");
        this.stars.setWrap(Repeat, Repeat);
        this.sky = (isNight) ? new Texture("bg/night_sky.png") : new Texture("bg/day_sky.png");
        this.sky.setWrap(Repeat, Repeat);
    }

    public void update()
    {
        //Move the bg
        this.bgX += 0.25f;
        this.starsX += 0.075f;
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(sky, 0, 0, 0, 0, this.width, this.height);
        batch.draw(bg, 0, (float)height / 4, (int) bgX, 0, this.width, bg.getHeight());
        if(isNight)
            batch.draw(stars, 0, - Constants.STARS_OFFSET + 2 * (float)height / 3, (int) starsX, 0, this.width, stars.getHeight());
    }
}
