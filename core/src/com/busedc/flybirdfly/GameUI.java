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
    }

    public void handleInput(float x, float y)
    {
        if(!PAUSE) {
            if (x > pause.getOriginX() && x < pause.getWidth() * 2 + pause.getX() + pause.getOriginX()) {
                if(y < (height - pause.getY()) * 2f && y > height - pause.getY() - pause.getHeight() * 2 - pause.getOriginY())
                {
                    pauseMenu.setPosition((float) width / 2 - pauseMenu.getWidth() / 2, (float) height / 2);
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
        if(PAUSE)
            Game.batch.setShader(GrayscaleShader.grayscaleShader);
    }
}
