package lab2;

public class BinarySemaphore {
    private int value;
    public BinarySemaphore(){
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
