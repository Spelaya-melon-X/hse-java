package hse.java.lectures.lecture6.tasks.synchronizer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class StreamingMonitor {

    private final ReentrantLock lock = new ReentrantLock();


    private final Condition[] turns;
    private final int n;
    private final int totalTicksAllowed;


    private int currentWriterIndex = 0;
    private int totalTicks = 0;
    private boolean done = false;


    public StreamingMonitor(int writerCount, int ticksPerWriter) {
        this.n = writerCount;
        this.totalTicksAllowed = writerCount * ticksPerWriter;
        this.turns = new Condition[writerCount];
        for (int i = 0; i < writerCount; i++) {
            turns[i] = lock.newCondition();
        }
    }


    public boolean awaitTurn(int writerIndex) throws InterruptedException {
        lock.lock();
        try {
            while (!done && currentWriterIndex != writerIndex) {
                turns[writerIndex].await();
            }
            return !done;
        } finally {
            lock.unlock();
        }
    }

    public void completeTick(int writerIndex) {
        lock.lock();
        try {
            totalTicks++;
            if (totalTicks >= totalTicksAllowed) {
                done = true;

                for (Condition c : turns) {
                    c.signalAll();
                }
            } else {
                currentWriterIndex = (writerIndex + 1) % n;
                turns[currentWriterIndex].signal();
            }
        } finally {
            lock.unlock();
        }
    }


    public boolean isDone() {
        lock.lock();
        try {
            return done;
        } finally {
            lock.unlock();
        }
    }
}