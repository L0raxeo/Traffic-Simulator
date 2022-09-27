package com.l0raxeo.TrafficSimulator.input;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener
{

    private static KeyListener instance;
    private final boolean[] keyPressed = new boolean[350];
    private final boolean[] keyBeginPress = new boolean[350];

    private KeyListener() {}

    public static void endFrame() {
        Arrays.fill(get().keyBeginPress, false);
    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
            get().keyBeginPress[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
            get().keyBeginPress[key] = false;
        }
    }

    /**
     * Being Pressed
     * Key Code reference for GLFW
     * <a href="https://www.glfw.org/docs/3.3/group__keys.html">...</a>
     */
    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }

    /**
     * On press
     * Key Code reference for GLFW
     * <a href="https://www.glfw.org/docs/3.3/group__keys.html">...</a>
     */
    public static boolean keyBeginPress(int keyCode) {
        return get().keyBeginPress[keyCode];
    }

}
