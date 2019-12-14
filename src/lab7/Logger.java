package lab7;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    List<List<Double>> philosophersWaitingTimes;

    public Logger(int N, AlgoType type) {
        philosophersWaitingTimes = new ArrayList<>();
        for(int i = 0; i < N; i++){
            philosophersWaitingTimes.add(new ArrayList<Double>());
        }
    }
    public void addMeasurement(double time, int philosopherID){
        philosophersWaitingTimes.get(philosopherID).add(time);
    }

    public void summarize(){
        List<Double> means = new ArrayList<>();
        for(List<Double> times: philosophersWaitingTimes){
            means.add(times.stream().reduce(Double.parseDouble("0"), Double::sum)/ (long) times.size());
        }
        System.out.println(means);
    }
}
