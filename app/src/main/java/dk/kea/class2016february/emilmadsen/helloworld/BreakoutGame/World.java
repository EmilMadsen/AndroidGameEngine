package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

public class World
{
    public static final float MIN_X = 0;
    public static final float MAX_X = 319;
    public static final float MIN_Y = 32;
    public static final float MAX_Y = 479;
    Ball ball = new Ball();

    public void update(float deltaTime)
    {
        //ball.velocityY += 20;
        ball.x = ball.x + ball.velocityX * deltaTime;
        ball.y = ball.y + ball.velocityY * deltaTime;
        if(ball.x < MIN_X)
        {
            ball.velocityX = -ball.velocityX;
            ball.x = MIN_X;
        }
        if(ball.x + Ball.WIDTH > MAX_X)
        {
            ball.velocityX = -ball.velocityX;
            ball.x = MAX_X - Ball.WIDTH;
        }
        if(ball.y < MIN_Y)
        {
            ball.velocityY = -ball.velocityY;
            ball.y = MIN_Y;
        }
        if(ball.y + Ball.HEIGHT > MAX_Y)
        {
            ball.velocityY = -ball.velocityY;
            ball.y = MAX_Y - Ball.HEIGHT;
        }

    }

}
