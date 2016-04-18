package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

import android.graphics.Bitmap;

import dk.kea.class2016february.emilmadsen.helloworld.Game;

public class WorldRenderer
{
    Game game;
    World world;
    Bitmap ballImage;
    Bitmap paddleImage;
    Bitmap blocksImage;

    public WorldRenderer(Game game, World world)
    {
        this.game = game;
        this.world = world;
        this.ballImage = game.loadBitmap("ball.png");
        this.paddleImage = game.loadBitmap("paddle.png");
        this.blocksImage = game.loadBitmap("blocks.png");
    }

    public void render()
    {
        game.drawBitmap(ballImage, (int)world.ball.x, (int)world.ball.y);
        game.drawBitmap(paddleImage, (int) world.paddle.x, (int) world.paddle.y);
        Block block;
        int stop = world.blocks.size();
        for(int i = 0; i < stop; i++)
        {
            block = world.blocks.get(i);
            game.drawBitmap(blocksImage, (int)block.x, (int)block.y, 0,
                    (int)(block.type*Block.HEIGHT), (int)Block.WIDTH, (int)Block.WIDTH);
        }
    }
}
