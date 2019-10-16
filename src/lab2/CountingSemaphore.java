package lab2;

public class CountingSemaphore {
    private int value;

    public int getValue(){
        return value;
    }

    public CountingSemaphore(int value){
        this.value = value;
    }

    public void V(){
        synchronized (this){
            value ++;
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
            value --;
        }
    }
}
