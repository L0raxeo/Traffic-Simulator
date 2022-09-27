package com.l0raxeo.TrafficSimulator.objects;

import com.l0raxeo.TrafficSimulator.components.Sprite;
import com.l0raxeo.TrafficSimulator.components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs
{

    public static GameObject generateSpriteObject(Sprite sprite, String name, float sizeX, float sizeY)
    {
        GameObject block = new GameObject(name,
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

}
