package lab9.simpleSolution;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer{
    public Semaphore mutex = new Semaphore(1);
    public Semaphore writing = new Semaphore(1);
    public int buffer = 0;
    public int counter = 0;
}
