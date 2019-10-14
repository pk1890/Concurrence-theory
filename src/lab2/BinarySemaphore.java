package lab2;

public class BinarySemaphore {
    private boolean taken;
    public BinarySemaphore(){
        taken = false;
    }
    

    public synchronized void take(){
        while (taken){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        taken = true;
    }
}
