package dk.kea.class2016february.emilmadsen.helloworld;

/**
 * Created by Emil on 29-02-2016.
 */
public interface TouchHandler
{
    boolean isTouchDown(int pointer);
    int getTouchX(int pointer);
    int getTouchY(int pointer);
}
