package dk.kea.class2016february.emilmadsen.helloworld;

/**
 * Created by Emil on 29-02-2016.
 */
public class TouchEventPool extends Pool<TouchEvent>
{
    protected TouchEvent newItem()
    {
        return new TouchEvent();
    }
}
