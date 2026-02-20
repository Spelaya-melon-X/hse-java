package hse.java.lectures.lecture3.practice.randomSet;

import java.util.Objects;
import java.util.Random;


public class RandomSet<T> {
    HashMap<T , Object> hashMap ;
    private static final Object PRESENT = new Object();

    RandomSet() {
        hashMap = new HashMap<>();
    }




    public boolean insert(T value) {
        return hashMap.put(value , PRESENT) == null;
    }

    public boolean remove(T value) {
        return hashMap.remove(value) != null;
    }

    public boolean contains(T value) {
        return hashMap.containsKey(value);
    }

    public T getRandom() {
        Random random = new Random();
        int index = random.nextInt() % HashMap.getDefaultCapacity();
        Node<T, Object> node = hashMap.getKeyByIndex(index);
        return node.key;
    }


}
