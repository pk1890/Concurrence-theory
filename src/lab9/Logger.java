package lab9;

import lab7.AlgoType;
import lab9.simpleSolution.Reader;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    private List<List<Double>> readersWaitingTimes;
    private List<List<Double>> writersWaitingTimes;


    public Logger(int NReader, int NWriter) {
        readersWaitingTimes = new ArrayList<>();
        writersWaitingTimes = new ArrayList<>();
        for(int i = 0; i < NReader; i++){
            readersWaitingTimes.add(new ArrayList<Double>());
        }

        for(int i = 0; i < NWriter; i++){
            writersWaitingTimes.add(new ArrayList<Double>());
        }
    }
    public void addMeasurement(double time, int id, boolean isReader){
        if(isReader){
            readersWaitingTimes.get(id).add(time);
        }else {
            writersWaitingTimes.get(id).add(time);
        }
    }

    public void summarize(){
        List<Double> means = new ArrayList<>();
        for(List<Double> times: readersWaitingTimes){
            means.add(times.stream().reduce(Double.parseDouble("0"), Double::sum)/ (long) times.size());
        }
        System.out.println("READERS:");
        System.out.println(means);

        means = new ArrayList<>();
        for(List<Double> times: writersWaitingTimes){
            means.add(times.stream().reduce(Double.parseDouble("0"), Double::sum)/ (long) times.size());
        }
        System.out.println("WRITERS:");
        System.out.println(means);
    }
}
