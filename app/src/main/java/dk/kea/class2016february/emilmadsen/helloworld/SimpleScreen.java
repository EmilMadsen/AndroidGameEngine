package dk.kea.class2016february.emilmadsen.helloworld;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.Random;

public class SimpleScreen extends Screen
{
    Bitmap bob;
    float x = 0;
    float y = 0;
    Random rand = new Random();
    int clearColor = Color.rgb(240,79,45);
    Sound sound;
    Music music;
    boolean userWantsMusic = false;

    public SimpleScreen(Game game)
    {
        super(game);
        bob = game.loadBitmap("bob.png");
        sound = game.loadSound("explosion.ogg");
        music = game.loadMusic("music.ogg");
    }

    @Override
    public void update(float deltaTime)
    {
        game.clearFramebuffer(clearColor);

        x = x + 100 * deltaTime;
        y = y + 50 * deltaTime;
        if(x > game.getVirtualScreenWidth()) x = -128;
        if(y > game.getVirtualScreenHeight()) y = -128;
        game.drawBitmap(bob, (int)x, (int)y);

        Log.d("Framerate","fps: " + game.getFrameRate() + "=============");

        if(game.isTouchDown(0))
        {
            if(userWantsMusic)
            {
                music.pause();
                userWantsMusic = false;
            }
            else
            {
                music.play();
                userWantsMusic = true;
            }
        }


//        for(int pointer = 0; pointer < 5; pointer++)
//        {
//            if(game.isTouchDown(pointer))
//            {
//                game.drawBitmap(bob, game.getTouchX(pointer),game.getTouchY(pointer));
//                sound.play(1);
//            }
//        }

//        float x = -game.getAccelerometer()[0];
//        float y = game.getAccelerometer()[1];
//        x = (x/10) * game.getVirtualScreenWidth()/2 + game.getVirtualScreenWidth()/2;
//        y = (y/10) * game.getVirtualScreenHeight()/2 + game.getVirtualScreenHeight()/2;
//        game.drawBitmap(bob, (int)x-64, (int)y-64);
    }

    @Override
    public void pause()
    {
        music.pause();
        Log.d("SimpleScreen","we are pausing");
    }

    @Override
    public void resume()
    {
        if(userWantsMusic) music.play();
        Log.d("SimpleScreen","we are resuming");
    }

    @Override
    public void dispose()
    {
        Log.d("SimpleScreen","we are disposing");
        sound.dispose();
    }
}
