package lab1;
public class Main {
    public static void main(String[] args) throws InterruptedException {
//        Counter counter = new Counter();
//
//        Thread incThread = new Thread(() -> {
//            for(int i = 0; i < 1000000; i++){
//                counter.inc();
//            }
//        });
//
//        Thread decThread = new Thread(() -> {
//            for(int i = 0; i < 1000000; i++){
//                counter.dec();
//            }
//        });
//
//        incThread.start();
//        decThread.start();
//
//        incThread.join();
//        decThread.join();
//
//        System.out.println(counter.getVal());

//        buffer buffer = new buffer();

        Buffer buffer = new Buffer();
        Consumer consumer = new Consumer(buffer, 1);
        Consumer consumer1 = new Consumer(buffer, 2);
        Producer producer = new Producer(buffer);

        Thread c1 = new Thread(consumer);
        Thread c2 = new Thread(consumer1);
        Thread p1 = new Thread(producer);

        c1.start();
        c2.start();
        p1.start();

        c1.join();
        c2.join();
        p1.join();


    }
}
