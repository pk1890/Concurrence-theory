package lab9.articleSolution;

import java.util.concurrent.Semaphore;

public class Buffer{
    public Semaphore in = new Semaphore(1);
    public Semaphore out = new Semaphore(1);
    public Semaphore wrt = new Semaphore(0);
    public int buffer = 0;
    public int ctrin = 0;
    public int ctrout = 0;
    public boolean wait = false;
}
