package lab4.stream;

public class Producer implements Runnable{

    private Buffer buff;
    private int sleepTime;

    public Producer(Buffer buff, int sleepTime){
        this.buff = buff;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(sleepTime);
                buff.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
