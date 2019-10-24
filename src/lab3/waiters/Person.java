package lab3.waiters;

public class Person implements Runnable{
    public final int pairID;
    private Waiter waiter;

    @Override
    public void run() {
        System.out.println("PERSON []");
        waiter.getSeated(this);

    }

    public Person(int pairID, Waiter waiter){
        this.pairID = pairID;
        this.waiter = waiter;
    }


}
