package lab4.randomNaive;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private int[] data;
    private Lock lock;
    private int M;
    private Condition notEnoughSpace;
    private Condition notEnoughProducts;
    private Random rnd;

    public Buffer(int M){
        lock = new ReentrantLock();
        notEnoughSpace = lock.newCondition();
        notEnoughProducts = lock.newCondition();
        this.M = M;
        data = new int[2*M];
        rnd = new Random();

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
            while(getFreeSpace() < (portionSize)) {
                System.out.println("P[" + producerId + "] try to produce " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
                notEnoughSpace.await();
            }
            Logger.addMeasurementProducer(System.nanoTime() - timestamp, portionSize);
            System.out.println("P[" + producerId + "] produce " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
            insertProducts(portionSize);
            notEnoughProducts.signalAll();
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
            while(getUsedSpace() < portionSize) {
                System.out.println("C[" + consumerId + "] try to consume " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
                notEnoughProducts.await();
            }
            Logger.addMeasurementConsumer(System.nanoTime() - timestamp, portionSize);
            System.out.println("C[" + consumerId + "] consume " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
            getProducts(portionSize);
            notEnoughSpace.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();

        }
    }

}
