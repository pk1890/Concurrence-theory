package lab4.randomNaive;


class Consumer implements Runnable {
    private Buffer buff;
    private int consumerId;

    Consumer(Buffer buff, int consumerId) {
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