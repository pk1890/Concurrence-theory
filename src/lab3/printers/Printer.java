package lab3.printers;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Printer {
    public boolean reserved;
    public final int id;

    public Printer(int i) {
        id = i;
        this.reserved = false;
    }

    public void print(){
        System.out.println(" [" + id +"] Drukuje drukuje");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
