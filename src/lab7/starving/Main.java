package lab7.starving;

import lab7.AlgoType;
import lab7.Logger;
import sun.awt.X11.XStateProtocol;

public class Main {


    public static void main(String[] args) {
        int N = 5;
        Table table = new Table(N);
        Logger logger = new Logger(N, AlgoType.STARVING);
        for(int i = 0; i < N; i++){
            new Thread(new Philosopher(table, i, logger)).start();
        }
        try {
            Thread.sleep(10000);
            logger.summarize();
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//MEANS: [1.421590377142857E8, 2.73202061125E8, 2.992774096E8, 2.3425661816666666E8, 6.38009394E8]

}
