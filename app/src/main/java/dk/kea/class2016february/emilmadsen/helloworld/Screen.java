package dk.kea.class2016february.emilmadsen.helloworld;

public abstract class Screen
{
    protected final Game game;

    public Screen(Game game)
    {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();

}
