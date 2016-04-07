package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

import android.graphics.Bitmap;

import dk.kea.class2016february.emilmadsen.helloworld.Game;
import dk.kea.class2016february.emilmadsen.helloworld.Screen;

public class GameScreen extends Screen
{
    enum State
    {
        Paused,
        Running,
        Gameover,
    }

    Bitmap background, resume, gameOver;
    State state = State.Running;
    World world;
    WorldRenderer renderer;

    public GameScreen(Game game)
    {
        super(game);
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        world = new World();
        renderer = new WorldRenderer(game, world);
//        ball = game.loadBitmap("ball.png");
//        paddle = game.loadBitmap("paddle.png");
//        blocks = game.loadBitmap("blocks.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if(state == State.Paused && game.getTouchEvents().size() > 0)
        {
            state = State.Running;
        }
        if(state == State.Gameover && game.getTouchEvents().size() > 0)
        {
            game.setScreen(new MainMenuScreen(game));
            return;
        }
        if(state == State.Running && game.getTouchY(0) < 36 && game.getTouchX(0) > 320-36)
        {
            state = State.Paused; //possibly create the resume screen.
        }

        game.drawBitmap(background, 0, 0);
        if(state == State.Paused)
        {
            game.drawBitmap(resume, 160 - resume.getWidth()/2, 240 - resume.getHeight()/2);
        }
        if(state == State.Gameover)
        {
            game.drawBitmap(gameOver, 160 - gameOver.getWidth()/2, 240 - gameOver.getHeight()/2);
        }

        // do something with ball, blocks and paddle
        if(state == State.Running)
        {
            world.update(deltaTime);
        }
        game.drawBitmap(background, 0, 0);
        renderer.render();
    }

    @Override
    public void pause()
    {
        if(state == State.Running)
        {
            state = State.Paused;
        }
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
