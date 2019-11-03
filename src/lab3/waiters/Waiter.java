package lab3.waiters;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Waiter {

    private Person person1;
    private Person person2;

    private ReentrantLock lock;
    private Condition seatsChanged;
    private Condition waitingForPartner;
    private List<Integer> waitingPeople;
//    private List<Integer, Boolean>

    private Map<Integer, Condition> clientsConditions;

    public void addToClients(Person p){
        clientsConditions.put(p.pairID, lock.newCondition());
    }

    public Waiter(){
        lock = new ReentrantLock();
        seatsChanged = lock.newCondition();
        waitingForPartner = lock.newCondition();
        person1 = null;
        person2 = null;
        waitingPeople = new LinkedList<Integer>();
        clientsConditions = new HashMap<>();
    }


    void getSeated(Person p) throws InterruptedException {
        try {
            lock.lock();
            System.out.println(waitingPeople);
            boolean wasWaiting = false;
            if (waitingPeople.isEmpty() || !waitingPeople.contains(p.pairID)) {
                waitingPeople.add(p.pairID);
                wasWaiting = true;
                System.out.println("Person from no " + p.innerID + " pair " + p.pairID + " Waiting for partner");
                System.out.println(waitingPeople);
                clientsConditions.get(p.pairID).await();
            }
            if (wasWaiting) {
                System.out.println("Person from no " + p.innerID + " pair " + p.pairID + " Stopped waiting for partner");
//                waitingPeople.forEach(e -> System.out.print(e));
                System.out.println(waitingPeople);
                waitingPeople.remove(p.pairID);
                System.out.println(waitingPeople);
            }

            clientsConditions.get(p.pairID).signal();

            while ((person1 != null && person1.pairID != p.pairID) || (person2 != null && person2.pairID == p.pairID)) {
                System.out.println("Person from pair " + p.pairID + " Waiting for table");
                seatsChanged.await();
            }

            if (person1 == null) {
                person1 = p;
            } else {
                person2 = p;
            }
        }
        finally {
            lock.unlock();
        }

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

//    public void getSeated(Person p) throws InterruptedException {
//       try{
//           lock.lock();
//           seatingCheck2(p);
//       }
//        finally{
//            lock.unlock();
//        }
//    }

    public void releaseSeat(Person person) {
        try{
            lock.lock();
            if(person1 == person) person1 = null;
            else if (person2 == person) person2 = null;
            seatsChanged.signalAll();
        }
        finally {
            lock.unlock();

        }
    }
}
