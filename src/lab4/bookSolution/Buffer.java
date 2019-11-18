package lab4.bookSolution;

import lab4.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private int[] data;
    private Lock lock;
    private int M;
    private Condition notEnoughSpaceOthers;
    private Condition notEnoughSpaceFirst;
    private boolean isSomeoneInNotEnoughProductsFirst;
    private boolean isSomeoneInNotEnoughSpaceFirst;
    private Condition notEnoughProductsOthers;
    private Condition notEnoughProductsFirst;
    private Random rnd;

    public Buffer(int M){
        lock = new ReentrantLock();
        this.M = M;
        data = new int[2*M];
        rnd = new Random();
        notEnoughProductsOthers = lock.newCondition();
        notEnoughProductsFirst = lock.newCondition();
        notEnoughSpaceFirst = lock.newCondition();
        notEnoughSpaceOthers = lock.newCondition();
        isSomeoneInNotEnoughProductsFirst = false;
        isSomeoneInNotEnoughSpaceFirst = false;
    }


    private void insertProducts(int count) {
        for(int i = 0; count > 0; i++) {
            if(data[i] == 0) {
                count--;
                data[i] = 1;
            }
        }
    }

    private void getProducts(int count) {
        for(int i = 0; count > 0; i++) {
            if(data[i] == 1) {
                count--;
                data[i] = 0;
            }
        }
    }

    private int getFreeSpace(){
        int counter = 0;
        for(int item : data) {
            if(item == 0) counter++;
        }
        return counter;
    }

    private int getUsedSpace(){
        int counter = 0;
        for(int item : data) {
            if(item == 1) counter++;
        }
        return counter;
    }

    void produce(int producerId) {
        int portionSize = rnd.nextInt(M-1)+1;
        long timestamp;
        lock.lock();
        try {
            timestamp = System.nanoTime();
            if(isSomeoneInNotEnoughSpaceFirst) notEnoughSpaceOthers.await();
            while(getFreeSpace() < (portionSize)) {
//                System.out.println("P[" + producerId + "] try to produce " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
                notEnoughSpaceFirst.await();
            }
            Logger.addMeasurementProducer(System.nanoTime() - timestamp, portionSize);
//            System.out.println("P[" + producerId + "] produce " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
            insertProducts(portionSize);
            notEnoughSpaceOthers.signal();
            notEnoughSpaceFirst.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void consume(int consumerId) {
        int portionSize = rnd.nextInt(M-1)+1;
        long timestamp;
        lock.lock();
        try {
            timestamp = System.nanoTime();
            if(isSomeoneInNotEnoughProductsFirst) notEnoughProductsOthers.await();
            while(getUsedSpace() < portionSize) {
//                System.out.println("C[" + consumerId + "] try to consume " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
                notEnoughProductsFirst.await();
            }
            Logger.addMeasurementConsumer(System.nanoTime() - timestamp, portionSize);
//            System.out.println("C[" + consumerId + "] consume " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
            getProducts(portionSize);
            notEnoughProductsOthers.signal();
            notEnoughProductsFirst.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();

        }
    }

}
