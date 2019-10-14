package lab1;

public class Producer implements Runnable {
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {

        for(int i = 0;  i < 10;   i++) {
           // try {
                buffer.putSem("message "+i);
         //   } catch (InterruptedException e) {
           //     e.printStackTrace();
            //}
        }

    }
}
