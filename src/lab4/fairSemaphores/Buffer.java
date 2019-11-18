package lab4.fairSemaphores;

import lab4.Logger;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private int[] data;
    private Lock lock;
    private int M;
    private Semaphore freeSpace;
    private Semaphore productsCount;
    private Random rnd;

    public Buffer(int M){
        lock = new ReentrantLock();
        freeSpace = new Semaphore(2*M, true);
        productsCount = new Semaphore(0, true);
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
//        lock.lock();
        try {
            timestamp = System.nanoTime();
            freeSpace.acquire(portionSize);
//            while(getFreeSpace() < (portionSize)) {
//                System.out.println("P[" + producerId + "] try to produce " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
//                notEnoughSpace.await();
//            }
            Logger.addMeasurementProducer(System.nanoTime() - timestamp, portionSize);
//            System.out.println("P[" + producerId + "] produce " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
            insertProducts(portionSize);
            productsCount.release(portionSize);
//            notEnoughProducts.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(rnd.nextInt(M - portionSize));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            lock.unlock();
        }
    }

    void consume(int consumerId) {
        int portionSize = rnd.nextInt(M-1)+1;
        long timestamp;
//        lock.lock();
        try {
            timestamp = System.nanoTime();
            productsCount.acquire(portionSize);
//            while(getUsedSpace() < portionSize) {
//                System.out.println("C[" + consumerId + "] try to consume " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
//                notEnoughProducts.await();
//            }
            Logger.addMeasurementConsumer(System.nanoTime() - timestamp, portionSize);
//            System.out.println("C[" + consumerId + "] consume " + portionSize + " items; Free space: " + getFreeSpace() + "; Products: " + getUsedSpace());
            getProducts(portionSize);
            freeSpace.release(portionSize);
//            notEnoughSpace.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(rnd.nextInt(M - portionSize));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            lock.unlock();

        }
    }

}
