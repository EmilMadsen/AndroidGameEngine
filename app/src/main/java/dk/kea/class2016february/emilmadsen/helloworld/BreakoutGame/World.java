package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

import java.util.ArrayList;
import java.util.List;

import dk.kea.class2016february.emilmadsen.helloworld.CollisionListener;

public class World
{
    public static final float MIN_X = 0;
    public static final float MAX_X = 319;
    public static final float MIN_Y = 32;
    public static final float MAX_Y = 479;
    boolean gameOver = false;
    int level = 1;
    int points = 0;
    Ball ball = new Ball();
    Paddle paddle = new Paddle();
    List<Block> blocks = new ArrayList<>();
    CollisionListener collisionListener;

    public World(CollisionListener collisionListener)
    {
        generateBlocks();
        this.collisionListener = collisionListener;
    }

    public void update(float deltaTime, float accelX, int touchX)
    {
        if(ball.y + Ball.HEIGHT > MAX_Y)
        {
            gameOver = true;
            return;
        }

        if(blocks.size() == 0)
        {
            generateBlocks();
            ball = new Ball();
            ball.velocityX = ball.velocityX * (1.0f + (float)level*0.1f);
            ball.velocityY = ball.velocityY * (1.0f + (float)level*0.1f);
            paddle = new Paddle();
        }

        //ball.velocityY += 20;
        ball.x = ball.x + ball.velocityX * deltaTime;
        ball.y = ball.y + ball.velocityY * deltaTime;

        collideWalls();

        paddle.x = paddle.x - (accelX * 50 * deltaTime);
        if(touchX > 0) paddle.x = touchX - (int)Paddle.WIDTH/2;
        if(paddle.x < MIN_X)
        {
            paddle.x = MIN_X;
        }
        if(paddle.x + paddle.WIDTH > MAX_X)
        {
            paddle.x = MAX_X - paddle.WIDTH;
        }

        collideBallPaddle();
        collideBallBlocks(deltaTime);

    }

    private void collideBallBlocks(float deltaTime)
    {
        Block block;
        int stop = blocks.size();
        for(int i = 0; i < stop; i++)
        {
            block = blocks.get(i);
            if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                    block.x, block.y, Block.WIDTH, Block.HEIGHT))
            {
                points = points + ((10-block.type)* 20);
                collisionListener.collisionBlock();
                blocks.remove(i);
                i--;
                stop--;
                float oldvx = ball.velocityX;
                float oldvy = ball.velocityY;
                reflectBall(ball, block);
                ball.x = ball.x -oldvx * deltaTime * 1.01f;
                ball.y = ball.y -oldvy * deltaTime * 1.01f;
            }
        }
    }

    private void reflectBall(Ball ball, Block block)
    {
        // check top left corner of the block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, 1,1))
        {
            if(ball.velocityX > 0) ball.velocityX = -ball.velocityX;
            if(ball.velocityY > 0) ball.velocityY = -ball.velocityY;
            return;
        }

        //Top Right Corner.
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x+Block.WIDTH, block.y, 1,1))
        {
            if(ball.velocityX < 0) ball.velocityX = -ball.velocityX;
            if(ball.velocityY > 0) ball.velocityY = -ball.velocityY;
            return;
        }

        //Bot Left Corner
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y+Block.HEIGHT, 1,1))
        {
            if(ball.velocityX > 0) ball.velocityX = -ball.velocityX;
            if(ball.velocityY < 0) ball.velocityY = -ball.velocityY;
            return;
        }

        //Bot Right Corner
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x+Block.WIDTH, block.y+Block.HEIGHT, 1,1))
        {
            if(ball.velocityX < 0) ball.velocityX = -ball.velocityX;
            if(ball.velocityY < 0) ball.velocityY = -ball.velocityY;
            return;
        }

        //Check the top edge of the block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y+Block.HEIGHT, Block.WIDTH, 1))
        {
            if(ball.velocityY > 0) ball.velocityY = -ball.velocityY;
            return;
        }

        // check the bot edge of the block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, Block.WIDTH, 1))
        {
            if(ball.velocityY < 0) ball.velocityY = -ball.velocityY;
            return;
        }

        //left edge of the block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, 1, Block.HEIGHT))
        {
            if(ball.velocityX > 0) ball.velocityX = -ball.velocityX;
            return;
        }

        //right edge of the block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x+Block.WIDTH, block.y, 1, Block.HEIGHT))
        {
            if(ball.velocityX < 0) ball.velocityX = -ball.velocityX;
            return;
        }
    }

    private boolean collideRects(float x, float y, float width, float height, float x2, float y2,
                                 float width2, float height2)
    {
        if(x < x2 + width2 &&
                x + width > x2 &&
                y < y2 + height2 &&
                y + height > y2)
        {
            return true;
        }
        return false;
    }

    private void collideBallPaddle()
    {
        if(ball.y > paddle.y) return;

        // Checks if ball hits right side of paddle
        if(ball.x + Ball.WIDTH >= paddle.x && // If the ball hits the sides of paddle
                ball.x + Ball.WIDTH < paddle.x + 3 &&
                ball.y + Ball.HEIGHT + 2 >= paddle.y)
        {
            ball.y = paddle.y - Ball.HEIGHT - 0; // "Resetter" ball til oversiden af paddle
            ball.velocityY = -ball.velocityY; //ændrer retning på bold
            if(ball.velocityX > 0) ball.velocityX = -ball.velocityX;
            collisionListener.collisionPaddle();
            return;
        }

        // Checks if ball hits right side of paddle
        if(ball.x < paddle.x+Paddle.WIDTH &&
                ball.x > paddle.x + Paddle.WIDTH - 3 &&
                ball.y + Ball.HEIGHT + 2 >= paddle.y)
        {
            ball.y = paddle.y - Ball.HEIGHT - 0;
            ball.velocityY = -ball.velocityY;
            if(ball.velocityX < 0) ball.velocityX = -ball.velocityX;
            collisionListener.collisionPaddle();
            return;
        }

        //Checks if the ball hits the paddle (middle).
        if(ball.x + Ball.WIDTH >= paddle.x &&
                ball.x < paddle.x + Paddle.WIDTH &&
                ball.y + Ball.HEIGHT + 2 >= paddle.y)
        {
            if (ball.velocityY > 0)
            {
                ball.y = paddle.y - Ball.HEIGHT - 0;
                ball.velocityY = -ball.velocityY;
                collisionListener.collisionPaddle();
            }
        }
    }

    private void collideWalls()
    {
        if(ball.x < MIN_X)
        {
            ball.velocityX = -ball.velocityX;
            ball.x = MIN_X;
            collisionListener.collisionWall();
        }
        else if(ball.x + Ball.WIDTH > MAX_X)
        {
            ball.velocityX = -ball.velocityX;
            ball.x = MAX_X - Ball.WIDTH;
            collisionListener.collisionWall();

        }
        if(ball.y < MIN_Y)
        {
            ball.velocityY = -ball.velocityY;
            ball.y = MIN_Y;
            collisionListener.collisionWall();
        }

    }

    private void generateBlocks()
    {
        blocks.clear();
        int type = 0;
        int startY = (int) (MIN_Y + Ball.HEIGHT * 2);
        startY = startY + level*20; // will make it go lower, at higher levels.
        if(startY > 200) startY = 200; // dont go too low
        for(int y = startY; y < startY + 8*Block.HEIGHT; y = (int)(y + Block.HEIGHT), type++)
        {
            for(int x = 20; x < 320-Block.WIDTH/2; x = (int)(x + Block.WIDTH))
            {
                blocks.add(new Block(x, y, type));
            }
        }
    }

}
