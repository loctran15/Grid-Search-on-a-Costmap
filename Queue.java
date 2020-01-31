
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item> implements Iterable<Item> {
    private int n;
    private Node first;
    private Node last;

    private class Node 
    {
        private Item item;
        private Node next;
    }
    
    public Queue() {
        first = null;
        last = null;
        n = 0;
    }

    public boolean isEmpty() 
    {
        return first == null;
    }

    public int size() 
    {
        return n;
    }

    public int length() 
    {
        return n;
    }

    public Item peek() 
    {
        if (isEmpty())
        {
            System.out.println("error");
        }
        return first.item;
    }

    public void enqueue(Item item) 
    {
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) 
        {
            first = last;
        }
        else oldlast.next = last;
        n++;
    }

    public Item dequeue() 
    {
        if (isEmpty())
        {
            System.out.println("error");
        }
        Item item = first.item;
        first = first.next;
        n--;
        if (isEmpty())
        {
            last = null;
        }
        return item;
    }

    public Iterator<Item> iterator() 
    {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> 
    {
        private Node current = first;
        public boolean hasNext() 
        {
            return current != null;
        }
        public Item next() 
        {
            if (!hasNext())
            {
                System.out.println("error");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}