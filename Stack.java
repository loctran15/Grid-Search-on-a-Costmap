import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<Item> implements Iterable<Item> 
{
    private int n;
    private Node first;

    private class Node 
    {
        private Item item;
        private Node next;
    }

    public Stack() 
    {
        first = null;
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


    public void push(Item item) 
    {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        n++;
    }

    public Item pop() 
    {
        if (isEmpty())
        {
            System.out.println("error");
        }
        Item item = first.item; 
        first = first.next;
        n--;
        return item;
    }

    public Item peek() 
    {
        if (isEmpty())
        {
            System.out.println("error");
        }
        return first.item;
    }

    public Iterator<Item> iterator()  
    { 
        return new ListIterator();  
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext()  
        { 
            return current != null;                     
        }
        public Item next() {
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
}
