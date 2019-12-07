package lab8.zad2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveTask;

public class Main {

    public static ArrayList<Long> generateTestArray(){
        Random random = new Random();
        ArrayList<Long> ret = new ArrayList<>();
        random.longs(10_000_000, 0, 10_000_000).forEach(ret::add);
        return ret;
    }

    public static long naiveSum(List<Long> array){
        return array.stream().parallel().reduce(0L, Long::sum);
    }

    public static long sum(List<Long> array){
        Sum sum = new Sum(array);
        return sum.compute();
    }

    public static void main(String[] args) {
        List<Long> testList = generateTestArray();
        long start, end;
        long sum;
        start = System.nanoTime();
        sum = naiveSum(testList);
        end = System.nanoTime();

        System.out.println("NAIVE SUM: "+sum+" TIME: " + (end-start)/10000);

        start = System.nanoTime();
        sum = sum(testList);
        end = System.nanoTime();

        System.out.println("SUM: "+sum+" TIME: " + (end-start)/10000);


    }
}
