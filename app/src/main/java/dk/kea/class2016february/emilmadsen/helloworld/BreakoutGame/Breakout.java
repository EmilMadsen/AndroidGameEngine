package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

import dk.kea.class2016february.emilmadsen.helloworld.Game;
import dk.kea.class2016february.emilmadsen.helloworld.Screen;

public class Breakout extends Game
{

    @Override
    public Screen createStartScreen()
    {
        return new MainMenuScreen(this);
    }
}
