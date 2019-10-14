package lab2;

public class CountingSemaphore {
    private int value;
    public CountingSemaphore(){
        value = 1;
    }

    public void V(){
        synchronized (this){
            value = 1;
            notifyAll();
        }
    }

    public void P(){
        synchronized (this) {
            while (value == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            value = 0;
        }
    }
}
