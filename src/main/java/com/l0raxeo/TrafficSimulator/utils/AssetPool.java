package com.l0raxeo.TrafficSimulator.utils;

import com.l0raxeo.TrafficSimulator.audio.Sound;
import com.l0raxeo.TrafficSimulator.components.SpriteSheet;
import com.l0raxeo.TrafficSimulator.renderer.Shader;
import com.l0raxeo.TrafficSimulator.renderer.Texture;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AssetPool
{

    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    public static Shader getShader(String resourceName)
    {
        File file = new File(resourceName);

        if (AssetPool.shaders.containsKey(file.getAbsolutePath())) return shaders.get(file.getAbsolutePath());
        else
        {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName)
    {
        File file = new File(resourceName);

        if (AssetPool.textures.containsKey(file.getAbsolutePath()))
            return AssetPool.textures.get(file.getAbsolutePath());
        else
        {
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet)
    {
        File file = new File(resourceName);

        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
    }

    public static SpriteSheet getSpriteSheet(String resourceName)
    {
        File file = new File(resourceName);

        assert AssetPool.spriteSheets.containsKey(file.getAbsolutePath()) : "Error: Tried to access sprite sheet '" + resourceName + "' and it has not been added to asset pool.";

        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(), null);
    }

    public static Collection<Sound> getAllSounds()
    {
        return sounds.values();
    }

    public static void clearAllSounds()
    {
        for (Sound s : getAllSounds())
        {
            if (!s.isPlaying())
                s.stop();
        }
    }

    public static Sound getSound(String soundFile)
    {
        File file = new File(soundFile);

        if (sounds.containsKey(file.getAbsolutePath()))
            return sounds.get(file.getAbsolutePath());
        else assert false : "Sound file not added '" + soundFile + "'";

        return null;
    }

    public static Sound addSound(String soundFile, boolean loops)
    {
        File file = new File(soundFile);

        if (sounds.containsKey(file.getAbsolutePath()))
            return sounds.get(file.getAbsolutePath());
        else
        {
            Sound sound = new Sound(file.getAbsolutePath(), loops);
            AssetPool.sounds.put(file.getAbsolutePath(), sound);

            return sound;
        }
    }

}
