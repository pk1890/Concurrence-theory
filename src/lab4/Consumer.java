package lab4;

public class Consumer implements Runnable {
    private Buffer buff;
    private int sleepTime;

    public Consumer(Buffer buff, int sleepTime){
        this.buff = buff;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(sleepTime);
                buff.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
