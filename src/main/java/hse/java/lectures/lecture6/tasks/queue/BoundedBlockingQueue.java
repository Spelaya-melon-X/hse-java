package hse.java.lectures.lecture6.tasks.queue;

import java.util.LinkedList;

public class BoundedBlockingQueue<T> {
    final Integer capacity ;
    LinkedList<T> list;
    Object monitor ;


    public BoundedBlockingQueue(int capacity) {
        if ( capacity <= 0 ) {
            throw new IllegalArgumentException() ;
        }
        this.capacity = capacity;
        list = new LinkedList<>();
        monitor = new Object();


    }

    public void put(T item) throws InterruptedException {
        if ( item == null) throw new IllegalArgumentException() ;


        synchronized (monitor) {
            while ( capacity ==  list.size()) {
                monitor.wait();
            }
            list.add(item) ;
            monitor.notifyAll();
        }

    }

    public T take() throws InterruptedException {
        synchronized (monitor) {
            while (list.isEmpty()) {
                monitor.wait();
            }
            T res  = list.removeFirst();
            monitor.notifyAll();
            return  res;
        }
    }

    public int size() {
        synchronized (monitor) {
            return list.size();
        }
    }

    public int capacity() {
        return capacity;
    }
}