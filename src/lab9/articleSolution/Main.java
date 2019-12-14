package lab9.articleSolution;

import lab9.Logger;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        Buffer buffer = new Buffer();
        Logger logger = new Logger(5, 5);
        for(int i = 0; i < 5; i++){
            threads.add(new Thread(new Writer(buffer, i, logger)));
            threads.add(new Thread(new Reader(buffer, i, logger)));
        }
        threads.forEach(Thread::start);
        Thread.sleep(10000);
        threads.forEach(Thread::stop);
        logger.summarize();
    }

}

