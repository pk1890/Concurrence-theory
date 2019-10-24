package lab3.waiters;

public class Main {
    public static void main(String[] args) {
        Waiter waiter = new Waiter();
        int pairsNo = 3;
        for (int i = 0; i < pairsNo; i++){
            new Thread(new Person(i, waiter)).start();
            new Thread(new Person(i, waiter)).start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
