package lab7.starving;

import lab7.Logger;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Philosopher implements Runnable {
    Table table;
    int number;
    static final int MAX_SLEEP_TIME = 1000;
    Logger logger;


    public Semaphore leftFork(){
        return table.forks.get( (number) );
    }
    public Semaphore rightFork(){
        return table.forks.get( (number+1) % table.FORKS_NO );
    }

    public Philosopher(Table table, int no, Logger logger){
          this.table = table;
          this.number = no;
          this.logger = logger;
    }

    public void think(){
        try {
            Thread.sleep(new Random().nextInt(MAX_SLEEP_TIME));
//            System.out.println("["+number+"]: Thinking...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void eat(){
        try {
            long end;
            long start = System.nanoTime();
            table.changingForks.acquire();
            while (leftFork().availablePermits() == 0 || rightFork().availablePermits() == 0){
                table.changingForks.release();
                table.changingForks.acquire();
            }
            leftFork().acquire();
            rightFork().acquire();
//            System.out.println("["+number+"]: Eating...; FORKS: "+table.getForksStatus());
            table.changingForks.release();
            end = System.nanoTime();
            logger.addMeasurement(end-start, number);
            Thread.sleep(new Random().nextInt(MAX_SLEEP_TIME));

            table.changingForks.acquire();
            leftFork().release();
            rightFork().release();
//            System.out.println("["+number+"]: StoppedEating...");
            table.changingForks.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            eat();
            think();
        }
    }
}
