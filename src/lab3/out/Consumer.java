package lab3.out;

public class Consumer implements Runnable {
    private BoundedBuffer buffer;
    private  int id;
    public Consumer(BoundedBuffer buffer, int id) {
        this.buffer = buffer;
        this.id = id;
    }

    public void run() {

        for(int i = 0;  i < 5;   i++) {
            //try {
            String message = null;
            try {
                message = (String)buffer.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(message + "::"+id);
            //} catch (InterruptedException e) {
             //   e.printStackTrace();
            //}
        }

    }
}
