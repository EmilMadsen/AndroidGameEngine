package dk.kea.class2016february.emilmadsen.helloworld;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;

import java.util.Random;

public class SimpleScreen extends Screen
{
    Bitmap bitmap;
    int x = 0;
    int y = 0;
    Random rand = new Random();
    int clearColor = Color.MAGENTA;

    public SimpleScreen(Game game)
    {
        super(game);
        bitmap = game.loadBitmap("bob.png");
    }

    @Override
    public void update(float deltaTime)
    {
        game.clearFramebuffer(clearColor);

        for(int pointer = 0; pointer < 5; pointer++)
        {
            if(game.isTouchDown(pointer))
            {
                game.drawBitmap(bitmap, game.getTouchX(pointer),game.getTouchY(pointer));
            }
        }
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
