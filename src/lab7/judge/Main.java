package lab7.judge;

import lab7.AlgoType;
import lab7.Logger;
import lab7.judge.Philosopher;
import lab7.judge.Table;

public class Main {


    public static void main(String[] args) {
        Table table = new Table(5);
        Logger logger = new Logger(5, AlgoType.JUDGE);
        for(int i = 0; i < 5; i++){
            new Thread(new Philosopher(table, i, logger)).start();
        }
        try {
            Thread.sleep(10000);
            logger.summarize();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//MEANS: [1.3102834478E9, 9.72725855E8, 1.4604116825E9, 1.51969352875E9, 1.00150162425E9]

}
