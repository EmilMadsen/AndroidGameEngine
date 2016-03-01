package dk.kea.class2016february.emilmadsen.helloworld;

public class SimpleGame extends Game
{
    @Override
    public Screen createStartScreen()
    {
        return new SimpleScreen(this);
    }
}
