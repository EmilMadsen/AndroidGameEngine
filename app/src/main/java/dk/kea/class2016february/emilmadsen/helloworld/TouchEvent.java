package dk.kea.class2016february.emilmadsen.helloworld;

/**
 * Created by Emil on 29-02-2016.
 */
public class TouchEvent
{
    public enum TouchEventType
    {
        Down,
        Up,
        Dragged
    }
    public TouchEventType type;
    public int x;
    public int y;
    public int pointer;
}
