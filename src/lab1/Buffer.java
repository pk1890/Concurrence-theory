package lab1;

import lab2.BinarySemaphore;

public class Buffer {

    private String buff;

    private BinarySemaphore sem1, semBuffEmpty, semBuffFull;

    public Buffer(){
        sem1 = new BinarySemaphore();
        semBuffEmpty = new BinarySemaphore();
        semBuffFull = new BinarySemaphore();
        semBuffEmpty.P();
    }

    public void putSem(String s){
        semBuffFull.P();
        sem1.P();
        semBuffEmpty.V();
        buff = s;
        sem1.V();
    }

    public String takeSem(){
        semBuffEmpty.P();
        sem1.P();
        semBuffFull.V();
        String tmp = buff;
        buff = null;
        sem1.V();
        return tmp;
    }

    public synchronized void put(String s) throws InterruptedException {
        while (buff != null){
            wait();
        }
        buff = s;
        notifyAll();
    }

    public synchronized String take() throws InterruptedException {
        while (buff == null){
            wait();
        }
        String tmp;
        tmp = buff;
        buff = null;
        notifyAll();
        return tmp;

    }
}
