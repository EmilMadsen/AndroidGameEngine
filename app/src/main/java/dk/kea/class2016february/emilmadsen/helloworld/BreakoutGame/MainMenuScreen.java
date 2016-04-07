package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

import android.graphics.Bitmap;

import dk.kea.class2016february.emilmadsen.helloworld.Game;
import dk.kea.class2016february.emilmadsen.helloworld.Screen;

public class MainMenuScreen extends Screen
{
    Bitmap mainmenu, insertCoin;
    float passedTime = 0;
    long startTime = System.nanoTime();

    public MainMenuScreen(Game game)
    {
        super(game);
        mainmenu = game.loadBitmap("mainmenu.png");
        insertCoin = game.loadBitmap("insertcoin.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if(game.isTouchDown(0))
        {
            game.setScreen(new GameScreen(game));
            return;
        }
        passedTime = passedTime + deltaTime;
        game.drawBitmap(mainmenu, 0, 0);
        if((passedTime - (int)passedTime) > 0.5f)
        {
            game.drawBitmap(insertCoin, 160 - (insertCoin.getWidth()/2), 320);
        }
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }
}
