package com.busedc.flybirdfly;

public class Constants {
    public static final float WORLD_GRAVITY = -10f;
    public static final float PPM = 35f;
    public static final float GROUND_OFFSET = 35f;
    public static final float STARS_OFFSET = 15f;
    public static final float GROUND_TOP_VELOCITY = 8f;
    public static final float BIRD_VERTICAL_VELOCITY = 8.5f;
    public static final float BIRD_STARTING_Y = 8.5f;
    public static final float BIRD_SPRITE_SCALE = 4.5f;
    public static final float TUBE_HWIDTH = 2.5f;
    public static final float TUBE_HHEIGHT = 20f;
    public static final float TUBE_VELOCITY = -6.5f;
    public static final float TUBE_X_DIST = 25f;
    public static final float TUBE_Y_DIST = 7.5f;
    public static final float INITIAL_TUBE_OFFSET = 50f;
    public static final int NIGHT_START_HOUR = 18;
    public static final float RND_MAX_TUBE_HEIGHT = -20f;
    public static final float RND_MIN_TUBE_HEIGHT = 0f;
    public static final float TUBE_X_SCALE_CORRECTION = 1.05f;
    public static final short CATEGORY_BIRD = 0x0001;
    public static final short CATEGORY_TUBE = 0x0002;
    public static final short CATEGORY_GROUND = 0x0004;
    public static final short MASK_BIRD = CATEGORY_GROUND | CATEGORY_TUBE;
    public static final short MASK_DEAD_BIRD = CATEGORY_GROUND;
    public static final short MASK_TUBE = CATEGORY_BIRD;
    public static final short MASK_GROUND = CATEGORY_BIRD;
}