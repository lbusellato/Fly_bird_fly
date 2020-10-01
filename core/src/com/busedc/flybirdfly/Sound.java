package com.busedc.flybirdfly;

import com.badlogic.gdx.audio.Music;

public class Sound {
    public enum SFX
    {
        FLAP,
        SCORE,
        DEATH,
    }

    private Music bgm;
    private Music flap;
    private Music score;
    private Music death;


    public Sound()
    {
        sound = Gdx.audio.newSound(Gdx.files.internal("data/testjava.mp3"));
    }

    public void play(SFX sfx)
    {
        switch(sfx)
        {
            case FLAP:
                break;
            case SCORE:
                break;
            case DEATH:
                break;
            default:
                break;
        }
    }

    public void startBGM()
    {

    }

    public void dispose()
    {
        bgm.dispose();
        flap.dispose();
        score.dispose();
        death.dispose();
    }
}
