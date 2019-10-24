package lab3.waiters;

import java.util.Random;

public class Person implements Runnable{
    public final int pairID;
    private Waiter waiter;

    @Override
    public void run() {
        System.out.println("PERSON [" + pairID +"] : wants to be seated");
        while (true) {
            try {
                waiter.getSeated(this);
                System.out.println("PERSON [" + pairID + "] : Eating...");
                Thread.sleep(new Random().nextInt(300));
                waiter.releaseSeat(this);
                System.out.println("PERSON [" + pairID + "] : Walks away");
                Thread.sleep(new Random().nextInt(300));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public Person(int pairID, Waiter waiter){
        this.pairID = pairID;
        this.waiter = waiter;
    }


}
