package lab4.randomFairWaiting;


public class Producer implements Runnable {
    private Buffer buff;
    private int producerId;

    public Producer(Buffer buff, int producerId) {
        this.buff = buff;
        this.producerId = producerId;
    }

    @Override
    public void run() {
        while(true) {
            buff.produce(producerId);
        }
    }
}