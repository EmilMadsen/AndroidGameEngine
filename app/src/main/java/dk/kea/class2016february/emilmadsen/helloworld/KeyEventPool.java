package dk.kea.class2016february.emilmadsen.helloworld;

/**
 * Created by Emil on 07-03-2016.
 */
public class KeyEventPool extends Pool<MyKeyEvent>
{
    @Override
    protected MyKeyEvent newItem()
    {
        return new MyKeyEvent();
    }
}
