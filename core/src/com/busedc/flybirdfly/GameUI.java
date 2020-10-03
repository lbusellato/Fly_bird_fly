package com.busedc.flybirdfly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameUI {
    public int width;
    public int height;
    public boolean PAUSE = false;
    public boolean RESUMED = false;
    public Sprite pause;
    public Sprite pauseMenu;
    public Sprite h;
    public Sprite d;
    public Sprite u;
    public Sprite h_hi;
    public Sprite d_hi;
    public Sprite u_hi;
    public int highscore = 0;
    public GameUI(int width, int height)
    {
        this.width = width;
        this.height = height;
        pause = new Sprite(new Texture("UI/pause_btn.png"));
        pause.setScale(4.0f);
        pause.setPosition(pause.getWidth() * 2, height - pause.getHeight() * 4);
        pauseMenu = new Sprite(new Texture("UI/unmuted_pause_menu.png"));
        pauseMenu.setScale(4.0f);
        pauseMenu.setPosition(-1000, -1000);
        u = new Sprite(new Texture("score_nos/0.png"));
        d = new Sprite(new Texture("score_nos/0.png"));
        h = new Sprite(new Texture("score_nos/0.png"));
        u_hi = new Sprite(new Texture("score_nos/0.png"));
        d_hi = new Sprite(new Texture("score_nos/0.png"));
        h_hi = new Sprite(new Texture("score_nos/0.png"));
        u.setPosition(-100, -100);
        d.setPosition(-100, -100);
        h.setPosition(-100, -100);
        u_hi.setPosition(-100, -100);
        d_hi.setPosition(-100, -100);
        h_hi.setPosition(-100, -100);
        u.setScale(2.0f);
        d.setScale(2.0f);
        h.setScale(2.0f);
        u_hi.setScale(2.0f);
        d_hi.setScale(2.0f);
        h_hi.setScale(2.0f);
        highscore = Game.highscore;
    }

    public void handleInput(float x, float y)
    {
        if(!PAUSE) {
            if (x > pause.getOriginX() && x < pause.getWidth() * 2 + pause.getX() + pause.getOriginX()) {
                if(y < (height - pause.getY()) * 2f && y > height - pause.getY() - pause.getHeight() * 2 - pause.getOriginY())
                {
                    pauseMenu.setPosition((float) width / 2 - pauseMenu.getWidth() / 2, (float) height / 2);
                    //Display current score
                    u.setTexture(new Texture("score_nos/" + Game.Score.score % 10 + ".png"));
                    u.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth(), pauseMenu.getY() + 2 * u.getHeight());
                    if(Game.Score.score > 9) {
                        d.setTexture(new Texture("score_nos/" + ((Game.Score.score / 10) % 10) + ".png"));
                        d.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 2 * u.getWidth(), pauseMenu.getY() + 2 * u.getHeight());
                    }
                    if(Game.Score.score > 99) {
                        h.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 4 * u.getWidth(), pauseMenu.getY() + 2 * u.getHeight());
                        h.setTexture(new Texture("score_nos/" + ((Game.Score.score / 100) % 100) + ".png"));
                    }

                    //Display highscore
                    u_hi.setTexture(new Texture("score_nos/" + highscore % 10 + ".png"));
                    u_hi.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth(), pauseMenu.getY() - 2 * u.getHeight());
                    if(highscore > 9) {
                        d_hi.setTexture(new Texture("score_nos/" + ((highscore / 10) % 10) + ".png"));
                        d_hi.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 2 * u.getWidth(), pauseMenu.getY() - 2 * u.getHeight());
                    }
                    if(highscore > 99) {
                        h_hi.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 4 * u.getWidth(), pauseMenu.getY() - 2 * u.getHeight());
                        h_hi.setTexture(new Texture("score_nos/" + ((highscore / 100) % 100) + ".png"));
                    }
                    Game.batch.setShader(GrayscaleShader.grayscaleShader);
                    PAUSE = !PAUSE;
                    RESUMED = true;
                }
            }
        }
        else
        {
            if (x < (float)width / 2 - 100f && x > (float)width / 2 - 400f &&
                y < (float)height / 2 - pauseMenu.getHeight() / 2 + 120f && y > (float)height / 2  - pauseMenu.getHeight() / 2)
            {
                pauseMenu.setPosition(-1000, -1000);
                u.setPosition(-100, -100);
                d.setPosition(-100, -100);
                h.setPosition(-100, -100);
                u_hi.setPosition(-100, -100);
                d_hi.setPosition(-100, -100);
                h_hi.setPosition(-100, -100);
                    Game.batch.setShader(null);
                    PAUSE = !PAUSE;
                RESUMED = true;
            }
            else if(x > (float)width / 2 - 54f && x < (float)width / 2 + 62f &&
                    y < (float)height / 2 - pauseMenu.getHeight() / 2 + 120f && y > (float)height / 2  - pauseMenu.getHeight() / 2)
            {
                Game.Sound.MUTE = !Game.Sound.MUTE;
                pauseMenu.setTexture((Game.Sound.MUTE) ?
                        new Texture("UI/muted_pause_menu.png") :
                        new Texture("UI/unmuted_pause_menu.png") );
            }
        }
    }

    public void draw(SpriteBatch batch)
    {
        if(!PAUSE)
            pause.draw(batch);
        if(PAUSE)
            Game.batch.setShader(null);
        pauseMenu.draw(batch);
        this.u.draw(batch);
        if(Game.Score.score > 9)
            this.d.draw(batch);
        if(Game.Score.score > 99)
            this.h.draw(batch);
        this.u_hi.draw(batch);
        if(highscore > 9)
            this.d_hi.draw(batch);
        if(highscore > 99)
            this.h_hi.draw(batch);
        if(PAUSE)
            Game.batch.setShader(GrayscaleShader.grayscaleShader);
    }
}
