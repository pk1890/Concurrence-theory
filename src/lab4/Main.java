package lab4;

import java.util.Random;

public class Main {

    public static void main(String[] args) {

        final int BUFF_SIZE = 100;
        final int PROCESSORS_NO = 5;

        Random random = new Random();

        Buffer buff = new Buffer(BUFF_SIZE);
        Producer producer = new Producer(buff, random.nextInt(10));
        new Thread(producer).start();
        Consumer consumer = new Consumer(buff, random.nextInt(10));
        new Thread(consumer).start();

        for(int i = 0; i < PROCESSORS_NO; i++){
            new Thread(new Processor(i, buff, random.nextInt(10))).start();
        }

    }
}
