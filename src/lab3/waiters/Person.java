package lab3.waiters;

import java.util.Random;
import java.util.concurrent.locks.Condition;

public class Person implements Runnable{
    public final int pairID;
    public final int innerID;
    private Waiter waiter;
    public Condition waitForPartner;

    @Override
    public void run() {
        System.out.println(Colors.ANSI_GREEN + "PERSON [" + pairID + ":" + innerID +"] : wants to be seated" + Colors.ANSI_RESET);
        while (true) {
            try {
                waiter.getSeated(this);
                System.out.println(Colors.ANSI_CYAN + "PERSON [" + pairID + ":" + innerID+ "] : Eating..." + Colors.ANSI_RESET);
                Thread.sleep(new Random().nextInt(300));
                waiter.releaseSeat(this);
                System.out.println(Colors.ANSI_RED + "PERSON [" + pairID + ":" + innerID+ "] : Walks away" + Colors.ANSI_RESET);
                Thread.sleep(new Random().nextInt(300));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public Person(int pairID, int innerID, Waiter waiter){
        this.pairID = pairID;
        this.waiter = waiter;
        this.innerID = innerID;
        waiter.addToClients(this);
    }


}
