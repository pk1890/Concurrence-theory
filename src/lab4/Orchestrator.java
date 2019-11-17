package lab4;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

enum MeasureType{
    NAIVE,
    FAIRWAITING
}

public class Orchestrator {

    public static int[] M = {1000, 10000, 100000};
    public static int[] processes = {20, 200, 2000};

    public static void main(String[] args){
        for(int m : M){
            for(int process : processes){
                try {
                    System.out.println(m+" " + process + "NAIVE");
                    makeMeasurements(m, process, MeasureType.NAIVE);
                    System.out.println(m+" " + process + "FAIR");
                    makeMeasurements(m, process, MeasureType.FAIRWAITING);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static void makeMeasurements(int M, int processCount, MeasureType type) throws InterruptedException {
        switch (type){
            case NAIVE: {
                Logger.init(processCount, 2*M, "naive");
                lab4.randomNaive.Buffer buff = new lab4.randomNaive.Buffer(M);
                List<Thread> producers = new ArrayList<>();
                List<Thread> consumers = new ArrayList<>();
                for (int i = 0; i < processCount; i++) {
                    producers.add(new Thread(new lab4.randomNaive.Producer(buff, i)));
                    consumers.add(new Thread(new lab4.randomNaive.Consumer(buff, i)));
                }

                //starting
                for (int i = 0; i < processCount; i++) {
                    producers.get(i).start();
                    consumers.get(i).start();
                }

                Thread.sleep(3000);

                try {
                    Logger.summary();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            case FAIRWAITING: {
                Logger.init(processCount, 2*M, "fair");
                lab4.randomFairWaiting.Buffer buff = new lab4.randomFairWaiting.Buffer(M);
                List<Thread> producers = new ArrayList<>();
                List<Thread> consumers = new ArrayList<>();
                for (int i = 0; i < processCount; i++) {
                    producers.add(new Thread(new lab4.randomFairWaiting.Producer(buff, i)));
                    consumers.add(new Thread(new lab4.randomFairWaiting.Consumer(buff, i)));
                }

                //starting
                for (int i = 0; i < processCount; i++) {
                    producers.get(i).start();
                    consumers.get(i).start();
                }

                Thread.sleep(3000);

                try {
                    Logger.summary();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }
    }
}
