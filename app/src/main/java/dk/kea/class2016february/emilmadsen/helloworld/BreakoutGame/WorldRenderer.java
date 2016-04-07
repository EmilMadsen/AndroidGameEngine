package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

import android.graphics.Bitmap;

import dk.kea.class2016february.emilmadsen.helloworld.Game;

public class WorldRenderer
{
    Game game;
    World world;
    Bitmap ballImage;

    public WorldRenderer(Game game, World world)
    {
        this.game = game;
        this.world = world;
        this.ballImage = game.loadBitmap("ball.png");
    }

    public void render()
    {
        game.drawBitmap(ballImage, (int)world.ball.x, (int)world.ball.y);
    }
}
