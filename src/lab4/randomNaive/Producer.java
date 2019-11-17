package lab4.randomNaive;


class Producer implements Runnable {
    private Buffer buff;
    private int producerId;

    Producer(Buffer buff, int producerId) {
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