package hse.java.lectures.lecture3.practice.randomSet;

import java.util.Objects;
import java.util.Random;

import static hse.java.lectures.lecture3.practice.randomSet.HashMap.getDefaultCapacity;


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
        if (hashMap.empty()) {
            throw new EmptySetException("попытка получить случайный элемент из пустого множества.");
        }
        Random random = new Random();
        return null ;

    }


}
