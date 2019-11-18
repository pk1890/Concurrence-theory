package lab4.fairSemaphores;


public class Consumer implements Runnable {
    private Buffer buff;
    private int consumerId;

    public Consumer(Buffer buff, int consumerId) {
        this.buff = buff;
        this.consumerId = consumerId;
    }

    @Override
    public void run() {
        while(true) {
            buff.consume(consumerId);
        }
    }
}