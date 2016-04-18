package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

public class Block
{
    public static final float WIDTH = 40;
    public static final float HEIGHT = 18;
    int type;
    float x;
    float y;

    public Block(float x, float y, int type)
    {
        this.x = x;
        this.y = y;
        this.type = type;

    }
}
