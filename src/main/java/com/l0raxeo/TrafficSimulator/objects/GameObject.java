package com.l0raxeo.TrafficSimulator.objects;

import com.l0raxeo.TrafficSimulator.components.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{

    private static int ID_COUNTER = 0;
    private int uid = -1;

    private String name = "Default Name";
    private final List<Component> components;
    public Transform transform;
    private boolean doSerialization = true;
    private transient boolean isDead = false;
    private final int zIndex;

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.zIndex = zIndex;
        this.components = new ArrayList<>();
        this.transform = transform;

        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }

        return null;
    }

    public <T extends Component> boolean hasComponent(Class<T> componentClass)
    {
        for (Component c : components)
            if (componentClass.isAssignableFrom(c.getClass()))
                return true;

        return false;
    }

    public List<Component> getAllDebugComponents()
    {
        List<Component> debugComponents = new ArrayList<>();

        for (Component c : components)
            if (c.isDebug())
                debugComponents.add(c);

        return debugComponents;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass)
    {
        for (int i=0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c)
    {
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float dt) {
        for (Component component : components)
        {
            if (!component.isDisabled())
                component.update(dt);
        }
    }

    public void start() {
        for (Component component : components) {
            component.start();
        }
    }

    public void imgui() {
        for (Component c : components) {
            if (!c.isDisabled())
                c.imgui();
        }
    }

    public int zIndex() {
        return this.zIndex;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public static void init(int maxId)
    {
        ID_COUNTER = maxId;
    }

    public int getUid()
    {
        return this.uid;
    }

    public List<Component> getAllComponents()
    {
        return this.components;
    }

    public void setNoSerialize() {
        this.doSerialization = false;
    }

    public boolean doSerialization() {
        return this.doSerialization;
    }

    public boolean isDead()
    {
        return isDead;
    }

    public void die()
    {
        this.isDead = true;
    }

}
