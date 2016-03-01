package dk.kea.class2016february.emilmadsen.helloworld;

import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.util.List;

public class MultiTouchHandler implements TouchHandler, View.OnTouchListener
{
    private boolean[] isTouched = new boolean[20];
    private int[] touchX = new int[20];
    private int[] touchY = new int[20];

    private List<TouchEvent> touchEventBuffer;
    private TouchEventPool touchEventPool;

    public MultiTouchHandler(View view, List<TouchEvent> touchEventBuffer, TouchEventPool touchEventPool)
    {
        view.setOnTouchListener(this);
        this.touchEventBuffer = touchEventBuffer;
        this.touchEventPool = touchEventPool;
    }

    @Override
//    public synchronized boolean onTouch(View view, MotionEvent event)
    public boolean onTouch(View view, MotionEvent event)
    {
        synchronized (touchEventBuffer)
        {
            TouchEvent touchEvent = null;
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            int pointerId = event.getPointerId(pointerIndex);

            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchEvent = touchEventPool.obtain();
                    touchEvent.type = TouchEvent.TouchEventType.Down;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int)(event.getX(pointerIndex));
                    touchEvent.y = touchY[pointerId] = (int)(event.getY(pointerIndex));
                    isTouched[pointerId] = true;
                    touchEventBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent = touchEventPool.obtain();
                    touchEvent.type = TouchEvent.TouchEventType.Up;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int)(event.getX(pointerIndex));
                    touchEvent.y = touchY[pointerId] = (int)(event.getY(pointerIndex));
                    isTouched[pointerId] = false;
                    touchEventBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int pointerCount = event.getPointerCount();
                    for(int i = 0; i < pointerCount; i++)
                    {
                        touchEvent = touchEventPool.obtain();
                        touchEvent.type = TouchEvent.TouchEventType.Dragged;
                        pointerIndex = i;
                        pointerId = event.getPointerId(pointerIndex);
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[pointerId] = (int)(event.getX(pointerIndex));
                        touchEvent.y = touchY[pointerId] = (int)(event.getY(pointerIndex));
                        touchEventBuffer.add(touchEvent);
                    }
                    break;
            }
        }

        return true;
    }

    @Override
    public synchronized boolean isTouchDown(int pointer)
    {
        if(pointer < 0 || pointer >= 20)
            return false;
        else
            return isTouched[pointer];
    }

    @Override
    public synchronized int getTouchX(int pointer)
    {
        if(pointer < 0 || pointer >= 20)
            return -1;
        else
            return touchX[pointer];
    }

    @Override
    public synchronized int getTouchY(int pointer)
    {
        if(pointer < 0 || pointer >= 20)
            return -1;
        else
            return touchY[pointer];
    }
}
