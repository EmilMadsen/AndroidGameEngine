package dk.kea.class2016february.emilmadsen.helloworld;

import java.util.ArrayList;
import java.util.List;

public abstract class Pool<T>
{
    private List<T> items = new ArrayList<>();
    protected abstract T newItem();

    public T obtain()
    {
        int last = items.size() -1;
        if(last == -1)return newItem();
        return items.remove(last);
    }

    public void free(T item) //returns the item to the pool
    {
        items.add(item);
    }
}
