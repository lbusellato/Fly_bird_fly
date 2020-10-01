package com.busedc.flybirdfly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundEngine {
    public boolean MUTE = false;
    public enum SFX
    {
        FLAP,
        SCORE,
        DEATH,
    }

    private Music bgm;
    private Sound flap;
    private Sound score;
    private Sound death;


    public SoundEngine()
    {
        flap = Gdx.audio.newSound(Gdx.files.internal("sound/flap.wav"));
        score = Gdx.audio.newSound(Gdx.files.internal("sound/score.wav"));
        death = Gdx.audio.newSound(Gdx.files.internal("sound/dead.wav"));
    }

    public void play(SFX sfx)
    {
        if(!MUTE) {
            switch (sfx) {
                case FLAP:
                    flap.play();
                    break;
                case SCORE:
                    score.play();
                    break;
                case DEATH:
                    death.play();
                    break;
                default:
                    break;
            }
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
