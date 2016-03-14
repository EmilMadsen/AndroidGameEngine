package dk.kea.class2016february.emilmadsen.helloworld;

/**
 * Created by Emil on 07-03-2016.
 */
public class MyKeyEvent
{
    public enum KeyEventType
    {
        Down,
        Up
    }

    public KeyEventType type;
    public int keyCode;
    public char character;
}
