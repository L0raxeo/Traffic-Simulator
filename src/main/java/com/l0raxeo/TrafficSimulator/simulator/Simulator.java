package com.l0raxeo.TrafficSimulator.simulator;

import com.l0raxeo.TrafficSimulator.objects.GameObject;
import com.l0raxeo.TrafficSimulator.renderer.Camera;
import com.l0raxeo.TrafficSimulator.renderer.Renderer;
import com.l0raxeo.TrafficSimulator.window.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Simulator
{

    protected Renderer renderer = new Renderer();
    private Camera camera;

    protected List<GameObject> gameObjects = new ArrayList<>();
    protected Vector4f backdrop = new Vector4f(0, 0, 0, 0);

    private boolean isRunning = false;
    private boolean isDebugMode = false;

    public Simulator() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public List<GameObject> getGameObjects(String name)
    {
        List<GameObject> gameObjects = new ArrayList<>();

        for (GameObject go : this.gameObjects)
            if (go.getName() != null && go.getName().equals(name))
                gameObjects.add(go);

        return gameObjects;
    }

    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }

    public List<GameObject> getGameObjects(Vector2f coordinates)
    {
        List<GameObject> gos = new ArrayList<>();

        float x = coordinates.x;
        float y = coordinates.y;

        for (GameObject go : gameObjects)
        {
            Vector2f pos = go.transform.position;

            float ax = pos.x;
            float ay = pos.y;
            float bx = pos.x + go.transform.scale.x;
            float by = pos.y + go.transform.scale.y;

            if (x > ax && x < bx && y > ay && y < by)
                gos.add(go);
        }

        return gos;
    }

    public List<GameObject> getGameObjects(float x, float y)
    {
        List<GameObject> gos = new ArrayList<>();

        for (GameObject go : gameObjects)
        {
            Vector2f pos = go.transform.position;

            float ax = pos.x;
            float ay = pos.y;
            float bx = pos.x + go.transform.scale.x;
            float by = pos.y + go.transform.scale.y;

            if (x > ax && x < bx && y > ay && y < by)
                gos.add(go);
        }

        return gos;
    }

    public List<GameObject> getGameObjects()
    {
        return this.gameObjects;
    }

    public void update(float dt)
    {

    }
    public void render()
    {

    }

    public Camera camera() {
        return this.camera;
    }

    public void imgui() {

    }

    public void loadProperties()
    {

    }

    protected void setBackdrop(Vector4f color)
    {
        this.backdrop = color;
        Window.setBackdrop(this.backdrop);
    }

    protected void setBackdrop(float r, float g, float b)
    {
        this.backdrop = new Vector4f(r, g, b, (float) 1);
        Window.setBackdrop(this.backdrop);
    }

    protected void createCamera(Vector2f position)
    {
        this.camera = new Camera(position);
    }

    public void enableDebugMode()
    {
        isDebugMode = true;
    }

    public void disableDebugMode()
    {
        isDebugMode = false;
    }

    public boolean isDebugMode()
    {
        return isDebugMode;
    }

}
