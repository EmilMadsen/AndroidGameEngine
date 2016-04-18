package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import java.util.List;

import dk.kea.class2016february.emilmadsen.helloworld.CollisionListener;
import dk.kea.class2016february.emilmadsen.helloworld.Game;
import dk.kea.class2016february.emilmadsen.helloworld.Music;
import dk.kea.class2016february.emilmadsen.helloworld.Screen;
import dk.kea.class2016february.emilmadsen.helloworld.Sound;
import dk.kea.class2016february.emilmadsen.helloworld.TouchEvent;

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
    Typeface font;
    Music music;
    Sound bounceSound;
    Sound blockSound;

    public GameScreen(Game game)
    {
        super(game);
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        font = game.loadFont("font.ttf");
        music = game.loadMusic("music.ogg");
        bounceSound = game.loadSound("bounce.wav");
        blockSound = game.loadSound("blocksplosion.wav");
        world = new World(new CollisionListener()
        {
            public void collisionWall(){ bounceSound.play(1); }
            public void collisionPaddle(){ bounceSound.play(1); }
            public void collisionBlock(){ blockSound.play(1); }
        });
        renderer = new WorldRenderer(game, world);

//        ball = game.loadBitmap("ball.png");
//        paddle = game.loadBitmap("paddle.png");
//        blocks = game.loadBitmap("blocks.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if(world.gameOver) state = State.Gameover;
        if(state == State.Paused && game.getTouchEvents().size() > 0)
        {
            state = State.Running;
        }
        if(state == State.Gameover)
        {
            List<TouchEvent> events = game.getTouchEvents();
            int stop = events.size();
            for (int i = 0; i < stop; i++)
            {
                if(events.get(i).type == TouchEvent.TouchEventType.Up)
                {
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
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
            int touchX = -1;
            if(game.isTouchDown(0)) touchX = game.getTouchX(0);
            world.update(deltaTime, game.getAccelerometer()[0], touchX);
        }
        //game.drawBitmap(background, 0, 0);
        game.drawText(font,Integer.toString(world.points), 15,12, Color.GREEN, 10);
        renderer.render();
    }

    @Override
    public void pause()
    {
        if(state == State.Running)
        {
            music.pause();
            state = State.Paused;
        }
    }


    @Override
    public void resume()
    {
        music.play();
    }

    @Override
    public void dispose()
    {

    }
}
