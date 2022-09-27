package com.l0raxeo.TrafficSimulator.components;

import com.l0raxeo.TrafficSimulator.objects.Transform;
import com.l0raxeo.TrafficSimulator.renderer.Texture;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{

    private final Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;

    @Override
    public void start()
    {
        this.lastTransform = gameObject.transform.copy();
    }

    public void update(float dt)
    {
        if (!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.copy(this.lastTransform);
            setDirty();
        }
    }

    @Override
    public void imgui()
    {
        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorPicker4("Color Picker: ", imColor))
        {
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            this.setDirty();
        }
    }

    public Vector4f getColor()
    {
        return this.color;
    }

    public Texture getTexture()
    {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords()
    {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite)
    {
        this.sprite = sprite;
        this.setDirty();
    }

    public void setColor(Vector4f color)
    {
        if (!this.color.equals(color))
        {
            this.setDirty();
            this.color.set(color);
        }
    }

    public void setColor(Vector3f color)
    {
        // converts vector 3f to 4f to match class color obj
        Vector4f tmpConversion = new Vector4f(color.x, color.y, color.z, 1);

        if (!this.color.equals(tmpConversion))
        {
            this.setDirty();
            this.color.set(tmpConversion);
        }
    }

    public boolean isDirty()
    {
        return this.isDirty;
    }

    /**
     * Sets isDirty boolean to true, queuing it to be re-rendered.
     */
    public void setDirty()
    {
        this.isDirty = true;
    }

    /**
     * Sets isDirty boolean to false.
     */
    public void setClean()
    {
        this.isDirty = false;
    }

    public void setTexture(Texture texture)
    {
        this.sprite.setTexture(texture);
    }

}
