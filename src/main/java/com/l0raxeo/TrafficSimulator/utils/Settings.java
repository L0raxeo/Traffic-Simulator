package com.l0raxeo.TrafficSimulator.utils;

public class Settings
{

    public static int GRID_WIDTH = 8, GRID_HEIGHT = 8;
    public static int FPS = 0;
    public static int SWAP_INTERVAL = 0;

    public static boolean vsync()
    {
        return SWAP_INTERVAL == 1;
    }

}
