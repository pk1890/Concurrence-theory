package lab3.out;

import lab2.BinarySemaphore;

public class Counter{
    private int val;

    BinarySemaphore semaphore;

    public  void inc(){
        semaphore.P();
        val = val+1;
        semaphore.V();
    }

    public  int getVal(){
        semaphore.P();
        int tmp =  val;
        semaphore.V();
        return tmp;
    }

    public  void dec() {
        semaphore.P();
        val = val-1;
        semaphore.V();
    }


    public Counter(){
        this.val = 0;
        semaphore = new BinarySemaphore();
    }
}
