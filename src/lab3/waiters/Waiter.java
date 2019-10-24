package lab3.waiters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Waiter {
    private Person person1;
    private Person person2;

    private ReentrantLock lock;
    private Condition seatsChanged;
    private Condition twoAreSeated;

    public Waiter(){
        lock = new ReentrantLock();
        seatsChanged = lock.newCondition();
        twoAreSeated = lock.newCondition();
        person1 = null;
        person2 = null;
    }

    void seatingCheck(Person p) throws InterruptedException {
        if(person1 == null && person2 == null){
            person1 = p;
            System.out.println("Person from pair" + p.pairID + "seated on first seat");
            return;
        }
        else if(person1 != null && person2 == null){
            if(person1.pairID == p.pairID){
                person2 = p;
                return;
            } else {
                seatsChanged.await();
                seatingCheck(p);
                return;
            }
        }else if(person1 == null && person2 != null){
            if(person2.pairID == p.pairID){
                person1 = p;
                return;
            } else {
                seatsChanged.await();
                seatingCheck(p);
                return;
            }
        } else {
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
}
