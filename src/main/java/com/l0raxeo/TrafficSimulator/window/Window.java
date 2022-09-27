package com.l0raxeo.TrafficSimulator.window;

import com.l0raxeo.TrafficSimulator.imgui.ImGuiLayer;
import com.l0raxeo.TrafficSimulator.input.KeyListener;
import com.l0raxeo.TrafficSimulator.input.MouseListener;
import com.l0raxeo.TrafficSimulator.renderer.DebugDraw;
import com.l0raxeo.TrafficSimulator.simulator.Simulator;
import com.l0raxeo.TrafficSimulator.utils.Settings;
import org.joml.Math;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{

    private static Window window = null;

    private long audioContext;
    private long audioDevice;

    private final String title;
    private float width, height;

    private long glfwWindow;
    private ImGuiLayer imguiLayer;
    private static Vector4f backdrop;
    private static Simulator simulator = null;

    private Window()
    {
        this.title = "Traffic-Simulator";
        this.width = 1280;
        this.height = 720;

        backdrop = new Vector4f(1, 1, 1, 1);
    }

    /**
     * Changes scenes according to scene class in params.
     * The class was used in order to allow the user to
     * attempt to change scenes without having to create
     * the scene within the parameters of this method.
     */
    public static Simulator getSimulator()
    {
        if (simulator == null)
        {
            simulator = new Simulator();
            simulator.loadProperties();
            simulator.init();
            simulator.start();
        }

        return Window.simulator;
    }

    public static Window get()
    {
        if (Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    public void run()
    {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Destroy audio context
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow((int) this.width, (int) this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        Settings.SWAP_INTERVAL = 1;
        glfwSwapInterval(Settings.SWAP_INTERVAL);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // Initialize the audio device
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        // Audio Context
        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        assert alCapabilities.OpenAL10 : "Audio Library not supported";

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        this.imguiLayer = new ImGuiLayer(glfwWindow);
        this.imguiLayer.initImGui();

        Window.getSimulator();
    }

    public void loop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        float timer = beginTime + 1;

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            DebugDraw.beginFrame();

            glClearColor(backdrop.x, backdrop.y, backdrop.z, backdrop.w);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                simulator.update(dt);
                simulator.render();
                DebugDraw.draw();

                if (beginTime >= timer)
                {
                    Settings.FPS = Math.round(1 / dt);
                    timer = beginTime + 1;
                }
            }

            this.imguiLayer.update(dt, simulator);

            KeyListener.endFrame();
            MouseListener.endFrame();
            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public static float getWidth() {
        return get().width;
    }

    public static float getHeight() {
        return get().height;
    }

    public static void setWidth(float newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(float newHeight) {
        get().height = newHeight;
    }

    public static void setBackdrop(Vector4f color)
    {
        backdrop = color;
    }

}
