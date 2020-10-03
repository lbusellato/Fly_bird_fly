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
    public Sprite game_over;
    public int highscore;
    public GameUI(int width, int height)
    {
        this.width = width;
        this.height = height;
        pause = new Sprite(new Texture("UI/pause_btn.png"));
        pause.setScale(4.0f);
        pause.setPosition(pause.getWidth() * 2, height - pause.getHeight() * 4);
        pauseMenu = new Sprite((Main.Sound.MUTE) ?
                new Texture("UI/muted_pause_menu.png") :
                new Texture("UI/unmuted_pause_menu.png"));
        pauseMenu.setScale(4.0f);
        pauseMenu.setPosition(-1000, -1000);
        u = new Sprite(new Texture("score_nos/0.png"));
        d = new Sprite(new Texture("score_nos/0.png"));
        h = new Sprite(new Texture("score_nos/0.png"));
        u_hi = new Sprite(new Texture("score_nos/0.png"));
        d_hi = new Sprite(new Texture("score_nos/0.png"));
        h_hi = new Sprite(new Texture("score_nos/0.png"));
        game_over = new Sprite(new Texture("UI/game_over.png"));
        game_over.setPosition(-1000, -1000);
        game_over.setScale(4.5f);
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
        highscore = Main.highscore;
    }

    public void handleInput(float x, float y)
    {
        if (!PAUSE) {
            if (x > pause.getOriginX() && x < pause.getWidth() * 2 + pause.getX() + pause.getOriginX()) {
                if (y < (height - pause.getY()) * 2f && y > height - pause.getY() - pause.getHeight() * 2 - pause.getOriginY()) {
                    pauseMenu.setPosition((float) width / 2 - pauseMenu.getWidth() / 2, (float) height / 2);
                    //Display current score
                    u.setTexture(new Texture("score_nos/" + Main.Score.score % 10 + ".png"));
                    u.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth(), pauseMenu.getY() + 2 * u.getHeight());
                    if (Main.Score.score > 9) {
                        d.setTexture(new Texture("score_nos/" + ((Main.Score.score / 10) % 10) + ".png"));
                        d.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 2 * u.getWidth(), pauseMenu.getY() + 2 * u.getHeight());
                    }
                    if (Main.Score.score > 99) {
                        h.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 4 * u.getWidth(), pauseMenu.getY() + 2 * u.getHeight());
                        h.setTexture(new Texture("score_nos/" + ((Main.Score.score / 100) % 100) + ".png"));
                    }

                    //Display highscore
                    u_hi.setTexture(new Texture("score_nos/" + highscore % 10 + ".png"));
                    u_hi.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth(), pauseMenu.getY() - 2 * u.getHeight());
                    if (highscore > 9) {
                        d_hi.setTexture(new Texture("score_nos/" + ((highscore / 10) % 10) + ".png"));
                        d_hi.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 2 * u.getWidth(), pauseMenu.getY() - 2 * u.getHeight());
                    }
                    if (highscore > 99) {
                        h_hi.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 4 * u.getWidth(), pauseMenu.getY() - 2 * u.getHeight());
                        h_hi.setTexture(new Texture("score_nos/" + ((highscore / 100) % 100) + ".png"));
                    }
                    Main.batch.setShader(GrayscaleShader.grayscaleShader);
                    PAUSE = !PAUSE;
                    RESUMED = true;
                }
            }
        } else {
            if (x < (float) width / 2 - 100f && x > (float) width / 2 - 400f &&
                    y < (float) height / 2 - pauseMenu.getHeight() / 2 + 120f && y > (float) height / 2 - pauseMenu.getHeight() / 2) {
                pauseMenu.setPosition(-1000, -1000);
                u.setPosition(-100, -100);
                d.setPosition(-100, -100);
                h.setPosition(-100, -100);
                u_hi.setPosition(-100, -100);
                d_hi.setPosition(-100, -100);
                h_hi.setPosition(-100, -100);
                Main.batch.setShader(null);
                PAUSE = !PAUSE;
                RESUMED = true;
            } else if (x > (float) width / 2 - 54f && x < (float) width / 2 + 62f &&
                    y < (float) height / 2 - pauseMenu.getHeight() / 2 + 120f && y > (float) height / 2 - pauseMenu.getHeight() / 2) {
                Main.Sound.MUTE = !Main.Sound.MUTE;
                Main.prefs.putBoolean("mute", Main.Sound.MUTE);
                Main.prefs.flush(); // This saves the preferences file.
                pauseMenu.setTexture((Main.Sound.MUTE) ?
                        new Texture("UI/muted_pause_menu.png") :
                        new Texture("UI/unmuted_pause_menu.png"));
            }
        }
    }

    public void updateScore()
    {
        if(Main.Score.score > Main.prefs.getInteger("highScore"))
        {
            Main.prefs.putInteger("highScore", Main.Score.score);
            Main.prefs.flush();
            Main.highscore = Main.Score.score;
            highscore = Main.Score.score;
            pauseMenu.setTexture(new Texture("UI/hiscore_game_over_menu.png"));
        }
        else
        {
            pauseMenu.setTexture(new Texture("UI/game_over_menu.png"));
        }
    }

    public void draw(SpriteBatch batch)
    {
        if(Main.DEAD)
        {
            game_over.setPosition((float) width / 2 - game_over.getWidth() / 2, 7 * (float) height / 8);
            pauseMenu.setPosition((float) width / 2 - pauseMenu.getWidth() / 2, (float) height / 2);
            //Display current score
            u.setTexture(new Texture("score_nos/" + Main.Score.score % 10 + ".png"));
            u.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth(), pauseMenu.getY() + 2 * u.getHeight());
            if (Main.Score.score > 9) {
                d.setTexture(new Texture("score_nos/" + ((Main.Score.score / 10) % 10) + ".png"));
                d.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 2 * u.getWidth(), pauseMenu.getY() + 2 * u.getHeight());
            }
            if (Main.Score.score > 99) {
                h.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 4 * u.getWidth(), pauseMenu.getY() + 2 * u.getHeight());
                h.setTexture(new Texture("score_nos/" + ((Main.Score.score / 100) % 100) + ".png"));
            }

            //Display highscore
            u_hi.setTexture(new Texture("score_nos/" + highscore % 10 + ".png"));
            u_hi.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth(), pauseMenu.getY() - 2 * u.getHeight());
            if (highscore > 9) {
                d_hi.setTexture(new Texture("score_nos/" + ((highscore / 10) % 10) + ".png"));
                d_hi.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 2 * u.getWidth(), pauseMenu.getY() - 2 * u.getHeight());
            }
            if (highscore > 99) {
                h_hi.setPosition(pauseMenu.getX() + 2 * pauseMenu.getWidth() - 4 * u.getWidth(), pauseMenu.getY() - 2 * u.getHeight());
                h_hi.setTexture(new Texture("score_nos/" + ((highscore / 100) % 100) + ".png"));
            }
        }
        if(!PAUSE && !Main.DEAD)
            pause.draw(batch);
        if(PAUSE)
            Main.batch.setShader(null);
        game_over.draw(batch);
        pauseMenu.draw(batch);
        this.u.draw(batch);
        if(Main.Score.score > 9)
            this.d.draw(batch);
        if(Main.Score.score > 99)
            this.h.draw(batch);
        this.u_hi.draw(batch);
        if(highscore > 9)
            this.d_hi.draw(batch);
        if(highscore > 99)
            this.h_hi.draw(batch);
        if(PAUSE)
            Main.batch.setShader(GrayscaleShader.grayscaleShader);
    }
}
