package lab3.waiters;

public class Main {
    public static void main(String[] args) {
        Waiter waiter = new Waiter();
        int pairsNo = 3;
        for (int i = 0; i < pairsNo; i++){
            new Thread(new Person(i, 1, waiter)).start();
            new Thread(new Person(i, 2, waiter)).start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
