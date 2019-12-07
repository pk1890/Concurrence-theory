package lab8.zad2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Sum extends RecursiveTask<Long> {
    final List<Long> list;
    public Sum(List<Long> list){
        this.list = list;
    }
    @Override
    protected Long compute() {
        if(list.size() <= 5000) return list.stream().reduce(0L, Long::sum);
        Sum sum1 = new Sum(list.subList(0, list.size()/2));
        Sum sum2 = new Sum(list.subList(list.size()/2, list.size()));
        sum2.fork();
        return sum1.compute() + sum2.join();
    }
}
