package hse.java.lectures.lecture3.practice.randomSet;

public class Node<K, V> {
    K key;
    V value;
    Node<K, V> next;

    public Node(K key , V value , Node<K,V> next ) {
        this.key = key ;
        this.value = value;
        this.next = next;
    }


}
