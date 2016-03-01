package dk.kea.class2016february.emilmadsen.helloworld;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class Game extends Activity implements Runnable, View.OnKeyListener, TouchHandler
{
    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>();
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Screen screen;
    private Canvas canvas;
    private Bitmap virtualSurface;
    private Rect src = new Rect();
    private Rect dst = new Rect();
    private boolean[] pressedKeys = new boolean[256];

    private TouchHandler touchHandler;
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEvents = new ArrayList<>();
    private List<TouchEvent> touchEventBuffer = new ArrayList<>();

    public abstract Screen createStartScreen();


    protected void onCreate(Bundle instanceBundle)
    {
        super.onCreate(instanceBundle);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //probably not needed since addFlags(), calls setFlags()
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();
        screen = createStartScreen();
        if(surfaceView.getWidth() > surfaceView.getHeight())
        {
            setVirtualSurface(480, 320);
        }
        else
        {
            setVirtualSurface(320, 480);
        }

        surfaceView.setFocusableInTouchMode(true);
        surfaceView.requestFocus();
        surfaceView.setOnKeyListener(this);
        touchHandler = new MultiTouchHandler(surfaceView, touchEventBuffer, touchEventPool);
    }

    public void setVirtualSurface(int width, int height)
    {
        if(virtualSurface != null) virtualSurface.recycle();
        virtualSurface = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(virtualSurface);
    }



    public void setScreen(Screen newScreen)
    {
        if(screen != null) screen.dispose();
        screen = newScreen;
    }

    public Bitmap loadBitmap(String fileName)
    {
        InputStream in = null;
        Bitmap bitmap = null;
        try
        {
            in = getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if(bitmap == null)
                throw new RuntimeException("Could not get a bitmap from the file: " + fileName);
            return bitmap;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not load the file: " + fileName);
        }
        finally
        {
            if (in != null)
                try
                {
                    in.close();
                }
                catch(IOException e)
                {
                    Log.d("closing inputstream","Shit");
                }
        }
    }

 /*   public Music loadMusic(String fileName)
    {
        return null;
    }

    public Sound loadSound(String fileName)
    {
        return null;
    }
 */

    public void clearFramebuffer(int color)
    {
        if(canvas != null) canvas.drawColor(color);
    }

    public int getFramebufferWidth()
    {
        return surfaceView.getWidth();
    }

    public int getFramebufferHeight()
    {
        return surfaceView.getHeight();
    }

    public void drawBitmap(Bitmap bitmap, int x, int y)
    {
        if(canvas != null) canvas.drawBitmap(bitmap, x, y, null);
    }

    public void drawBitmap(Bitmap  bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
    {
        if(canvas == null) return;
        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWidth;
        src.bottom = srcY + srcHeight;

        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;

        canvas.drawBitmap(bitmap, src, dst, null);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            pressedKeys[keyCode] = true;
        }
        else if (event.getAction() == KeyEvent.ACTION_UP)
        {
            pressedKeys[keyCode] = false;
        }
        return false;
    }

    public boolean isKeyPressed(int keyCode)
    {
        return pressedKeys[keyCode];
    }

    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }

    public int getTouchX(int pointer)
    {
        float ratioX = (float)virtualSurface.getWidth() / (float)surfaceView.getWidth();
        int x = touchHandler.getTouchX(pointer);
        x = (int)(x * ratioX);

        return x;
    }

    public int getTouchY(int pointer)
    {
        float ratioY = (float)virtualSurface.getHeight() / (float)surfaceView.getHeight();
        int y = touchHandler.getTouchY(pointer);
        y = (int)(y * ratioY);

        return y;
    }

//    public List<KeyEvent> getKeyEvents()
//    {
//        return null;
//    }

    public float[] getAccelerometer()
    {
        return null;
    }

    // This is the main method for the game loooop
    public void run()
    {
        while(true)
        {
            synchronized (stateChanges)
            {
                for(int i = 0; i < stateChanges.size(); i++)
                {
                    state = stateChanges.get(i);
                    if(state == State.Disposed)
                    {
                        if(screen != null)screen.dispose();
                        Log.d("Game", "State is Disposed");
                    }
                    else if(state == State.Paused)
                    {
                        if(screen != null)screen.pause();
                        Log.d("Game", "State is Paused");
                    }
                    else if(state == State.Resumed)
                    {
                        if(screen != null)screen.resume();
                        state = State.Running;
                        Log.d("Game", "State is Resumed");
                    }
                }
                stateChanges.clear();
            }
            if(state == State.Running)
            {
                if(!surfaceHolder.getSurface().isValid()) continue;
                Canvas physicalCanvas = surfaceHolder.lockCanvas();
                // here we should do some drawing on the screen
                //canvas.drawColor(Color.BLUE);
                if(screen != null)screen.update(0);

                src.left = 0;
                src.top = 0;
                src.right = virtualSurface.getWidth() -1;
                src.bottom = virtualSurface.getHeight() -1;

                dst.left = 0;
                dst.top = 0;
                dst.right = surfaceView.getWidth();
                dst.bottom = surfaceView.getHeight();
                physicalCanvas.drawBitmap(virtualSurface, src, dst, null);
                surfaceHolder.unlockCanvasAndPost(physicalCanvas);
                //physicalCanvas = null;
            }
        }
    }

    public void onPause()
    {
        super.onPause();
        synchronized (stateChanges)
        {
            if(isFinishing())
            {
                stateChanges.add(stateChanges.size(), State.Disposed);
            }
            else
            {
                stateChanges.add(stateChanges.size(), State.Paused);
            }
        }
        try
        {
            mainLoopThread.join();
        }
        catch (InterruptedException e){}
    }

    public void onResume()
    {
        super.onResume();
        mainLoopThread = new Thread(this);
        mainLoopThread.start();
        synchronized (stateChanges)
        {
            stateChanges.add(stateChanges.size(), State.Resumed);
        }
    }

}
