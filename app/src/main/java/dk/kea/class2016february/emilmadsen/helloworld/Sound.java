package dk.kea.class2016february.emilmadsen.helloworld;

import android.media.SoundPool;

public class Sound
{
    int soundId;
    SoundPool soundPool;

    public Sound(SoundPool s, int sId)
    {
        soundId = sId;
        soundPool = s;
    }

    public void play(float volume)
    {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    public void dispose()
    {
        soundPool.unload(soundId);
    }
}
