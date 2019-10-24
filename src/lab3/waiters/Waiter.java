package lab3.waiters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Waiter {
    private Person person1;
    private Person person2;

    private ReentrantLock lock;
    private Condition seatsChanged;
    private Condition waitingForPartner;

    public Waiter(){
        lock = new ReentrantLock();
        seatsChanged = lock.newCondition();
        waitingForPartner = lock.newCondition();
        person1 = null;
        person2 = null;
    }

    void seatingCheck(Person p) throws InterruptedException {
        if(person1 == null && person2 == null){
            person1 = p;
            System.out.println("Person from pair " + p.pairID + " seated on first seat");
            while (person2 == null){
                System.out.println("Person from pair " + p.pairID + " Waiting for partner");
                waitingForPartner.await();
            }
            return;
        }
        else if(person1 != null && person2 == null){
            if(person1.pairID == p.pairID){
                person2 = p;
                System.out.println("Person from pair " + p.pairID + " seated on second seat");
                waitingForPartner.signal();
                return;
            } else {
                System.out.println("Person from pair " + p.pairID + " waits for free seat");
                seatsChanged.await();
                seatingCheck(p);
                return;
            }
        }else if(person1 == null && person2 != null){
            if(person2.pairID == p.pairID){
                person1 = p;
                System.out.println("Person from pair " + p.pairID + " seated on first seat");
                waitingForPartner.signal();
                return;
            } else {
                System.out.println("Person from pair " + p.pairID + " waits for free seat");
                seatsChanged.await();
                seatingCheck(p);
                return;
            }
        } else {
            System.out.println("Person from pair " + p.pairID + " waits for free seat");
            seatsChanged.await();
            seatingCheck(p);
            return;
        }
    }

    public void getSeated(Person p) throws InterruptedException {
        lock.lock();
        seatingCheck(p);
        lock.unlock();
    }

    public void releaseSeat(Person person) {
        lock.lock();
        if(person1 == person) person1 = null;
        else if (person2 == person) person2 = null;
        seatsChanged.signalAll();
        lock.unlock();
    }
}
