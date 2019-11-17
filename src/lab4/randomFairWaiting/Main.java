package lab4.randomFairWaiting;

import lab4.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        int M = 1000;
        int processCount = 10;
        int measurementTime = 30;

        if(args.length >= 2) {
            M = Integer.parseInt(args[0]);
            processCount = Integer.parseInt(args[1]);
        }
        Logger.init(processCount, 2*M, "fair");

        Buffer Buffer = new Buffer(M);

        //creating
        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();
        for(int i = 0; i < processCount; i++) {
            producers.add(new Thread(new Producer(Buffer, i)));
            consumers.add(new Thread(new Consumer(Buffer, i)));
        }

        //starting
        for(int i = 0; i < processCount; i++) {
            producers.get(i).start();
            consumers.get(i).start();
        }

        Thread.sleep(1000 * measurementTime);

        Logger.summary();

        System.exit(0);
    }
}
