import java.util.NoSuchElementException;
import java.util.Iterator;

public class DoublyLinkedList<E extends Comparable<E>> implements Iterable<E> {
 
    private Node head;
    private Node tail;
    private int size;
     
    public DoublyLinkedList() {
        size = 0;
    }
    private class Node {
        E element;
        Node next;
        Node prev;
 
        public Node(E element, Node next, Node prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }

    public int size() 
    { 
        return size; 
    }

    public boolean isEmpty() 
    { 
        return size == 0; 
    }

    public void addFirst(E element) {
        Node tmp = new Node(element, null, null);
        if (isEmpty())
        {
            head = tmp;
            tail = tmp;
        }
        else
        {
            tmp.next = head;
            head = tmp;
        }
        size++;
    }
     
    public void addAfter(E insertingElement, E element) {
        Node insertingNode = new Node (insertingElement, null, null);
        if (isEmpty())
        {
            head = insertingNode;
            tail = insertingNode;
        }
        else
        {
            Node tmp = head;
            while (tmp != null)
            {
                if (tmp.element == element)
                {
                    if (tmp.next == null)
                    {
                        tmp.next = insertingNode;
                        insertingNode.prev = tmp;
                        tail = insertingNode;
                    }
                    else
                    {
                        insertingNode.next = tmp.next;
                        insertingNode.prev = tmp;
                        tmp.next = insertingNode;
                        insertingNode.next.prev = insertingNode;
                    }
                }
                tmp = tmp.next;
            }
        }
        size++;
    }
     
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        Node tmp = head;
        head = head.next;
        head.prev = null;
        size--;
        return tmp.element;
    }

    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        Node tmp = tail;
        tail = tail.prev;
        tail.next = null;
        size--;
        return tmp.element;
    }
    
    public void remove(E searchElement)
    {
        Node tmp = head;
        E element;
        while (tmp != null)
        {
            if (tmp.element == searchElement)
            {
                break;
            }
            tmp = tmp.next;
        }
        if (tmp == null)
        {
            return;
        }
        else
        {
            if (tmp == head && tail == tmp)
            {
                head = null;
                tail = null;
                size--;
                return;
            }
            if (tmp == head)
            {
                element = removeFirst();
            }
            else if(tmp == tail)
            {
                element = removeLast();
            }
            else
            {
                 tmp.prev.next = tmp.next;
                 tmp.next.prev = tmp.prev;
                 size--;
            }
        }
    }
    
    public boolean contains(E searchElement)
    {
        Node tmp = head;
        while (tmp != null)
        {
            if (tmp.element == searchElement)
            {
                return true;
            }
            tmp = tmp.next;
        }
        return false;
    }
    
    public Iterator<E> iterator() 
    {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<E> 
    {
        private Node current = head;
        public boolean hasNext() 
        {
            return current != null;
        }
        public E next() 
        {
            E item = current.element;
            current = current.next;
            return item;
        }
    }
    
}