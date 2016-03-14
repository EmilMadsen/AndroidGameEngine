package dk.kea.class2016february.emilmadsen.helloworld;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;

import java.util.Random;

public class SimpleScreen extends Screen
{
    Bitmap bob;
    int x = 0;
    int y = 0;
    Random rand = new Random();
    int clearColor = Color.MAGENTA;

    public SimpleScreen(Game game)
    {
        super(game);
        bob = game.loadBitmap("bob.png");
    }

    @Override
    public void update(float deltaTime)
    {
        game.clearFramebuffer(clearColor);


/*
        for(int pointer = 0; pointer < 5; pointer++)
        {
            if(game.isTouchDown(pointer))
            {
                game.drawBitmap(bitmap, game.getTouchX(pointer),game.getTouchY(pointer));
            }
        }
*/
        float x = -game.getAccelerometer()[0];
        float y = game.getAccelerometer()[1];
        x = (x/5) * game.getVirtualScreenWidth()/2 + game.getVirtualScreenWidth()/2;
        y = (y/5) * game.getVirtualScreenHeight()/2 + game.getVirtualScreenHeight()/2;
        game.drawBitmap(bob, (int)x-64, (int)y-64);
    }

    @Override
    public void pause()
    {
        Log.d("SimpleScreen","we are pausing");
    }

    @Override
    public void resume()
    {
        Log.d("SimpleScreen","we are resuming");
    }

    @Override
    public void dispose()
    {
        Log.d("SimpleScreen","we are disposing");
    }
}
